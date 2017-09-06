package org.spoofax.jsglr2.parsetable;

import static org.spoofax.terms.Term.termAt;

import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoString;

public class Production implements IProduction {

	private final int productionNumber;
	private final ProductionAttributes productionAttributes;
	private final ProductionInfo productionInfo;
	
	public Production(int productionNumber, IStrategoAppl productionTerm, ProductionAttributes productionAttributes) {
		this.productionNumber = productionNumber;
		this.productionAttributes = productionAttributes;
		this.productionInfo = new ProductionInfo(productionTerm);
	}
	
	public int productionNumber() {
	    return productionNumber;
	}
	
	public static ProductionType typeFromInt(int productionType) {
		switch (productionType) {
		case 1:		return ProductionType.REJECT;
		case 2:		return ProductionType.PREFER;
		case 3:		return ProductionType.BRACKET;
		case 4:		return ProductionType.AVOID;
		case 5:		return ProductionType.LEFT_ASSOCIATIVE;
		case 6:		return ProductionType.RIGHT_ASSOCIATIVE;
		default:	return ProductionType.NO_TYPE; 
		}
	}
    
    public String sort() {
        return productionInfo.getSort();
    }
    
    public String constructor() {
        return productionInfo.getConstructor();
    }
    
    public String descriptor() {
        return productionInfo.descriptor();
    }
    
    public boolean isContextFree() {
        return productionInfo.isContextFree();
    }
    
    public boolean isLayout() {
        return productionInfo.isLayout();
    }
    
    public boolean isLiteral() {
        return productionInfo.isLiteral();
    }
    
    public boolean isLexical() {
        return productionInfo.isLexical();
    }
    
    public boolean isList() {
        return productionInfo.isList();
    }
    
    public boolean isOptional() {
        return productionInfo.isOptional();
    }
    
    public boolean isCompletionOrRecovery() {
        return productionAttributes.isCompletionOrRecovery();
    }
    
    public boolean isOperator() {
        if (!productionInfo.isLiteral()) return false;
        
        IStrategoString lit = termAt(productionInfo.lhs, 0);
        String contents = lit.stringValue();
        
        for (int i = 0; i < contents.length(); i++) {
            char c = contents.charAt(i);
            if (Character.isLetter(c)) return false;
        }
        
        return true;
    }
	
}
