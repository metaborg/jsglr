package org.spoofax.jsglr2.parsetable.symbols;

public class SequenceSymbol extends NonTerminalSymbol implements ISequenceSymbol {

    public SequenceSymbol(SyntaxContext syntaxContext) {
        super(syntaxContext, SortCardinality.List);
    }

    @Override public ConcreteSyntaxContext concreteSyntaxContext() {
        return null;
    }

    @Override public String descriptor() {
        return "seq";
    }
}
