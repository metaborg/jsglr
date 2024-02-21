package org.spoofax.jsglr2.tokens;

import static jsglr.shared.IToken.Kind.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import jakarta.annotation.Nonnull;
import jsglr.shared.FilteredTokenIterator;
import jsglr.shared.IToken;
import jsglr.shared.Token;

import org.metaborg.parsetable.productions.IProduction;
import org.spoofax.jsglr2.parser.Position;

public class Tokens implements IParseTokens {

    private static final long serialVersionUID = 2054391299757162697L;

    private final String fileName;
    private final String input;

    private IToken startToken, endToken;

    private final ArrayList<IToken> tokens;

    public Tokens(String input, String fileName) {
        this.input = input;
        this.fileName = fileName;

        this.tokens = new ArrayList<>();
    }

    public IToken startToken() {
        return startToken;
    }

    public IToken endToken() {
        return endToken;
    }

    public void makeStartToken() {
        startToken = new Token(this, fileName, 0, 1, 1, 0, -1, TK_RESERVED);

        tokens.add(startToken);
    }

    public void makeEndToken(Position endPosition) {
        endToken = new Token(this, fileName, tokens.size(), endPosition.line, endPosition.column, endPosition.offset,
            -1, TK_EOF);

        tokens.add(endToken);
    }

    public IToken makeToken(Position startPosition, Position endPosition, IProduction production) {
        IToken token = new Token(this, fileName, tokens.size(), startPosition.line, startPosition.column,
            startPosition.offset, endPosition.offset - 1,
            startPosition.equals(endPosition) ? TK_NO_TOKEN_KIND : IProduction.getTokenKind(production));

        tokens.add(token);

        return token;
    }

    @Override @Nonnull public Iterator<IToken> iterator() {
        return new FilteredTokenIterator(allTokens());
    }

    @Override @Nonnull public Iterable<IToken> allTokens() {
        return Collections.unmodifiableList(tokens);
    }

    @Override public String getInput() {
        return input;
    }

    @Override public int getTokenCount() {
        return tokens.size();
    }

    @Override public IToken getTokenAtOffset(int offset) {
        for(IToken token : tokens) {
            if(token.getStartOffset() == offset)
                return token;
        }

        return null;
    }

    @Override public String getFilename() {
        return fileName;
    }

    @Override public String toString(IToken left, IToken right) {
        int startOffset = left.getStartOffset();
        int endOffset = right.getEndOffset();

        if(startOffset >= 0 && endOffset >= 0)
            return toString(startOffset, endOffset + 1);
        else
            return "";
    }

    @Override public String toString(int startOffset, int endOffset) {
        return input.substring(startOffset, endOffset);
    }

    @Override public String toString() {
        return tokens.toString();
    }

}
