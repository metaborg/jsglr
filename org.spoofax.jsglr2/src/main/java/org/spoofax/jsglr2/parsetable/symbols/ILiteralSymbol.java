package org.spoofax.jsglr2.parsetable.symbols;

public interface ILiteralSymbol extends INonTerminalSymbol {

    public String literal();

    default SortConcreteSyntaxContext concreteSyntaxContext() {
        return SortConcreteSyntaxContext.Literal;
    }

}
