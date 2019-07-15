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

    @Override public boolean isOperator() {
        for(int i = 0; i < literal.length(); i++) {
            char c = literal.charAt(i);

            if(Character.isLetter(c))
                return false;
        }

        return true;
    }

    @Override public String descriptor() {
        return "\"" + literal + "\"";
    }

}
