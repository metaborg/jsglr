package org.spoofax.jsglr.unicode.preprocessor;

import static org.spoofax.jsglr.unicode.UnicodeUtils.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import org.apache.tools.ant.taskdefs.condition.IsLastModified;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.unicode.UnicodeRangePair;
import org.spoofax.terms.Term;
import org.spoofax.terms.TermFactory;
import org.spoofax.terms.TermTransformer;

public class CFGrammarTransformer extends TermTransformer {

	public CFGrammarTransformer() {
		super(new TermFactory(), false);
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
						resultTerms.add(makeUnicodeCharClass(content));
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

	private UnicodeRangePair evaluateCharClass(IStrategoTerm charclass) {
		
	}
	
	
}
