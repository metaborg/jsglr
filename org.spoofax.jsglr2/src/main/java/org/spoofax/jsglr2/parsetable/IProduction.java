package org.spoofax.jsglr2.parsetable;

public interface IProduction {

    int id();

    ProductionType productionType();

    String sort();

    String startSymbolSort();

    String constructor();

    String descriptor();

    boolean isContextFree();

    boolean isLayout();

    boolean isLiteral();

    boolean isLexical();

    boolean isLexicalRhs(); // Whether the right hand side only contains character classes

    boolean isSkippableInParseForest();

    boolean isList();

    boolean isOptional();

    boolean isCompletionOrRecovery();

    // The methods below are for tokenization / syntax highlighting
    boolean isStringLiteral();

    boolean isNumberLiteral();

    boolean isOperator();

    static boolean typeMatchesReject(IProduction production) {
        return production.productionType() == ProductionType.REJECT;
    }

}
