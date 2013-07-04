package org.spoofax.jsglr.unicode.preprocessor;

import java.util.LinkedList;
import java.util.ListIterator;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.jsglr.unicode.UnicodeUtils;

public abstract class ProductionAST {
	
	private static final String SORT_CONS = "sort";
	private static final String LIT_CONS = "lit";
	private static final String ASCII_CONS = "ascii";
	public static final String UNICODE_CONS = "unicode";
	private static final String CHAR_CLASS_CONS = "char-class";
	private static final String LAYOUT = "LAYOUT";

	//ci-string:SingleQuotedStrCon -> Symbol {cons("ci-lit")}
	//"<START>" -> Symbol {cons("start")}
   // "<Start>" -> Symbol {cons("file-start")}
	
	protected LinkedList<IStrategoTerm> symbols;
	protected IStrategoTerm resultSymbol;
	protected IStrategoTerm attributes;
	
	private final UnicodeSymbolVisitor unicodeSymbolVisitor;
	
	protected ProductionAST() {
		this.unicodeSymbolVisitor = new UnicodeSymbolVisitor();
	}

	public LinkedList<IStrategoTerm> getSymbols() {
		return symbols;
	}

	public IStrategoTerm getResultSymbol() {
		return resultSymbol;
	}

	public IStrategoTerm getAttributes() {
		return attributes;
	}

	public abstract boolean matches(IStrategoTerm maybeProduction);

	public abstract void unpack(IStrategoTerm productionTerm);

	public abstract IStrategoTerm pack(final ITermFactory factory);
	
	
	public boolean containsUnicode() {
		this.unicodeSymbolVisitor.reset();
		for (IStrategoTerm t : this.symbols) {
			this.unicodeSymbolVisitor.visit(t);
			if (this.unicodeSymbolVisitor.isContainingUnicode()) {
				return true;
			}
		}
		this.unicodeSymbolVisitor.visit(this.resultSymbol);
		return this.unicodeSymbolVisitor.isContainingUnicode();
	}
	
	public void insertLayoutAndWrapSorts() {
		ListIterator<IStrategoTerm> iterator = this.symbols.listIterator();
		while (iterator.hasNext()) {
			IStrategoTerm term = iterator.next();
			if (UnicodeUtils.isSort(term)) {
				iterator.set(UnicodeUtils.makeCFSort(term));
			}
			if (iterator.hasNext()) {
				iterator.add(UnicodeUtils.makeOptionalLayout());
			}
		}
	}
	

	private static final DefaultProductionAST defaultProd = new DefaultProductionAST();
	private static final PrefixProductionAST prefixProd = new PrefixProductionAST();

	public static final boolean isProduction(IStrategoTerm term) {
		return defaultProd.matches(term) || prefixProd.matches(term);
	}

	public static final ProductionAST selectProduction(IStrategoTerm term) {
		if (defaultProd.matches(term)) {
			return defaultProd;
		} else if (prefixProd.matches(term)) {
			return prefixProd;
		} else {
			throw new IllegalArgumentException("New Production found for term: " + term);
		}
	}

}
