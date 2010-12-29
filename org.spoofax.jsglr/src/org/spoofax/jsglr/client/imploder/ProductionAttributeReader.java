package org.spoofax.jsglr.client.imploder;

import static org.spoofax.terms.StrategoListIterator.iterable;
import static org.spoofax.terms.Term.isTermAppl;
import static org.spoofax.terms.Term.isTermNamed;
import static org.spoofax.terms.Term.javaString;
import static org.spoofax.terms.Term.termAt;

import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoConstructor;
import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoNamed;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;


/**
 * Extracts attributes from parse table productions.
 * 
 * @author Lennart Kats <L.C.L.Kats add tudelft.nl>
 */
public class ProductionAttributeReader {
	
	private final int PARAMETRIZED_SORT_NAME = 0;
	
	private final int PARAMETRIZED_SORT_ARGS = 1;
	
	private final int ALT_SORT_LEFT = 0;
	
	private final int ALT_SORT_RIGHT = 1;
	
	protected final IStrategoConstructor sortFun;
	
	protected final IStrategoConstructor parameterizedSortFun;
	
	protected final IStrategoConstructor attrsFun;
	
	protected final IStrategoConstructor noAttrsFun;
	
	protected final IStrategoConstructor preferFun;
	
	protected final IStrategoConstructor avoidFun;
	
	private final IStrategoConstructor varSymFun;
	
	private final IStrategoConstructor altFun;
	
	private final IStrategoConstructor charClassFun;
	
	private final IStrategoConstructor litFun;
	
	private final IStrategoConstructor cilitFun;
	
	private final IStrategoConstructor lexFun;
	
	private final IStrategoConstructor optFun;
	
	private final IStrategoConstructor layoutFun;
	
	private final IStrategoConstructor cfFun;
	
	private final IStrategoConstructor varsymFun;
	
	private final IStrategoConstructor seqFun;
	
	private final IStrategoConstructor iterFun;
	
	private final IStrategoConstructor iterStarFun;
	
	private final IStrategoConstructor iterPlusFun;
	
	private final IStrategoConstructor iterSepFun;
	
	private final IStrategoConstructor iterStarSepFun;
	
	private final IStrategoConstructor iterPlusSepFun;
	
	public ProductionAttributeReader(ITermFactory factory) {
		sortFun = factory.makeConstructor("sort", 1);
		parameterizedSortFun =
			factory.makeConstructor("parameterized-sort", 2);
		attrsFun = factory.makeConstructor("attrs", 1);
		noAttrsFun = factory.makeConstructor("no-attrs", 0);
		preferFun = factory.makeConstructor("prefer", 0);
		avoidFun = factory.makeConstructor("avoid", 0);
		varSymFun = factory.makeConstructor("varsym", 1);
		altFun = factory.makeConstructor("alt", 2);
		charClassFun = factory.makeConstructor("char-class", 1);
		litFun = factory.makeConstructor("lit", 1);
		cilitFun = factory.makeConstructor("cilit", 1);
		lexFun = factory.makeConstructor("lex", 1);
		optFun = factory.makeConstructor("opt", 1);
		layoutFun = factory.makeConstructor("layout", 0);
		cfFun = factory.makeConstructor("cf", 1);
		varsymFun = factory.makeConstructor("varsym", 1);
		seqFun = factory.makeConstructor("seq", 2);
		iterFun = factory.makeConstructor("iter", 1);
		iterStarFun = factory.makeConstructor("iter-star", 1);
		iterPlusFun = factory.makeConstructor("iter-plus", 1);
		iterSepFun = factory.makeConstructor("iter-sep", 2);
		iterStarSepFun = factory.makeConstructor("iter-star-sep", 2);
		iterPlusSepFun = factory.makeConstructor("iter-plus-sep", 2);
	}

	public String getConsAttribute(IStrategoAppl attrs) {
		IStrategoTerm consAttr = getAttribute(attrs, "cons");
		return consAttr == null ? null : ((IStrategoNamed) consAttr).getName();
	}
	
	// FIXME: support meta-var constructors
	public String getMetaVarConstructor(IStrategoAppl rhs) {
		if (rhs.getSubtermCount() == 1 && varSymFun == rhs.getConstructor()) {
			return isIterFun(((IStrategoAppl) termAt(rhs, 0)).getConstructor())
					? "meta-listvar"
					: "meta-var";
		}
		return null;
	}
	
	public IStrategoTerm getAstAttribute(IStrategoAppl attrs) {
		return getAttribute(attrs, "ast");
	}
	
	public boolean isIndentPaddingLexical(IStrategoAppl attrs) {
		return getAttribute(attrs, "indentpadding") != null;
	}

