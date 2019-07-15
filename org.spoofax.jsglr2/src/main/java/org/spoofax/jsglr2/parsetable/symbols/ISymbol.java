package org.spoofax.jsglr2.parsetable.symbols;

public interface ISymbol {

    public SyntaxContext syntaxContext();

    public ConcreteSyntaxContext concreteSyntaxContext();

    public SortCardinality cardinality();

    public String descriptor();

}
