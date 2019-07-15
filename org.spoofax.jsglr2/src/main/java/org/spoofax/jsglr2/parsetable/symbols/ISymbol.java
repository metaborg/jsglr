package org.spoofax.jsglr2.parsetable.symbols;

public interface ISymbol {

    public SyntaxContext syntaxContext();

    public ConcreteSyntaxContext concreteSyntaxContext();

    public SortCardinality cardinality();

    public String descriptor();

    public static String getSort(ISymbol symbol) {
        return symbol instanceof ISortSymbol ? ((ISortSymbol) symbol).sort() : null;
    }

}
