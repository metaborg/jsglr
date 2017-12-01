package org.spoofax.jsglr2.parsetable;

public interface IProduction {

    int id();

    /*
     * It would make sense to have `ProductionType productionType()` in the interface here. However, SDF parse tables
     * represent production types in two ways: (1) on productions and (2) on reduce actions. They are not always equal
     * for the same productions due to a bug in the parser generator. Since the production types on reduce actions are
     * correct, the (sometimes incorrect) types on the productions itself are hidden here.
     */
    // public ProductionType productionType()

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

}
