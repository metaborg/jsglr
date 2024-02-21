package org.spoofax.jsglr2.integrationtest;

import static org.spoofax.terms.util.TermUtils.*;

import java.util.Objects;

import jakarta.annotation.Nullable;

import org.spoofax.interpreter.terms.IStrategoTerm;
import jsglr.shared.IToken;
import jsglr.shared.ImploderAttachment;

public final class TokenDescriptor {

    public final String token;
    public final IToken.Kind kind;
    public final int offset, line, column;
    @Nullable public String sort, cons;

    public TokenDescriptor(String token, IToken.Kind kind, int offset, int line, int column, @Nullable String sort,
        @Nullable String cons) {
        this.token = token;
        this.kind = kind;
        this.offset = offset;
        this.line = line;
        this.column = column;
        this.sort = sort;
        this.cons = cons;
    }

    public static TokenDescriptor from(String inputString, IToken token) {
        String inputPart;

        if(token.getStartOffset() >= 0 && token.getEndOffset() >= 0)
            inputPart = inputString.substring(token.getStartOffset(), token.getEndOffset() + 1);
        else
            inputPart = "";

        IStrategoTerm astNode = (IStrategoTerm) token.getAstNode();
        String sort = astNode == null ? null : ImploderAttachment.get(astNode).getSort();
        String cons =
            astNode == null ? null : isAppl(astNode) ? toAppl(astNode).getName() : isList(astNode) ? "[]" : null;
        return new TokenDescriptor(inputPart, token.getKind(), token.getStartOffset(), token.getLine(),
            token.getColumn(), sort, cons);
    }

    @Override public boolean equals(Object obj) {
        if(obj == null || obj.getClass() != this.getClass())
            return false;

        TokenDescriptor other = (TokenDescriptor) obj;

        return token.equals(other.token) && kind == other.kind && offset == other.offset && line == other.line
            && column == other.column && Objects.equals(sort, other.sort) && Objects.equals(cons, other.cons);
    }

    @Override public String toString() {
        return "<'" + token.replace("\n", "\\n") + "';" + kind + ";" + offset + ";" + line + "," + column + ";" + sort
            + "." + cons + ">";
    }

}
