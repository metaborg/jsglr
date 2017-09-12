package org.spoofax.jsglr2.parsetable;

import static org.spoofax.terms.StrategoListIterator.iterable;
import static org.spoofax.terms.Term.applAt;
import static org.spoofax.terms.Term.isTermAppl;
import static org.spoofax.terms.Term.isTermNamed;
import static org.spoofax.terms.Term.javaString;
import static org.spoofax.terms.Term.termAt;

import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoConstructor;
import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoNamed;
import org.spoofax.interpreter.terms.IStrategoTerm;

public class ProductionInfo {

	public final IStrategoAppl lhs;
	public final IStrategoList rhs;
	public final IStrategoAppl attributes;
	
	public ProductionInfo(IStrategoAppl productionTerm) {
		this.lhs = termAt(productionTerm, 1);
		this.rhs = termAt(productionTerm, 0);
		this.attributes = termAt(productionTerm, 2);
	}
	
	public String descriptor() {
		String sort = getSort();
		
		if (isList())
            return getIterConstructor().getName() + "(" + sort + ")";
        else if (sort != null) {
            String constructor = getConstructor();
            
            if (constructor == null)
                return sort;
            else
                return sort + "." + constructor + "/" + rhs.getSubtermCount();
        } else if (isLiteral()) {
			IStrategoTerm literal = getLiteral();
			
			return literal.toString();
		} else if (isLayout()) {
			return "layout";
		} else if (isLexical()) {
			return "lexical";
		} else {
			return "n.a.";
		}
	}
	
	public String getSort(IStrategoTerm term) {
		for (IStrategoTerm current = term; current.getSubtermCount() > 0 && isTermAppl(current); current = termAt(current, 0)) {
    		IStrategoAppl currentAppl = (IStrategoAppl) current;
			String sort = tryGetSort(currentAppl);
			
			if (sort != null)
				return sort;
    	}
    	
    	return null;
	}
	
	public String getSort() {
		return getSort(lhs);
	}

	public String tryGetSort(IStrategoAppl appl) {
		IStrategoConstructor cons = appl.getConstructor();
		
		if ("sort".equals(cons.getName()))
			return javaString(termAt(appl, 0));
		else if ("cf".equals(cons.getName()) || "lex".equals(cons.getName()))
			return tryGetSort(applAt(appl, 0));
		else if ("parameterized-sort".equals(cons.getName()))
			return getParameterizedSortName(appl);
		else if ("char-class".equals(cons.getName()))
			return null;
		else if ("alt".equals(cons.getName()))
			return getAltSortName(appl);
		else
			return null;
	}
	
	public String tryGetFirstSort() {
		for (IStrategoTerm rhsPart : rhs.getAllSubterms()) {
			String sort = tryGetSort((IStrategoAppl) rhsPart);
			
			if (sort != null)
				return sort;
		}
		return null;
	}
	
	private final int PARAMETRIZED_SORT_NAME = 0;
	private final int PARAMETRIZED_SORT_ARGS = 1;
    
    private String getParameterizedSortName(IStrategoAppl parameterizedSort) {
    	StringBuilder result = new StringBuilder();
    	
    	result.append(((IStrategoNamed) termAt(parameterizedSort, PARAMETRIZED_SORT_NAME)).getName());
    	result.append('_');
    	
		IStrategoList args = termAt(parameterizedSort, PARAMETRIZED_SORT_ARGS);
		
        for (IStrategoTerm arg : iterable(args)) {
			result.append(((IStrategoNamed) arg).getName());
		}
		
		return result.toString();
    }

	private final int ALT_SORT_LEFT = 0;
	private final int ALT_SORT_RIGHT = 1;
	
    private String getAltSortName(IStrategoAppl node) {
		String left = getSort(applAt(node, ALT_SORT_LEFT));
		String right = getSort(applAt(node, ALT_SORT_RIGHT));
		
		return left + "_" + right + "0";
    }

	public String getConstructor() {
		IStrategoTerm consAttr = getAttribute("cons");
		
		return consAttr == null ? null : ((IStrategoNamed) consAttr).getName();
	}
	
	public IStrategoTerm getLiteral() {
		return isLiteral() ? termAt(lhs, 0) : null;
	}
	
	public boolean isRecoverProduction(String constructor) {
		return getAttribute("recover") != null || isWaterConstructor(constructor);
	}
	
	private static final String WATER = "WATER";
	
	public boolean isWaterConstructor(String constructor) {
		return WATER.equals(constructor);
	}

	public boolean isCompletionProduction(int subtermCount) {
		return getAttribute("completion") != null && subtermCount > 0;
	}
	
