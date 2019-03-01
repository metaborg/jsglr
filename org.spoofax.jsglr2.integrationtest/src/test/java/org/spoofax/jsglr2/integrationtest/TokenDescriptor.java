package org.spoofax.jsglr2.integrationtest;

import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr2.parser.AbstractParse;

public final class TokenDescriptor {

    public final String token;
    public final int kind;
    public final int offset, line, column;

    public TokenDescriptor(String token, int kind, int offset, int line, int column) {
        this.token = token;
        this.kind = kind;
        this.offset = offset;
        this.line = line;
        this.column = column;
    }

    public static TokenDescriptor from(AbstractParse<?, ?> parse, IToken token) {
        String inputPart;

        if(token.getStartOffset() >= 0 && token.getEndOffset() >= 0)
            inputPart = parse.getPart(token.getStartOffset(), token.getEndOffset() + 1);
        else
            inputPart = "";

        return new TokenDescriptor(inputPart, token.getKind(), token.getStartOffset(), token.getLine(),
            token.getColumn());
    }

    @Override public boolean equals(Object obj) {
        if(obj == null || obj.getClass() != this.getClass())
            return false;

        TokenDescriptor other = (TokenDescriptor) obj;

        return token.equals(other.token) && kind == other.kind && offset == other.offset && line == other.line
            && column == other.column;
    }

    @Override public String toString() {
        return "<'" + token.replace("\n", "\\n") + "';" + kind + ";" + offset + ";" + line + "," + column + ">";
    }

}
