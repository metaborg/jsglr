package org.spoofax.jsglr2.parsetable;

import org.metaborg.parsetable.ProductionType;
import org.spoofax.interpreter.terms.IStrategoNamed;
import org.spoofax.interpreter.terms.IStrategoTerm;

public class ProductionAttributes {

    public final ProductionType type;
    public final IStrategoTerm constructorTerm;
    public final String constructor;
    public final boolean isRecover;
    public final boolean isBracket;
    public final boolean isCompletion;
    public final boolean isPlaceholderInsertion;
    public final boolean isLiteralCompletion;
    public final boolean isIgnoreLayout;
    public final boolean isNewlineEnforced;
    public final boolean isLongestMatch;
    public final boolean isCaseInsensitive;
    public final boolean isIndentPaddingLexical;
    public final boolean isFlatten;

    ProductionAttributes(ProductionType type, IStrategoTerm constructorTerm, boolean isRecover, boolean isBracket,
        boolean isCompletion, boolean isPlaceholderInsertion, boolean isLiteralCompletion, boolean isIgnoreIndent,
        boolean isNewlineEnforced, boolean isLongestMatch, boolean isCaseInsensitive, boolean isIndentPaddingLexical,
        boolean isFlatten) {
        this.type = type;
        this.constructorTerm = constructorTerm;
        this.constructor = constructorTerm == null ? null : ((IStrategoNamed) constructorTerm).getName();
        this.isRecover = isRecover;
        this.isBracket = isBracket;
        this.isCompletion = isCompletion;
        this.isPlaceholderInsertion = isPlaceholderInsertion;
        this.isLiteralCompletion = isLiteralCompletion;
        this.isIgnoreLayout = isIgnoreIndent;
        this.isNewlineEnforced = isNewlineEnforced;
        this.isLongestMatch = isLongestMatch;
        this.isCaseInsensitive = isCaseInsensitive;
        this.isIndentPaddingLexical = isIndentPaddingLexical;
        this.isFlatten = isFlatten;
    }

    public boolean isCompletionOrRecovery() {
        return isCompletion || isLiteralCompletion || isPlaceholderInsertion || isRecover;
    }

}
