package org.spoofax.jsglr2.parsetable.symbols;

public interface ILayoutSymbol extends INonTerminalSymbol {

    default ConcreteSyntaxContext concreteSyntaxContext() {
        return ConcreteSyntaxContext.Layout;
    }

}
