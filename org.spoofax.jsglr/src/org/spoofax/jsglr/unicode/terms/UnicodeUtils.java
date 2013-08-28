package org.spoofax.jsglr.unicode.terms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.spoofax.NotImplementedException;
import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.terms.Term;
import org.spoofax.terms.TermFactory;

public class UnicodeUtils {

	public static String readFile(File path, Charset encoding) throws IOException {
		BufferedReader in;
		if (encoding == null) {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
		} else {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(path), encoding));
		}
		StringBuilder builder = new StringBuilder();
		String temp = in.readLine();
		while (temp != null) {
			builder.append(temp);
			temp = in.readLine();
			if (temp != null) {
				builder.append("\n");
			}
		}
		in.close();
		return builder.toString();
	}
	public static void writeFile(String content, File dest) throws IOException {
		writeFile(content, dest, Charset.forName("UTF-8"));
	}
	
	public static void writeFile(String content, File dest, Charset encoding) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dest), encoding));
		writer.append(content);
		writer.flush();
		writer.close();
	}

	public static ITermFactory factory = new TermFactory();

	public static boolean isConstructors(IStrategoTerm term, String... constructors) {
		String name = Term.tryGetName(term);
		if (name == null) {
			return false;
		}
		boolean success = false;
		for (String c : constructors) {
			success = success || c.equals(name);
		}
		return success;
	}

	public static void forceConstructors(IStrategoTerm term, String... constructors) {
		if (!isConstructors(term, constructors)) {
			throw new IllegalArgumentException("Expected a term of constructor: " + Arrays.toString(constructors));
		}
	}

	public static boolean isConcGrammars(IStrategoTerm term) {
		return isConstructors(term, "conc-grammars");
	}

	public static boolean isContextFreeGrammar(IStrategoTerm term) {
		return isConstructors(term, "context-free-syntax");
	}
	
	public static boolean isContextFreePriorities(IStrategoTerm term) {
		return isConstructors(term, "context-free-priorities");
	}
	
	public static boolean isRestriction(IStrategoTerm term) {
		return isConstructors(term, "context-free-restrictions", "lexical-restrictions", "restrictions");
	}
	
	public static boolean isVariables(IStrategoTerm term) {
		return isConstructors(term, "lexical-variables", "variables");
	}
	
	public static boolean isFollow(IStrategoTerm term) {
		return isConstructors(term, "follow");
	}
	
	public static boolean isSyntaxOrPriorities(IStrategoTerm term) {
		return isConstructors(term, "context-free-syntax", "context-free-priorities", "syntax", "priorities", "lexical-syntax", "lexical-priorities");
	}

	public static boolean isProduction(IStrategoTerm term) {
		return isConstructors(term, "prod", "prefix-fun");
	}

	public static boolean isSort(IStrategoTerm term) {
		return isConstructors(term, "sort");
	}

	public static boolean isLit(IStrategoTerm term) {
		return isConstructors(term, "lit");
	}
	
	public static boolean isList(IStrategoTerm term) {
		return isConstructors(term, "list");
	}


	public static boolean isUnicode(IStrategoTerm term) {
		return isConstructors(term, "unicode");
	}
	
	public static boolean isAscii(IStrategoTerm term) {
		return isConstructors(term, "ascii");
	}
	
	public static boolean isPresentCharRange(IStrategoTerm term) {
		return isConstructors(term, "present");
	}
	
	public static boolean isAbsentCharRange(IStrategoTerm term) {
		return isConstructors(term, "absent");
	}
	
	public static boolean isConcCharRange(IStrategoTerm term) {
		return isConstructors(term, "conc");
	}
	
	public static boolean isRangeCharRange(IStrategoTerm term) {
		return isConstructors(term, "range");
	}
	
	public static boolean isSimpleCharClass(IStrategoTerm term) {
		return isConstructors(term, "simple-charclass");
	}
	
	public static boolean isInvertCharClass(IStrategoTerm term) {
		return isConstructors(term, "comp");
	}
	
	public static boolean isUnicodeInvertCharClass(IStrategoTerm term) {
		return isConstructors(term, "unicodecomp");
	}
	
	public static boolean isDiffCharClass(IStrategoTerm term) {
		return isConstructors(term, "diff");
	}
	
	public static boolean isIntersetCharClass(IStrategoTerm term) {
		return isConstructors(term, "isect");
	}
	
	public static boolean isUnionCharClass(IStrategoTerm term) {
		return isConstructors(term, "union");
	}
	
	public static boolean isCharClass(IStrategoTerm term) {
		return isConstructors(term, "char-class");
	}
	
	public static boolean isLex(IStrategoTerm term) {
		return isConstructors(term, "lex");
	}
	

	public static IStrategoTerm makeConcGrammer(IStrategoTerm grammar1, IStrategoTerm grammar2) {
		return factory.makeAppl(factory.makeConstructor("conc-grammars", 2), grammar1, grammar2);
	}

	public static LinkedList<IStrategoTerm> concGrammarsToList(IStrategoTerm term) {
		if (!isConcGrammars(term)) {
			return null;
		}
		LinkedList<IStrategoTerm> l = new LinkedList<IStrategoTerm>();
		concGrammarsToList(term, l);
		return l;
	}

	private static void concGrammarsToList(IStrategoTerm term, LinkedList<IStrategoTerm> list) {
		IStrategoTerm grammar1 = term.getSubterm(0);
		IStrategoTerm grammar2 = term.getSubterm(1);
		if (isConcGrammars(grammar1)) {
			concGrammarsToList(grammar1, list);
		} else {
			list.add(grammar1);
		}
		list.add(grammar2);
	}

	public static IStrategoTerm grammarListToConcGrammar(LinkedList<IStrategoTerm> grammars) {
		if (grammars.size() == 1) {
			return grammars.getFirst();
		}
		IStrategoTerm secondGrammar = grammars.removeLast();
		IStrategoTerm firstGrammar = grammarListToConcGrammar(grammars);
		return makeConcGrammer(firstGrammar, secondGrammar);
	}

	public static LinkedList<IStrategoTerm> contextFreeGrammarToProductionList(IStrategoTerm contextFreeGrammar) {
		if (!isContextFreeGrammar(contextFreeGrammar)) {
			throw new IllegalArgumentException("Given term is not a context-free grammar");
		}
		IStrategoList productions = (IStrategoList) contextFreeGrammar.getSubterm(0);
		LinkedList<IStrategoTerm> productionsList = new LinkedList<IStrategoTerm>();
		for (IStrategoTerm t : productions.getAllSubterms()) {
			if (isProduction(t)) {
				productionsList.add(t);
			} else {
				throw new IllegalArgumentException("Grammar does not only consist of productions");
			}
		}
		return productionsList;
	}

	private static IStrategoAppl makeGrammar(String cons, LinkedList<IStrategoTerm> productionList) {
		return factory.makeAppl(factory.makeConstructor(cons, 1), factory.makeList(productionList));
	}

	public static IStrategoTerm makeContextFreeGrammar(LinkedList<IStrategoTerm> productionList) {
		return makeGrammar("context-free-syntax", productionList);
	}

	public static IStrategoTerm makeSyntaxGrammar(LinkedList<IStrategoTerm> productionList) {
		return makeGrammar("syntax", productionList);
	}
	
	public static IStrategoTerm makeSyntaxGrammar(IStrategoTerm productions) {
		return factory.makeAppl(factory.makeConstructor("syntax", 1), productions);
	}

	public static IStrategoTerm makePriorities(IStrategoTerm priorities) {
		return factory.makeAppl(factory.makeConstructor("priorities", 1), priorities); 
	}
	
	public static boolean isPrefixProduction(IStrategoTerm term) {
		return isConstructors(term, "prefix-fun");
	}

	public static LinkedList<IStrategoTerm> strategoListToLinkedList(IStrategoList list) {
		LinkedList<IStrategoTerm> termsList = new LinkedList<IStrategoTerm>();
		for (IStrategoTerm symbol : list.getAllSubterms()) {
			termsList.add(symbol);
		}
		return termsList;
	}

	public static IStrategoTerm makeOptionalLayout() {
		return factory.makeAppl(factory.makeConstructor("opt", 1),
				factory.makeAppl(factory.makeConstructor("iter-star", 1), factory.makeAppl(factory.makeConstructor("layout", 0))));
	}

	public static IStrategoTerm makeCFSymbol(IStrategoTerm term) {
		return factory.makeAppl(factory.makeConstructor("cf", 1), term);
	}
	
	public static IStrategoTerm makeLEXSymbol(IStrategoTerm term) {
		return factory.makeAppl(factory.makeConstructor("lex", 1), term);
	}

	public static IStrategoTerm makeSymbolSeq(IStrategoTerm t1, IStrategoTerm t2) {
		
			return factory.makeAppl(factory.makeConstructor("seq", 2), t1,factory.makeList(t2));
		
	}
	
	public static IStrategoTerm makeRestrictionSymbolSeq(IStrategoTerm t1, IStrategoTerm t2) {
		
		return factory.makeAppl(factory.makeConstructor("seq", 2), t1,t2);
	
}
	
	public static IStrategoTerm makeBracket(IStrategoTerm term) {
		return factory.makeAppl(factory.makeConstructor("seq", 2), term,
				factory.makeList(new ArrayList<IStrategoTerm>()));
	}
	
	public static IStrategoTerm makeSymbolSeq(IStrategoTerm first, Collection<IStrategoTerm> rest) {
		if (rest.size() == 0) {
			return first;
		} else {
			return factory.makeAppl(factory.makeConstructor("seq", 2), first,
					factory.makeList(rest));
		}
	}
	
	public static IStrategoTerm makeSingle(IStrategoTerm term) {
		
		return factory.makeAppl(factory.makeConstructor("single", 1), term);
	
}

	public static IStrategoTerm makeAsciiLit(String string) {
		//return factory.makeAppl(factory.makeConstructor("lit", 1),
		//		factory.makeAppl(factory.makeConstructor("ascii", 1), factory.makeString("\"" + string + "\"")));
		return factory.makeAppl(factory.makeConstructor("lit", 1),factory.makeString(string));
	}

	public static IStrategoTerm makeUnicodeCharClass(String unicodeSymbolIdent) {
		return factory.makeAppl(
				factory.makeConstructor("simple-charclass", 1),
				factory.makeAppl(
						factory.makeConstructor("present", 1),
						factory.makeAppl(factory.makeConstructor("unicodechar", 1),
								factory.makeString(unicodeSymbolIdent))));
	}

	public static String toJavaString(IStrategoTerm term) {
		String s = Term.asJavaString(term.getSubterm(0));
		return s.substring(1, s.length() -1);
	}
	
	public static String unicodeToString(IStrategoTerm term) {
		forceConstructors(term, "unicode");
		return toJavaString(term);
	}

	private static void validateCharClass(int symbol) {
		if (symbol > 255 || symbol < 0) {
			throw new IllegalArgumentException("Symbol " + symbol + " is out of range");
		}
	}

	public static IStrategoTerm makeCharClass(int symbol) {
		validateCharClass(symbol);
		return makePresentSimpleCharClass(makeNumericCharacter(symbol));
	}

	private static IStrategoAppl makeNumericCharacter(int symbol) {
		return factory.makeAppl(factory.makeConstructor("numeric", 1), factory.makeString("\\" + symbol));
	}

	private static IStrategoTerm makePresentSimpleCharClass(IStrategoTerm charrange) {
		return factory.makeAppl(factory.makeConstructor("simple-charclass", 1),
				factory.makeAppl(factory.makeConstructor("present", 1), charrange));
	}
	
	public static IStrategoTerm charClassToSymbol(IStrategoTerm charclass) {
		return factory.makeAppl(factory.makeConstructor("char-class", 1), charclass);
	}

	public static IStrategoTerm makeCharRange(int start, int end) {
		validateCharClass(start);
		validateCharClass(end);
		int newStart = Math.min(start, end);
		int newEnd = Math.max(start, end);
		IStrategoTerm startTerm = makeNumericCharacter(newStart);
		IStrategoTerm endTerm = makeNumericCharacter(newEnd);
		IStrategoTerm range = factory.makeAppl(factory.makeConstructor("range", 2), startTerm, endTerm);
		return makePresentSimpleCharClass(range);
	}
	
	public static IStrategoTerm makeOrSymbol(IStrategoTerm term1, IStrategoTerm term2) {
		return factory.makeAppl(factory.makeConstructor("alt", 2), term1, term2);
	}

	public static IStrategoTerm makeOrSymbol(List<IStrategoTerm> terms) {
		if (terms.size() == 0) {
			throw new IllegalArgumentException("Connot create or over no terms");
		} else if (terms.size() == 1) {
			return terms.get(0);
		}
		IStrategoTerm left = terms.get(0);
		IStrategoTerm second = makeOrSymbol(terms.subList(1, terms.size()));
		return makeOrSymbol(left, second);
	}
	
	public static IStrategoTerm makeEmptySymbol() {
		return factory.makeAppl(factory.makeConstructor("empty", 0));
	}
	
	public static int characterToInt(IStrategoTerm term) {
		if (isConstructors(term, "numeric")) {
			return Integer.parseInt(Term.asJavaString(term.getSubterm(0)).substring(1));
		} else if (isConstructors(term, "short")) {
			String str = Term.asJavaString(term.getSubterm(0));
			if (str.length() == 1) {
				return str.charAt(0);
			} else {
				if (str.equals("\\n")) {
					return '\n';
				} else if (str.equals("\\r")) {
					return '\r';
				} else if (str.equals("\\t")) {
					return '\t';
				} else if (str.length() == 2) {
					return str.charAt(1);
				}
				
			}
		} else if (isConstructors(term, "unicodechar")) {
			String str = Term.asJavaString(term.getSubterm(0));
			return Integer.parseInt(str.substring(2, str.length()-2), 16);
		} else if (isConstructors(term, "top")) {
			
		} else if (isConstructors(term, "eof")) {
		
		} else if (isConstructors(term, "bot")) {
			
		} else if (isConstructors(term, "label_start")) {
			
		} else {
			throw new IllegalArgumentException("Given term is not a valid character: " + term);
		}
		throw new NotImplementedException("characterToLong not implemented for " + term );
	}

}
