package org.spoofax.jsglr2.tokenizer;

import static org.spoofax.jsglr.client.imploder.IToken.TK_EOF;
import static org.spoofax.jsglr.client.imploder.IToken.TK_RESERVED;

import java.util.ArrayList;
import java.util.Iterator;

import org.metaborg.parsetable.IProduction;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr.client.imploder.ITokens;
import org.spoofax.jsglr.client.imploder.Token;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;

public class Tokens implements ITokens {

    private final String filename;
    private final String input;

    public IToken startToken, endToken;

    private final ArrayList<IToken> tokens;

    public Tokens(String input, String filename) {
        this.input = input;
        this.filename = filename;

        this.tokens = new ArrayList<IToken>();
    }

    public void makeStartToken(AbstractParseForest parseForest) {
        startToken = new Token(this, filename, 0, parseForest.startPosition.line, parseForest.startPosition.column,
            parseForest.startPosition.offset, -1, TK_RESERVED);

        addToken(startToken);
    }

    public void makeEndToken(AbstractParseForest parseForest) {
        endToken = new Token(this, filename, tokens.size(), parseForest.endPosition.line,
            parseForest.endPosition.column, parseForest.endPosition.offset, -1, TK_EOF);

        addToken(endToken);
    }

    public IToken makeToken(AbstractParseForest parseForest, IProduction production) {
        int tokenKind;

        if(production.isLayout()) {
            tokenKind = IToken.TK_LAYOUT;
        } else if(production.isStringLiteral()) {
            tokenKind = IToken.TK_STRING;
        } else if(production.isNumberLiteral()) {
            tokenKind = IToken.TK_NUMBER;
        } else if(production.isOperator()) {
            tokenKind = IToken.TK_OPERATOR;
        } else if(production.isLexical()) {
            tokenKind = IToken.TK_IDENTIFIER;
        } else {
            tokenKind = IToken.TK_KEYWORD;
        }

        IToken endToken =
            new Token(this, filename, tokens.size(), parseForest.startPosition.line, parseForest.startPosition.column,
                parseForest.startPosition.offset, parseForest.endPosition.offset - 1, tokenKind);

        addToken(endToken);

        return endToken;
    }

    public int addToken(IToken token) {
        int index = tokens.size();

        tokens.add(token);

        return index;
    }

    @Override
    public Iterator<IToken> iterator() {
        @SuppressWarnings("unchecked") Iterator<IToken> result = (Iterator<IToken>) (Iterator<?>) tokens.iterator();
        return result;
    }

    @Override
    public String getInput() {
        return input;
    }

    @Override
    public int getTokenCount() {
        return tokens.size();
    }

    @Override
    public IToken getTokenAt(int index) {
        return tokens.get(index);
    }

    @Override
    public IToken getTokenAtOffset(int offset) {
        for(IToken token : tokens) {
            if(token.getStartOffset() == offset)
                return token;
        }

        return null;
    }

    @Override
    public String getFilename() {
        return filename;
    }

    @Override
    public String toString(IToken left, IToken right) {
        int startOffset = left.getStartOffset();
        int endOffset = right.getEndOffset();

        if(startOffset >= 0 && endOffset >= 0)
            return toString(startOffset, endOffset);
        else
            return "";
    }

    @Override
    public String toString(int startOffset, int endOffset) {
        return input.substring(startOffset, endOffset + 1);
    }

    @Override
    public boolean isAmbigous() {
        return false; // TODO: implement
    }

    @Override
    public String toString() {
        return tokens.toString();
    }

}
