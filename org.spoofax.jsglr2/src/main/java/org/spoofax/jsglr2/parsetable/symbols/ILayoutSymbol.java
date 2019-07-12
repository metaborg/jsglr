package org.spoofax.jsglr2.parsetable.symbols;

public interface ILayoutSymbol extends INonTerminalSymbol {

    default SortConcreteSyntaxContext concreteSyntaxContext() {
        return SortConcreteSyntaxContext.Layout;
    }

}