	/** Return the contents of a term attribute (e.g., "cons"), or null if not found. */
	public IStrategoTerm getAttribute(IStrategoAppl attrs, String attrName) {
		if (attrs.getConstructor() == noAttrsFun)
			return null;
		
		IStrategoList list = termAt(attrs, 0);
		
		for (IStrategoTerm attr : iterable(list)) {			
			if (isTermNamed(attr)) {
				IStrategoNamed namedAttr = (IStrategoNamed) attr;
				if (namedAttr.getName().equals("term")) {
					namedAttr = termAt(namedAttr, 0);
					
					if (namedAttr.getName().equals(attrName))
						return namedAttr.getSubtermCount() == 1 ? termAt(namedAttr, 0) : namedAttr;
				}				
			}
		}
		
		return null; // no cons found
	}
	
	/** 
	 * Get the RTG sort name of a production RHS, or for lists, the RTG element sort name.
	 */
    public String getSort(IStrategoAppl rhs) {
    	for (IStrategoTerm current = rhs; current.getSubtermCount() > 0 && isTermAppl(current); current = termAt(current, 0)) {
    		IStrategoAppl currentAppl = (IStrategoAppl) current;
			IStrategoConstructor cons = currentAppl.getConstructor();
			if (cons == sortFun)
    			return javaString(termAt(current, 0));
    		if (cons == parameterizedSortFun)
    			return getParameterizedSortName(currentAppl);
    		if (cons == charClassFun)
    			return null;
    		if (cons == altFun)
    			return getAltSortName(currentAppl);
    	}
    	
    	return null;
    }
    
    private String getParameterizedSortName(IStrategoAppl parameterizedSort) {
    	StringBuilder result = new StringBuilder();
    	
    	result.append(((IStrategoNamed)termAt(parameterizedSort, PARAMETRIZED_SORT_NAME)).getName());
    	result.append('_');
    	
		IStrategoList args = termAt(parameterizedSort, PARAMETRIZED_SORT_ARGS);
		
        for (IStrategoTerm arg : iterable(args)) {
			result.append(((IStrategoNamed) arg).getName());
		}
		
		return result.toString();
    }
    
    private String getAltSortName(IStrategoAppl node) {
		String left = getSort((IStrategoAppl) termAt(node, ALT_SORT_LEFT));
		String right = getSort((IStrategoAppl) termAt(node, ALT_SORT_RIGHT));
		
		// HACK: In the RTG, alt sorts appear with a number at the end
		return left + "_" + right + "0";
    }

	/**
	 * Identifies lexical parse tree nodes.
	 * 
	 * @see AsfixAnalyzer#isVariableNode(ATermAppl)
	 *      Identifies variables, which are usually treated similarly to
	 *      lexical nodes.
	 * 
	 * @return true if the current node is lexical.
	 */
	public boolean isNonContextFree(IStrategoAppl rhs) {
		return (lexFun == rhs.getConstructor() || isLiteral(rhs)
		    || isLayout(rhs)) || isVariableNode(rhs);
	}
	
	public boolean isLexical(IStrategoAppl rhs) {
		return lexFun == rhs.getConstructor();
	}

	public boolean isLayout(IStrategoAppl rhs) {
		IStrategoTerm details = termAt(rhs, 0);
		if (!isTermAppl(details))
			return false;
		
		if (optFun == ((IStrategoAppl) details).getConstructor())
			details = termAt(details, 0);
		
		return layoutFun == ((IStrategoAppl) details).getConstructor();
	}

	public boolean isLiteral(IStrategoAppl rhs) {
		IStrategoConstructor fun = rhs.getConstructor();
		return litFun == fun || cilitFun == fun;
	}
	
	public boolean isList(IStrategoAppl rhs) {
		IStrategoAppl details = rhs;
		
		if (rhs.getConstructor() == cfFun)
			details = termAt(details, 0);
		              	
	  	if (details.getConstructor() == optFun)
	  		details = termAt(details, 0);
	  	
		IStrategoConstructor fun = details.getConstructor();
		
		 // FIXME: Spoofax/159: AsfixImploder creates tuples instead of lists for seqs
		return isIterFun(fun) || seqFun == fun;
	}

	public boolean isIterFun(IStrategoConstructor fun) {
		return iterFun == fun || iterStarFun == fun || iterPlusFun == fun
				|| iterSepFun == fun || iterStarSepFun == fun || iterPlusSepFun == fun;
	}

	/**
	 * Identifies parse tree nodes that begin variables.
	 * 
	 * @see #isVariableNode(ATermAppl) 
	 * @return true if the current node is lexical.
	 */
	public boolean isVariableNode(IStrategoAppl rhs) {
		return varsymFun == rhs.getConstructor();
	}

	public boolean isLexLayout(IStrategoAppl rhs) {
		if (rhs.getSubtermCount() != 1) return false;
		IStrategoTerm child = rhs.getSubterm(0);
		return isTermAppl(child) && layoutFun == ((IStrategoAppl) child).getConstructor()
			&& lexFun == rhs.getConstructor();
	}

	public boolean isOptional(IStrategoAppl rhs) {
		if (rhs.getConstructor() == optFun)
			return true;
		IStrategoTerm contents = termAt(rhs, 0);
		return contents.getSubtermCount() == 1
			&& isTermAppl(contents)
			&& ((IStrategoAppl) contents).getConstructor() == optFun;
	}

}
