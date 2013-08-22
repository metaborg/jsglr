package org.spoofax.jsglr.unicode.transformer;

import java.util.LinkedList;
import java.util.ListIterator;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.jsglr.unicode.terms.UnicodeUtils;

/**
 * This class represents an AST of production. It contains the production
 * symbols, the result symbol and the annotation of the production. This class
 * is abstract to handle default and prefix productions.
 * 
 * @author moritzlichter
 * 
 */
public abstract class ProductionAST {

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

	/**
	 * Checks whether the given AST is an instance of the production type.
	 * 
	 * @param maybeProduction
	 *            the ast to check
	 * @return true when the ast matches this production type
	 */
	public abstract boolean matches(IStrategoTerm maybeProduction);

	/**
	 * Fills this object from the given ast.
	 * 
	 * @param productionTerm
	 *            the production ast to load from
	 */
	public abstract void unpack(IStrategoTerm productionTerm);

	/**
	 * Packs the content of this object to an ast and returns it.
	 * 
	 * @param factory
	 *            factory for creating terms
	 * @return the created ast
	 */
	public abstract IStrategoTerm pack(final ITermFactory factory);

	/**
	 * Checks whether this production contains unicode symbols.
	 * 
	 * @return true when the production contains unicode
	 */
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

	/**
	 * Desugars context free productions. This methods inserts optional layout
	 * between the sorts ond wraps the sorts in CF brackets. The result is a
	 * production which can be used in a syntax section.
	 */
	public void insertLayoutAndWrapSorts() {
		ListIterator<IStrategoTerm> iterator = this.symbols.listIterator();
		ProductionLEXTransformer transformer = new ProductionLEXTransformer();
		while (iterator.hasNext()) {
			IStrategoTerm term = iterator.next();
			// Insert LEX brackets around literals
			term = transformer.transform(term);
			if (!UnicodeUtils.isLex(term)) {
				// When no LEX was inserted, use a CF bracket
				term = UnicodeUtils.makeCFSymbol(term);
			}
			// Replace term with this one
			iterator.set(term);
			// Add optional layout
			if (iterator.hasNext()) {
				iterator.add(UnicodeUtils.makeLEXSymbol(UnicodeUtils.makeOptionalLayout()));
			}
		}
		this.resultSymbol = UnicodeUtils.makeCFSymbol(this.resultSymbol);
	}

	// STATIC UTILITY FUNCTIONS

	// Instances of AST for both production types

	private static final DefaultProductionAST defaultProd = new DefaultProductionAST();
	private static final PrefixProductionAST prefixProd = new PrefixProductionAST();

	/**
	 * Checks whether the given term is a production. When this method returns
	 * true {@link ProductionAST#selectProduction(IStrategoTerm)} returns a
	 * valid instance of {@link ProductionAST}.
	 * 
	 * @param term
	 *            the term to check
	 * @return true when the given term is a production
	 */
	public static final boolean isProduction(IStrategoTerm term) {
		return defaultProd.matches(term) || prefixProd.matches(term);
	}

	/**
	 * Selects the correct instance of {@link ProductionAST} for the given term.
	 * 
	 * @param term
	 *            the term to search for a production for
	 * @return the selected {@link ProductionAST} instance
	 */
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
