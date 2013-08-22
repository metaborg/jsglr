package org.spoofax.jsglr.unicode.transformer;

import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.charClassToSymbol;
import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.characterToInt;
import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.isAbsentCharRange;
import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.isAscii;
import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.isCharClass;
import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.isConcCharRange;
import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.isContextFreeGrammar;
import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.isContextFreePriorities;
import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.isDiffCharClass;
import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.isFollow;
import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.isIntersetCharClass;
import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.isInvertCharClass;
import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.isLit;
import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.isPresentCharRange;
import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.isRangeCharRange;
import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.isRestriction;
import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.isSimpleCharClass;
import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.isSyntaxOrPriorities;
import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.isUnicode;
import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.isUnicodeInvertCharClass;
import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.isUnionCharClass;
import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.makeAsciiLit;
import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.makeUnicodeCharClass;
import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.toJavaString;
import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.unicodeToString;

import java.util.Arrays;
import java.util.LinkedList;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.unicode.charranges.MixedUnicodeRange;
import org.spoofax.jsglr.unicode.terms.DefaultSequenceCreator;
import org.spoofax.jsglr.unicode.terms.RestrictionsSequenceCreator;
import org.spoofax.jsglr.unicode.terms.SequenceCreator;
import org.spoofax.jsglr.unicode.terms.UnicodeUtils;
import org.spoofax.jsglr.unicode.transformer.RestrictionsTransformer.Task;
import org.spoofax.terms.TermFactory;
import org.spoofax.terms.TermTransformer;

public class CFGrammarTransformer extends TermTransformer {

	private SequenceCreator currentSequenceCreator;
	private IStrategoTerm lockedTerm;

	public CFGrammarTransformer() {
		super(new TermFactory(), false);
	}

	@Override
	public IStrategoTerm preTransform(IStrategoTerm arg0) {
		// Lock: When a lockedTerm is set this method does not make any
		// modification to the terms
		if (this.lockedTerm != null) {
			return arg0;
		}
		// Switch the the current sequence creator for syntax/priorities or
		// restriction sections
		if (isSyntaxOrPriorities(arg0)) {
			this.currentSequenceCreator = new DefaultSequenceCreator();
		} else if (isRestriction(arg0)) {
			this.currentSequenceCreator = new RestrictionsSequenceCreator();
		}
		// Handle context-free grammars and priorities
		if (isContextFreeGrammar(arg0) | isContextFreePriorities(arg0)) {
			// Need to do something when unicode is contained
			UnicodeSymbolVisitor v = new UnicodeSymbolVisitor();
			v.visit(arg0);
			if (v.isContainingUnicode()) {
				// Desugar the grammar to syntax such that unicode can be
				// replaced
				ProductionTransformer prodTransformer = new ProductionTransformer();
				IStrategoTerm transformedGrammar = prodTransformer.transform(arg0);
				return transformedGrammar;
			} else {
				return arg0;
			}
		} else if (isFollow(arg0)) {
			// Handle follows in restrictions
			// Remove lists because the cannot be handles. replace them with
			// alts
			RestrictionsTransformer transformer = new RestrictionsTransformer();
			transformer.setTask(Task.REMOVE_LISTS);
			IStrategoTerm t = transformer.transform(arg0);
			return t;
		} else if (isLit(arg0)) {
			// Handle literals
			IStrategoTerm content = arg0.getSubterm(0);
			if (isUnicode(content)) {
				// Relace with sequence of unicode and plain ascii literals
				return this.currentSequenceCreator.createSequence(splitUnicodeString(unicodeToString(content)));
			} else if (isAscii(content)) {
				// Extract content of asciis
				return makeAsciiLit(toJavaString(arg0.getSubterm(0)));
			} else {
				return arg0;
			}
		} else if (isAscii(arg0)) {
			// extract context of ascii
			return arg0.getSubterm(0);
		} else if (isCharClass(arg0) | isSimpleCharClass(arg0)) {
			// Handle character classes and simple char classes

			// Extract content of charclasses when necessary
			IStrategoTerm eval = arg0;
			if (isCharClass(arg0)) {
				eval = arg0.getSubterm(0);
			}
			// Evaluate the char class
			MixedUnicodeRange r = evaluateCharClass(eval);
			// System.out.println("New AST: " + r.toAST());
			IStrategoTerm newAST = r.toAST(this.currentSequenceCreator);
			if (this.currentSequenceCreator instanceof DefaultSequenceCreator) {
				newAST = UnicodeUtils.makeBracket(newAST);
			}
			// Set the lock, otherwise evaluation is applied on the evaluation
			// char-classes resulting in stack overflow
			this.lockedTerm = newAST;
			return newAST;

		}

		return arg0;
	}

