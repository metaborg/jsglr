package org.spoofax.jsglr.client.imploder;

import static org.spoofax.jsglr.shared.Tools.applAt;
import static org.spoofax.jsglr.shared.Tools.asAppl;
import static org.spoofax.jsglr.shared.Tools.asJavaString;
import static org.spoofax.jsglr.shared.Tools.isAppl;
import static org.spoofax.jsglr.shared.Tools.termAt;

import org.spoofax.jsglr.shared.terms.AFun;
import org.spoofax.jsglr.shared.terms.ATerm;
import org.spoofax.jsglr.shared.terms.ATermAppl;
import org.spoofax.jsglr.shared.terms.ATermFactory;
import org.spoofax.jsglr.shared.terms.ATermList;


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
	
	protected final AFun sortFun;
	
	protected final AFun parameterizedSortFun;
	
	protected final AFun attrsFun;
	
	protected final AFun noAttrsFun;
	
	protected final AFun preferFun;
	
	protected final AFun avoidFun;
	
	private final AFun varSymFun;
	
	private final AFun altFun;
	
	private final AFun charClassFun;
	
	private final AFun litFun;
	
	private final AFun cilitFun;
	
	private final AFun lexFun;
	
	private final AFun optFun;
	
	private final AFun layoutFun;
	
	private final AFun cfFun;
	
	private final AFun varsymFun;
	
	private final AFun seqFun;
	
	private final AFun iterFun;
	
	private final AFun iterStarFun;
	
	private final AFun iterPlusFun;
	
	private final AFun iterSepFun;
	
	private final AFun iterStarSepFun;
	
	private final AFun iterPlusSepFun;
	
	public ProductionAttributeReader(ATermFactory factory) {
		sortFun = factory.makeAFun("sort", 1, false);
		parameterizedSortFun =
			factory.makeAFun("parameterized-sort", 2, false);
		attrsFun = factory.makeAFun("attrs", 1, false);
		noAttrsFun = factory.makeAFun("no-attrs", 0, false);
		preferFun = factory.makeAFun("prefer", 0, false);
		avoidFun = factory.makeAFun("avoid", 0, false);
		varSymFun = factory.makeAFun("varsym", 1, false);
		altFun = factory.makeAFun("alt", 2, false);
		charClassFun = factory.makeAFun("char-class", 1, false);
		litFun = factory.makeAFun("lit", 1, false);
		cilitFun = factory.makeAFun("cilit", 1, false);
		lexFun = factory.makeAFun("lex", 1, false);
		optFun = factory.makeAFun("opt", 1, false);
		layoutFun = factory.makeAFun("layout", 0, false);
		cfFun = factory.makeAFun("cf", 1, false);
		varsymFun = factory.makeAFun("varsym", 1, false);
		seqFun = factory.makeAFun("seq", 2, false);
		iterFun = factory.makeAFun("iter", 1, false);
		iterStarFun = factory.makeAFun("iter-star", 1, false);
		iterPlusFun = factory.makeAFun("iter-plus", 1, false);
		iterSepFun = factory.makeAFun("iter-sep", 2, false);
		iterStarSepFun = factory.makeAFun("iter-star-sep", 2, false);
		iterPlusSepFun = factory.makeAFun("iter-plus-sep", 2, false);
	}

	public String getConsAttribute(ATermAppl attrs) {
		ATerm consAttr = getAttribute(attrs, "cons");
		return consAttr == null ? null : ((ATermAppl) consAttr).getName();
	}
	
	// FIXME: support meta-var constructors
	public String getMetaVarConstructor(ATermAppl rhs) {
		if (rhs.getChildCount() == 1 && varSymFun == rhs.getAFun()) {
			return isIterFun(((ATermAppl) rhs.getChildAt(0)).getAFun())
					? "meta-listvar"
					: "meta-var";
		}
		return null;
	}
	
	public ATerm getAstAttribute(ATermAppl attrs) {
		return getAttribute(attrs, "ast");
	}
	
	public boolean isIndentPaddingLexical(ATermAppl attrs) {
		return getAttribute(attrs, "indentpadding") != null;
	}

	/** Return the contents of a term attribute (e.g., "cons"), or null if not found. */
	public ATerm getAttribute(ATermAppl attrs, String attrName) {
		if (attrs.getAFun() == noAttrsFun)
			return null;
		
		ATermList list = termAt(attrs, 0);
		
		for (ATerm attr : list) {			
			if (attr instanceof ATermAppl) {
				ATermAppl namedAttr = (ATermAppl) attr;
				if (namedAttr.getName().equals("term")) {
					namedAttr = termAt(namedAttr, 0);
					
					if (namedAttr.getName().equals(attrName))
						return namedAttr.getChildCount() == 1 ? termAt(namedAttr, 0) : namedAttr;
				}				
			}
		}
		
		return null; // no cons found
	}
	
	/** 
	 * Get the RTG sort name of a production RHS, or for lists, the RTG element sort name.
	 */
    public String getSort(ATermAppl rhs) {
    	for (ATerm current = rhs; current.getChildCount() > 0 && isAppl(current); current = termAt(current, 0)) {
    		AFun cons = asAppl(current).getAFun();
			if (cons == sortFun)
    			return asJavaString(termAt(current, 0));
    		if (cons == parameterizedSortFun)
    			return getParameterizedSortName(current);
    		if (cons == charClassFun)
    			return null;
    		if (cons == altFun)
    			return getAltSortName(current);
    	}
    	
    	return null;
    }
    
    private String getParameterizedSortName(ATerm node) {
    	StringBuilder result = new StringBuilder();
    	
    	result.append(applAt(node, PARAMETRIZED_SORT_NAME).getName());
    	result.append('_');
    	
		ATermList args = termAt(node, PARAMETRIZED_SORT_ARGS);
		
        for (ATermAppl arg = (ATermAppl) args.getFirst(); !args.getNext().isEmpty(); args = args.getNext()) {
			result.append(arg.getName());
		}
		
		return result.toString();
    }
    
    private String getAltSortName(ATerm node) {
		String left = getSort(applAt(node, ALT_SORT_LEFT));
		String right = getSort(applAt(node, ALT_SORT_RIGHT));
		
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
	public boolean isNonContextFree(ATermAppl rhs) {
		return (lexFun == rhs.getAFun() || isLiteral(rhs)
		    || isLayout(rhs)) || isVariableNode(rhs);
	}
	
	public boolean isLexical(ATermAppl rhs) {
		return lexFun == rhs.getAFun();
	}

	public boolean isLayout(ATermAppl rhs) {
		ATerm details = termAt(rhs, 0);
		if (!isAppl(details))
			return false;
		
		if (optFun == asAppl(details).getAFun())
			details = applAt(details, 0);
		
		return layoutFun == asAppl(details).getAFun();
	}

	public boolean isLiteral(ATermAppl rhs) {
		AFun fun = rhs.getAFun();
		return litFun == fun || cilitFun == fun;
	}
	
	public boolean isList(ATermAppl rhs) {
		ATermAppl details = cfFun == rhs.getAFun()
		                  ? applAt(rhs, 0)
		                  : rhs;
		              	
	  	if (details.getAFun() == optFun)
	  		details = applAt(details, 0);
	  	
		AFun fun = details.getAFun();
		
		 // FIXME: Spoofax/159: AsfixImploder creates tuples instead of lists for seqs
		return isIterFun(fun) || seqFun == fun;
	}

	public boolean isIterFun(AFun fun) {
		return iterFun == fun || iterStarFun == fun || iterPlusFun == fun
				|| iterSepFun == fun || iterStarSepFun == fun || iterPlusSepFun == fun;
	}

	/**
	 * Identifies parse tree nodes that begin variables.
	 * 
	 * @see #isVariableNode(ATermAppl) 
	 * @return true if the current node is lexical.
	 */
	public boolean isVariableNode(ATermAppl rhs) {
		return varsymFun == rhs.getAFun();
	}

	public boolean isLexLayout(ATermAppl rhs) {
		if (rhs.getChildCount() != 1) return false;
		ATerm child = rhs.getChildAt(0);
		return isAppl(child) && layoutFun == ((ATermAppl) child).getAFun()
			&& lexFun == rhs.getAFun();
	}

	public boolean isOptional(ATermAppl rhs) {
		if (rhs.getAFun() == optFun)
			return true;
		rhs = termAt(rhs, 0);
		if (rhs.getChildCount() == 1 && rhs.getAFun() == optFun)
			return true;
		return false;
	}

}
