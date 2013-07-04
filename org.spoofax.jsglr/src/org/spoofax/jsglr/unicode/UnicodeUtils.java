package org.spoofax.jsglr.unicode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedList;

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

	public static boolean isProduction(IStrategoTerm term) {
		return isConstructors(term, "prod", "prefix-fun");
	}

	public static boolean isSort(IStrategoTerm term) {
		return isConstructors(term, "sort");
	}

	public static boolean isLit(IStrategoTerm term) {
		return isConstructors(term, "lit");
	}

	public static boolean isUnicode(IStrategoTerm term) {
		return isConstructors(term, "unicode");
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
				factory.makeAppl(factory.makeConstructor("layout", 0)));
	}

	public static IStrategoTerm makeCFSort(IStrategoTerm term) {
		forceConstructors(term, "sort");
		return factory.makeAppl(factory.makeConstructor("cf", 1), term);
	}

	public static IStrategoTerm makeSymbolSeq(LinkedList<IStrategoTerm> symbolList) {
		if (symbolList.size() == 1) {
			return symbolList.getFirst();
		} else {
			return factory.makeAppl(factory.makeConstructor("seq", 2), symbolList.getFirst(),
					factory.makeList(symbolList.subList(1, symbolList.size())));
		}
	}

	public static IStrategoTerm makeAsciiLit(String string) {
		return factory.makeAppl(factory.makeConstructor("lit", 1),
				factory.makeAppl(factory.makeConstructor("ascii", 1), factory.makeString("\"" + string + "\"")));
	}

	public static IStrategoTerm makeUnicodeCharClass(String unicodeSymbolIdent) {
		return factory.makeAppl(
				factory.makeConstructor("simple-char-class", 1),
				factory.makeAppl(
						factory.makeConstructor("present", 1),
						factory.makeAppl(factory.makeConstructor("unicodechar", 1),
								factory.makeString(unicodeSymbolIdent))));
	}

	public static String unicodeToString(IStrategoTerm term) {
		forceConstructors(term, "unicode");
		String content = Term.asJavaString(term.getSubterm(0));
		return content.substring(1, content.length() - 1);
	}
}
