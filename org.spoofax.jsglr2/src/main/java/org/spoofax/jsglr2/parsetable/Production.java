package org.spoofax.jsglr2.parsetable;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.ProductionType;
import org.spoofax.jsglr2.parsetable.symbols.ConcreteSyntaxContext;
import org.spoofax.jsglr2.parsetable.symbols.ILiteralSymbol;
import org.spoofax.jsglr2.parsetable.symbols.ISymbol;
import org.spoofax.jsglr2.parsetable.symbols.SortCardinality;

public class Production implements IProduction {

    public final ISymbol lhs;
    public final ISymbol[] rhs;

    private final int productionId;
    private final boolean isStringLiteral;
    private final boolean isNumberLiteral;
    private final boolean isLexicalRhs;
    private final boolean isSkippableInParseForest;
    private final ProductionAttributes attributes;

    public Production(int productionId, ISymbol lhs, ISymbol[] rhs, Boolean isStringLiteral, Boolean isNumberLiteral,
        Boolean isLexicalRhs, Boolean isSkippableInParseForest, ProductionAttributes attributes) {
        this.productionId = productionId;
        this.lhs = lhs;
        this.rhs = rhs;
        this.isStringLiteral = isStringLiteral;
        this.isNumberLiteral = isNumberLiteral;
        this.isLexicalRhs = isLexicalRhs;
        this.isSkippableInParseForest = isSkippableInParseForest;
        this.attributes = attributes;
    }

    @Override public int id() {
        return productionId;
    }

    public static ProductionType typeFromInt(int productionType) {
        switch(productionType) {
            /*
             * During parsing we don't need the types 3 (BRACKET), 5 (LEFT_ASSOCIATIVE) and 6 (RIGHT_ASSOCIATIVE) and
             * thus ignore them here.
             */
            case 1:
                return ProductionType.REJECT;
            case 2:
                return ProductionType.PREFER;
            case 4:
                return ProductionType.AVOID;
            default:
                return ProductionType.NO_TYPE;
        }
    }

    @Override public String sort() {
        return ISymbol.getSort(lhs);
    }

    @Override public String startSymbolSort() {
        if("<START>".equals(sort())) {
            for(ISymbol symbol : rhs) {
                String sort = ISymbol.getSort(symbol);

                if(sort != null)
                    return sort;
            }
        }

        return null;
    }

    @Override public String constructor() {
        return attributes.constructor;
    }

    @Override public String descriptor() {
        return lhs.toString() + " = " + Arrays.stream(rhs).map(ISymbol::toString).collect(Collectors.joining(" "));
    }

    @Override public boolean isContextFree() {
        return lhs.concreteSyntaxContext() == ConcreteSyntaxContext.ContextFree;
    }

    @Override public boolean isLayout() {
        return lhs.concreteSyntaxContext() == ConcreteSyntaxContext.Layout;
    }

    @Override public boolean isLiteral() {
        return lhs.concreteSyntaxContext() == ConcreteSyntaxContext.Literal;
    }

    @Override public boolean isLexical() {
        return lhs.concreteSyntaxContext() == ConcreteSyntaxContext.Lexical || (isLexicalRhs && !isLiteral());
    }

    @Override public boolean isSkippableInParseForest() {
        return isSkippableInParseForest;
    }

    @Override public boolean isList() {
        return lhs.cardinality() == SortCardinality.List || attributes.isFlatten;
    }

    @Override public boolean isOptional() {
        return lhs.cardinality() == SortCardinality.Optional;
    }

    @Override public boolean isStringLiteral() {
        return isStringLiteral;
    }

    @Override public boolean isNumberLiteral() {
        return isNumberLiteral;
    }

    @Override public boolean isOperator() {
        return isLiteral() && ((ILiteralSymbol) lhs).isOperator();
    }

    @Override public boolean isCompletionOrRecovery() {
        return attributes.isCompletionOrRecovery();
    }

    @Override public String toString() {
        return descriptor();
    }

    @Override public int hashCode() {
        return productionId;
    }

    @Override public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }

        Production that = (Production) o;

        return productionId == that.productionId;
    }

    @Override public boolean isIgnoreLayoutConstraint() {
        return attributes.isIgnoreLayout;
    }

    @Override public boolean isLongestMatch() {
        return attributes.isLongestMatch;
    }

}
