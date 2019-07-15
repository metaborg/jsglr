package org.spoofax.jsglr2.parsetable.symbols;

public interface ILiteralSymbol extends INonTerminalSymbol {

    public String literal();

    default ConcreteSyntaxContext concreteSyntaxContext() {
        return ConcreteSyntaxContext.Literal;
    }

}
