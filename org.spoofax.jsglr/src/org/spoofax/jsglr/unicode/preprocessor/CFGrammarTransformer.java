package org.spoofax.jsglr.unicode.preprocessor;

import static org.spoofax.jsglr.unicode.UnicodeUtils.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.tests.unicode.MyTermTransformer;
import org.spoofax.jsglr.unicode.MixedUnicodeRange;
import org.spoofax.jsglr.unicode.UnicodeUtils;
import org.spoofax.terms.TermFactory;

public class CFGrammarTransformer extends MyTermTransformer {

	public CFGrammarTransformer() {
		super(new TermFactory(), false);
	}
	
	private boolean doNotRecur = false;
	int ident = 0;
	private String getIdent() {
		String s = "";
		for (int i = 0; i <ident; i++) {
			s += " ";
		}
		return s;
	}
	public final IStrategoTerm transform(IStrategoTerm term) {
		term = preTransform(term);
		if (term == null) {
			return null;
		} else if (doNotRecur) {
			doNotRecur = false;
			System.out.println("Do Not recur in:" + term);
			return postTransform(term);
		} else {
			System.out.println(getIdent() + term);
			ident ++;
			IStrategoTerm t= postTransform(simpleAll(term));
			ident --;
			return t;
		}
	}

	@Override
	public IStrategoTerm preTransform(IStrategoTerm arg0) {
		if (isConcGrammars(arg0)) {
			LinkedList<IStrategoTerm> grammars = concGrammarsToList(arg0);
			LinkedList<IStrategoTerm> convertedProductions = new LinkedList<IStrategoTerm>();
			ListIterator<IStrategoTerm> grammarIterator = grammars.listIterator();
			while (grammarIterator.hasNext()) {
				IStrategoTerm grammar = grammarIterator.next();
				if (isContextFreeGrammar(grammar)) {
					LinkedList<IStrategoTerm> productions = contextFreeGrammarToProductionList(grammar);
					Iterator<IStrategoTerm> productionsIterator = productions.iterator();
					boolean changed = false;
					while (productionsIterator.hasNext()) {
						IStrategoTerm p = productionsIterator.next();
						if (ProductionAST.isProduction(p)) {
							ProductionAST prod = ProductionAST.selectProduction(p);
							prod.unpack(p);
							if (prod.containsUnicode()) {
								productionsIterator.remove();
								prod.insertLayoutAndWrapSorts();
								convertedProductions.add(prod.pack(factory));
								changed = true;
							}
						}
					}
					if (changed) {
						grammarIterator.set(makeContextFreeGrammar(productions));
					}
				}
			}
			if (!convertedProductions.isEmpty()) {
				grammars.add(makeSyntaxGrammar(convertedProductions));
			}
			IStrategoTerm newGrammar = grammarListToConcGrammar(grammars);
			return newGrammar;
		} else if (isLit(arg0)) {
			IStrategoTerm content = arg0.getSubterm(0);
			if (isUnicode(content)) {
				return makeSymbolSeq(splitUnicodeString(unicodeToString(content)));
			}
		} else if (isCharClass(arg0)) {
			doNotRecur = true;
			System.out.println("CharRange: " + arg0);
			MixedUnicodeRange r = evaluateCharClass(arg0.getSubterm(0));
			System.out.println("New AST: " + r.toAST());
			IStrategoTerm newAST =  r.toAST();
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
					startPosition = currentPosition -2;
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
			return range;
		} else {
			System.out.println(charclass);
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
		} else if (isRangeCharRange(charrange)){
			int start = characterToInt(charrange.getSubterm(0));
			int end = characterToInt(charrange.getSubterm(1));
			return new MixedUnicodeRange(start, end);
		} else {
			int chr = characterToInt(charrange);
			return new MixedUnicodeRange(chr, chr);
		} 
	}
	
	
}
