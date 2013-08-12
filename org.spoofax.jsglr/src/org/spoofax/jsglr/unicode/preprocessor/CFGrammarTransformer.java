package org.spoofax.jsglr.unicode.preprocessor;

import static org.spoofax.jsglr.unicode.UnicodeUtils.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.tests.unicode.MyTermTransformer;
import org.spoofax.jsglr.unicode.DefaultSequenceCreator;
import org.spoofax.jsglr.unicode.MixedUnicodeRange;
import org.spoofax.jsglr.unicode.RestrictionsSequenceCreator;
import org.spoofax.jsglr.unicode.SequenceCreator;
import org.spoofax.jsglr.unicode.UnicodeUtils;
import org.spoofax.terms.Term;
import org.spoofax.terms.TermFactory;

public class CFGrammarTransformer extends MyTermTransformer {

	private SequenceCreator currentSequenceCreator;

	public CFGrammarTransformer() {
		super(new TermFactory(), false);
	}

	@Override
	public IStrategoTerm preTransform(IStrategoTerm arg0) {
		// if (isConcGrammars(arg0)) {
		// LinkedList<IStrategoTerm> grammars = concGrammarsToList(arg0);
		// LinkedList<IStrategoTerm> convertedSyntaxProductions = new
		// LinkedList<IStrategoTerm>();
		// LinkedList<IStrategoTerm> convertedPrioritiesProductions = new
		// LinkedList<IStrategoTerm>();
		// ListIterator<IStrategoTerm> grammarIterator =
		// grammars.listIterator();
		// while (grammarIterator.hasNext()) {
		// IStrategoTerm grammar = grammarIterator.next();
		if (isSyntaxOrPriorities(arg0)) {
			this.currentSequenceCreator = new DefaultSequenceCreator();
		} else if (isRestriction(arg0)) {
			this.currentSequenceCreator = new RestrictionsSequenceCreator();
		}
		if (isContextFreeGrammar(arg0) | isContextFreePriorities(arg0)) {

			/*
			 * LinkedList<IStrategoTerm> productions =
			 * contextFreeGrammarToProductionList(grammar);
			 * Iterator<IStrategoTerm> productionsIterator =
			 * productions.iterator(); boolean changed = false; while
			 * (productionsIterator.hasNext()) { IStrategoTerm p =
			 * productionsIterator.next();
			 */
			// if (ProductionAST.isProduction(p)) {
			// ProductionAST prod = ProductionAST.selectProduction(p);
			// prod.unpack(p);
			// if (prod.containsUnicode()) {
			// productionsIterator.remove();
			// prod.insertLayoutAndWrapSorts();
			// convertedSyntaxProductions.add(prod.pack(factory));
			// changed = true;
			// }
			// }
			// }
			UnicodeSymbolVisitor v = new UnicodeSymbolVisitor();
			v.visit(arg0);
			if (v.isContainingUnicode()) {

				ProductionTransformer prodTransformer = new ProductionTransformer();
				IStrategoTerm transformedGrammar = prodTransformer.transform(arg0);
				// System.out.println(transformedGrammar);
				return transformedGrammar;
			} else {
				return arg0;
			}
			// convertedSyntaxProductions.addAll(prodTransformer.getConvertedSyntaxProductions());
			// grammarIterator.set(transformedGrammar);
			// }
			// }
			// if (!convertedSyntaxProductions.isEmpty()) {
			// grammars.add(makeSyntaxGrammar(convertedSyntaxProductions));
			// }
			// IStrategoTerm newGrammar = grammarListToConcGrammar(grammars);
			// return newGrammar;
		} else if (isLit(arg0)) {
			IStrategoTerm content = arg0.getSubterm(0);
			if (isUnicode(content)) {
				return this.currentSequenceCreator.createSequence(splitUnicodeString(unicodeToString(content)));
			} else if (isAscii(content)) {
				return makeAsciiLit(toJavaString(arg0.getSubterm(0)));
			} else {
				return arg0;
			}
		} else if (isAscii(arg0)) {
			return arg0.getSubterm(0);
		} else if (isCharClass(arg0) | isSimpleCharClass(arg0)) {
			IStrategoTerm eval = arg0;
			if (isCharClass(arg0)) {
				eval = arg0.getSubterm(0);
			}
			doNotRecur = true;
			MixedUnicodeRange r = evaluateCharClass(eval);
			// System.out.println("New AST: " + r.toAST());
			IStrategoTerm newAST = r.toAST(this.currentSequenceCreator);
		//	System.out.println(newAST);
			if (!newAST.equals(arg0)) {
				return newAST;
			}
		}

		return arg0;
	}

	@Override
	public IStrategoTerm postTransform(IStrategoTerm arg0) {

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
		} else if (isInvertCharClass(charclass)) {
			MixedUnicodeRange range = evaluateCharClass(charclass.getSubterm(0));
			range.invert();
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
