package org.spoofax.jsglr2.parsetable.symbols;

public interface IAltSymbol extends INonTerminalSymbol {

    ISymbol first();

    ISymbol second();

}