	@Override
	public IStrategoTerm postTransform(IStrategoTerm arg0) {
		if (arg0 == this.lockedTerm) {
			this.lockedTerm = null;
		}
		if (isFollow(arg0)) {
			RestrictionsTransformer transformer = new RestrictionsTransformer();
			transformer.setTask(Task.LIFT_ALTS);
			IStrategoTerm t = transformer.transform(arg0);
			transformer.setTask(Task.ADD_BRACKETS);
			t = transformer.transform(t);
			return t;
		}
		return arg0;
	}

	private LinkedList<IStrategoTerm> splitUnicodeString(String stringvalue) {
		int startPosition = 0;
		int currentPosition = 0;
		boolean isUnicode = false;
		LinkedList<IStrategoTerm> resultTerms = new LinkedList<IStrategoTerm>();
		while (currentPosition < stringvalue.length() - 1) {
			if (stringvalue.charAt(currentPosition) == '\\' && stringvalue.charAt(currentPosition + 1) == 'u') {
				// End or begin of unicode
				if (currentPosition != startPosition) {

					if (isUnicode) {
						String content = stringvalue.substring(startPosition, currentPosition + 2);
						// System.out.println(content);
						resultTerms.add(charClassToSymbol(makeUnicodeCharClass(content)));
					} else {
						String content = stringvalue.substring(startPosition, currentPosition);
						resultTerms.add(makeAsciiLit(content));
					}
				}
				// Skip the \\u
				currentPosition += 2;
				isUnicode = !isUnicode;
				if (!isUnicode) {
					startPosition = currentPosition;
				} else {
					startPosition = currentPosition - 2;
				}
			} else {
				currentPosition++;
			}

		}
		if (startPosition != stringvalue.length()) {
			resultTerms.add(makeAsciiLit(stringvalue.substring(startPosition)));
		}
		return resultTerms;
	}

	private MixedUnicodeRange evaluateCharClass(IStrategoTerm charclass) {
		if (UnicodeUtils.isSimpleCharClass(charclass)) {
			return evaluateCharRange(charclass.getSubterm(0));
		} else if (isInvertCharClass(charclass) | isUnicodeInvertCharClass(charclass)) {
			MixedUnicodeRange range = evaluateCharClass(charclass.getSubterm(0));
			if (isUnicodeInvertCharClass(charclass)) {
				range.invert();
			} else {
				range.invertASCII();
			}
			range.diff(new MixedUnicodeRange(7, 7));
			return range;
		} else {
			MixedUnicodeRange r1 = evaluateCharClass(charclass.getSubterm(0));
			MixedUnicodeRange r2 = evaluateCharClass(charclass.getSubterm(1));
			if (isDiffCharClass(charclass)) {
				r1.diff(r2);
			} else if (isIntersetCharClass(charclass)) {
				r1.intersect(r2);
			} else if (isUnionCharClass(charclass)) {
				r1.unite(r2);
			} else {
				throw new IllegalArgumentException("Given charclass is invalid: " + charclass);
			}
			return r1;
		}
	}

	private MixedUnicodeRange evaluateCharRange(IStrategoTerm charrange) {
		if (isAbsentCharRange(charrange)) {
			return new MixedUnicodeRange();
		} else if (isPresentCharRange(charrange)) {
			return evaluateCharRange(charrange.getSubterm(0));

		} else if (isConcCharRange(charrange)) {
			MixedUnicodeRange r1 = evaluateCharRange(charrange.getSubterm(0));
			MixedUnicodeRange r2 = evaluateCharRange(charrange.getSubterm(1));
			r1.unite(r2);
			return r1;
		} else if (isRangeCharRange(charrange)) {
			int start = characterToInt(charrange.getSubterm(0));
			int end = characterToInt(charrange.getSubterm(1));
			return new MixedUnicodeRange(start, end);
		} else {
			// System.out.print(charrange);
			int chr = characterToInt(charrange);
			// System.out.println(" " + chr);
			return new MixedUnicodeRange(chr, chr);
		}
	}

}
