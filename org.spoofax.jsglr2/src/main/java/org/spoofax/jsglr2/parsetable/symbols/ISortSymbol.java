package org.spoofax.jsglr2.parsetable.symbols;

public interface ISortSymbol extends INonTerminalSymbol {

    public String sort();

    default SortConcreteSyntaxContext concreteSyntaxContext() {
        return null;
    }

}
