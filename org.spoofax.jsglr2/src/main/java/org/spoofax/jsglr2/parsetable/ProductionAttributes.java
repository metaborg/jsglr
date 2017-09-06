package org.spoofax.jsglr2.parsetable;

import org.spoofax.interpreter.terms.IStrategoTerm;

public class ProductionAttributes {

	public final ProductionType type;
    
	public final IStrategoTerm term;

	public final boolean isRecover;
	public final boolean isBracket;
	public final boolean isCompletion;
	public final boolean isPlaceholderInsertion;
	public final boolean isLiteralCompletion;
	public final boolean isIgnoreLayout;
	public final boolean isNewlineEnforced;
	public final boolean isLongestMatch;
    
    ProductionAttributes(ProductionType type, IStrategoTerm term, boolean isRecover, boolean isBracket, boolean isCompletion, boolean isPlaceholderInsertion, boolean isLiteralCompletion, boolean isIgnoreIndent, boolean isNewlineEnforced, boolean isLongestMatch) {
        this.type = type;
        
        this.term = term;
        
        this.isRecover = isRecover;
        this.isBracket = isBracket;
        this.isCompletion = isCompletion;
        this.isPlaceholderInsertion = isPlaceholderInsertion;
        this.isLiteralCompletion = isLiteralCompletion;
        this.isIgnoreLayout = isIgnoreIndent;
        this.isNewlineEnforced = isNewlineEnforced;
        this.isLongestMatch = isLongestMatch;
    }
    
    public boolean isCompletionOrRecovery() {
        return isCompletion || isLiteralCompletion || isPlaceholderInsertion || isRecover;
    }
	
}