	public boolean isBracketProduction() {
        return getAttribute("bracket") != null;
    }

	public boolean isRejectProduction() {
		return getAttribute("reject") != null;
	}
	
	public boolean isNonContextFree() {
		return isLexical() || isLiteral() || isLayout() || isVariableNode();
	}
	
	public boolean isContextFree() {
		return !isNonContextFree();
	}
	
	public boolean isLexical() {
	    boolean lexTerm = "lex".equals(lhs.getConstructor().getName());
	    
		return lexTerm || isLexicalRhs();
	}
	
	public boolean isLexicalRhs() {
	    if (rhs.getSubtermCount() > 0) {
	        boolean lexRhs = true;
	        
	        for (IStrategoTerm rhsPart : rhs.getAllSubterms()) {
	            String rhsPartConstructor = ((IStrategoAppl) rhsPart).getConstructor().getName();
	            
	            lexRhs &= "char-class".equals(rhsPartConstructor);
	        }
	        
	        return lexRhs;
	    } else
	        return false;
	}

	public boolean isLayout() {
		IStrategoTerm details = termAt(lhs, 0);
		
		if (!isTermAppl(details))
			return false;
		
		if ("opt".equals(((IStrategoAppl) details).getConstructor().getName()))
			details = termAt(details, 0);
		
		return "layout".equals(((IStrategoAppl) details).getConstructor().getName());
	}

	public boolean isLiteral() {
		String constructorName = lhs.getConstructor().getName();
		
		return "lit".equals(constructorName) || "cilit".equals(constructorName);
	}
	
	public boolean isList() {
		IStrategoConstructor constructor = getIterConstructor();
		
		if (isIterFun(constructor) || "seq".equals(constructor.getName()))
			return true;
		else
			return isFlatten();
	}
	
	private IStrategoConstructor getIterConstructor() {
	    IStrategoAppl details = lhs;
        
        if ("varsym".equals(details.getConstructor().getName()))
            details = termAt(details, 0);
        
        if ("cf".equals(details.getConstructor().getName()))
            details = termAt(details, 0);
        
        if ("opt".equals(details.getConstructor().getName()))
            details = termAt(details, 0);
        
        return details.getConstructor();
	}
    
    public boolean isFlatten() {
        return getAttribute("flatten") != null;
    }

	public boolean isIterFun(IStrategoConstructor constructor) {
		String constructorName = constructor.getName();
		
		return "iter".equals(constructorName) || "iter-star".equals(constructorName) || "iter-plus".equals(constructorName) || "iter-sep".equals(constructorName) || "iter-star-sep".equals(constructorName) || "iter-plus-sep".equals(constructorName);
	}
	
	public boolean isVariableNode() {
		return "varsym".equals(lhs.getConstructor().getName());
	}

	public boolean isLexLayout() {
		if (lhs.getSubtermCount() != 1)
			return false;
		
		IStrategoTerm child = lhs.getSubterm(0);
		
		return isTermAppl(child) && "layout".equals(((IStrategoAppl) child).getConstructor().getName()) && "lex".equals(lhs.getConstructor().getName());
	}

	public boolean isOptional() {
		if ("opt".equals(lhs.getConstructor().getName()))
			return true;
		
		IStrategoTerm contents = termAt(lhs, 0);
		
		return contents.getSubtermCount() == 1 && isTermAppl(contents) && "opt".equals(((IStrategoAppl) contents).getConstructor().getName());
	}

    public boolean isCaseInsensitive(IStrategoAppl attrs) {
        return getAttribute("case-insensitive") != null;
    }
	
	public IStrategoTerm getAstAttribute(IStrategoAppl attrs) {
		return getAttribute("ast");
	}
	
	public boolean isIndentPaddingLexical(IStrategoAppl attrs) {
		return getAttribute("indentpadding") != null;
	}
    
	public IStrategoTerm getAttribute(String attrName) {
		if ("no-attrs".equals(attributes.getConstructor().getName()))
			return null;
		
		IStrategoList list = termAt(attributes, 0);
		
		for (IStrategoTerm attr : iterable(list)) {			
			if (isTermNamed(attr)) {
				IStrategoNamed attrNamed = (IStrategoNamed) attr;
				
				if ("term".equals(attrNamed.getName())) {
					attrNamed = termAt(attrNamed, 0);
					
					if (attrName.equals(attrNamed.getName()))
						return attrNamed.getSubtermCount() == 1 ? termAt(attrNamed, 0) : attrNamed;
				} else if (attrName.equals(attrNamed.getName())) {
				    return attrNamed;
				}
			}
		}
		
		return null;
	}
	
}
