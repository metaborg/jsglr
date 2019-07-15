package org.spoofax.jsglr2.parsetable.symbols;

public enum SyntaxContext {

    ContextFree(ConcreteSyntaxContext.ContextFree), Lexical(ConcreteSyntaxContext.Lexical);

    public final ConcreteSyntaxContext concreteSyntaxContext;

    SyntaxContext(ConcreteSyntaxContext concreteSyntaxContext) {
        this.concreteSyntaxContext = concreteSyntaxContext;
    }

}
