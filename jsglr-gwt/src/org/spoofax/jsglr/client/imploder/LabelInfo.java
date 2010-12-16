package org.spoofax.jsglr.client.imploder;

import static org.spoofax.jsglr.shared.Tools.termAt;

import org.spoofax.jsglr.shared.terms.ATerm;
import org.spoofax.jsglr.shared.terms.ATermAppl;
import org.spoofax.jsglr.shared.terms.ATermList;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class LabelInfo {
	
	private final ProductionAttributeReader reader;

	private final ATermAppl production;
	
	private final String sort;
	
	private final String constructor;
	
	private final ATerm astAttribute;
	
	private final boolean isLexicalLiteralOrLayout;
	
	private final boolean isLexical;

	private final boolean isVar;

	private final boolean isList;

	private final boolean isIndentPaddingLexical;
	
	private final boolean isLexLayout;
	
	private final boolean isSortProduction;

	private final boolean isLayout;
	
	private final boolean isLiteral;
	
	private final String metaVarConstructor;
	
	public LabelInfo(ProductionAttributeReader reader, ATermAppl production) {
		this.production = production;
		this.reader = reader;
		ATermAppl rhs = getRHS();
		ATermAppl attrs = getAttrs();
		sort = reader.getSort(rhs);
		constructor = reader.getConsAttribute(attrs);
		astAttribute = reader.getAstAttribute(attrs);
		isLexicalLiteralOrLayout = reader.isLexicalLiteralOrLayout(rhs);
		isList = reader.isList(rhs);
		isVar = reader.isVariableNode(rhs);
		isIndentPaddingLexical = reader.isIndentPaddingLexical(attrs);
		isLexLayout = reader.isLexLayout(rhs);
		isLexical = reader.isLexical(rhs);
		isLayout = reader.isLayout(rhs);
		isLiteral = reader.isLiteral(rhs);
		isSortProduction = reader.sortFun == rhs.getAFun() || reader.parameterizedSortFun == rhs.getAFun();
		metaVarConstructor = reader.getMetaVarConstructor(rhs);
	}
    
	protected ATermList getLHS() {
    	return termAt(production, 0);
    }
    
	protected ATermAppl getRHS() {
    	return termAt(production, 1);
    }
    
	protected ATermAppl getAttrs() {
    	return termAt(production, 2);
    }
	
	public String getSort() {
		return sort;
	}
	
	public String getConstructor() {
		return constructor;
	}
	
	public ATerm getAstAttribute() {
		return astAttribute;
	}
	
	public boolean isLexicalLiteralOrLayout() {
		return isLexicalLiteralOrLayout;
	}
	
	public boolean isLexical() {
		return isLexical;
	}
	
	public boolean isList() {
		return isList;
	}
	
	public boolean isVar() {
		return isVar;
	}
	
	public boolean isIndentPaddingLexical() {
		return isIndentPaddingLexical;
	}
	
	public boolean isLexLayout() {
		return isLexLayout;
	}
	
	public boolean isLayout() {
		return isLayout;
	}
	
	public boolean isLiteral() {
		return isLiteral;
	}
	
	public boolean isSortProduction() {
		return isSortProduction;
	}
	
	public String getMetaVarConstructor() {
		return metaVarConstructor;
	}

	public boolean isOptional() {
		return reader.isOptional(getRHS());
	}
	
	@Override
	public String toString() {
		return production.toString();
	}
}
