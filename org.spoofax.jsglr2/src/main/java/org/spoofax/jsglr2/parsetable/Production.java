package org.spoofax.jsglr2.parsetable;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.ProductionType;

public class Production implements IProduction {

    private final int productionId;
    private final String sort;
    private final String startSymbolSort;
    private final String descriptor;
    private final boolean isContextFree;
    private final boolean isLayout;
    private final boolean isLiteral;
    private final boolean isLexical;
    private final boolean isLexicalRhs;
    private final boolean isSkippableInParseForest;
    private final boolean isList;
    private final boolean isOptional;
    private final boolean isStringLiteral;
    private final boolean isNumberLiteral;
    private final boolean isOperator;
    private final boolean isLongestMatch;
    private final ProductionAttributes attributes;

    public Production(int productionId, String sort, String startSymbolSort, String descriptor, Boolean isContextFree,
        Boolean isLayout, Boolean isLiteral, Boolean isLexical, Boolean isLexicalRhs, Boolean isSkippableInParseForest,
        Boolean isList, Boolean isOptional, Boolean isStringLiteral, Boolean isNumberLiteral, Boolean isOperator, Boolean isLongestMatch,
        ProductionAttributes attributes) {
        this.productionId = productionId;
        this.sort = sort;
        this.startSymbolSort = startSymbolSort;
        this.descriptor = descriptor;
        this.isContextFree = isContextFree;
        this.isLayout = isLayout;
        this.isLiteral = isLiteral;
        this.isLexical = isLexical;
        this.isLexicalRhs = isLexicalRhs;
        this.isSkippableInParseForest = isSkippableInParseForest;
        this.isList = isList;
        this.isOptional = isOptional;
        this.isStringLiteral = isStringLiteral;
        this.isNumberLiteral = isNumberLiteral;
        this.isOperator = isOperator;
        this.isLongestMatch = isLongestMatch;
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
        return sort;
    }

    @Override public String startSymbolSort() {
        return startSymbolSort;
    }

    @Override public String constructor() {
        return attributes.constructor;
    }

    @Override public String descriptor() {
        return descriptor;
    }

    @Override public boolean isContextFree() {
        return isContextFree;
    }

    @Override public boolean isLayout() {
        return isLayout;
    }

    @Override public boolean isLiteral() {
        return isLiteral;
    }

    @Override public boolean isLexical() {
        return isLexical;
    }

    @Override public boolean isLexicalRhs() {
        return isLexicalRhs;
    }

    @Override public boolean isSkippableInParseForest() {
        return isSkippableInParseForest;
    }

    @Override public boolean isList() {
        return isList;
    }

    @Override public boolean isOptional() {
        return isOptional;
    }

    @Override public boolean isStringLiteral() {
        return isStringLiteral;
    }

    @Override public boolean isNumberLiteral() {
        return isNumberLiteral;
    }

    @Override public boolean isOperator() {
        return isOperator;
    }

    @Override public boolean isCompletionOrRecovery() {
        return attributes.isCompletionOrRecovery();
    }

    @Override public String toString() {
        return descriptor;
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
        return isLongestMatch;
    }

}
