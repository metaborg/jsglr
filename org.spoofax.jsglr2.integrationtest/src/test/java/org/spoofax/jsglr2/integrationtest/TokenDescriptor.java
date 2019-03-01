package org.spoofax.jsglr2.integrationtest;

import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr2.parser.AbstractParse;

public final class TokenDescriptor {

    public final String token;
    public final int kind;
    public final int startLine, startColumn;
    public final int endLine, endColumn;

    public TokenDescriptor(String token, int kind, int startLine, int startColumn, int endLine, int endColumn) {
        this.token = token;
        this.kind = kind;
        this.startLine = startLine;
        this.startColumn = startColumn;
        this.endLine = endLine;
        this.endColumn = endColumn;
    }

    public static TokenDescriptor from(AbstractParse<?, ?> parse, IToken token) {
        String inputPart;

        if(token.getStartOffset() >= 0 && token.getEndOffset() >= 0)
            inputPart = parse.getPart(token.getStartOffset(), token.getEndOffset() + 1);
        else
            inputPart = "";

        return new TokenDescriptor(inputPart, token.getKind(), token.getLine(), token.getColumn(), token.getEndLine(),
            token.getEndColumn());
    }

    @Override public boolean equals(Object obj) {
        if(obj == null || obj.getClass() != this.getClass())
            return false;

        TokenDescriptor other = (TokenDescriptor) obj;

        return kind == other.kind && startLine == other.startLine && startColumn == other.startColumn
            && endLine == other.endLine && endColumn == other.endColumn;
    }

    @Override public String toString() {
        return "<'" + token + "';" + kind + ";" + startLine + "," + startColumn + ";" + endLine + "," + endColumn + ">";
    }

}
