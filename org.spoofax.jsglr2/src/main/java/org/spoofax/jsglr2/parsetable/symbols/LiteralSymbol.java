package org.spoofax.jsglr2.parsetable.symbols;

public class LiteralSymbol extends NonTerminalSymbol implements ILiteralSymbol {

    String literal;

    public LiteralSymbol(SyntaxContext syntaxContext, SortCardinality cardinality, String literal) {
        super(syntaxContext, cardinality);
        this.literal = literal;
    }

    @Override public String literal() {
        return literal;
    }

    @Override public String descriptor() {
        return "\"" + literal + "\"";
    }

}
