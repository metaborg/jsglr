package org.spoofax.jsglr2.parsetable.symbols;

public class SequenceSymbol extends NonTerminalSymbol implements ISequenceSymbol {

    public SequenceSymbol(SyntaxContext syntaxContext) {
        super(syntaxContext, SortCardinality.List);
    }

    @Override public SortConcreteSyntaxContext concreteSyntaxContext() {
        return null;
    }
}
