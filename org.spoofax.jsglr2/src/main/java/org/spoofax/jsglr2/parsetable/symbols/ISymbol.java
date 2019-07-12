package org.spoofax.jsglr2.parsetable.symbols;

public interface ISymbol {

    public SyntaxContext syntaxContext();

    public SortCardinality cardinality();

}
