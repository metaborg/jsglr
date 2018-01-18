package org.spoofax.jsglr2.tokenizer;

import org.metaborg.parsetable.IProduction;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parseforest.IDerivation;

public abstract class Tokenizer<ParseForest extends AbstractParseForest, ParseNode extends ParseForest, Derivation extends IDerivation<ParseForest>> {

    public void tokenize(Tokens tokens, ParseForest parseForest) {
        tokens.makeStartToken(parseForest);

        @SuppressWarnings("unchecked") ParseNode topParseNode = (ParseNode) parseForest;

        tokenizeParseNode(tokens, topParseNode);

        tokens.makeEndToken(parseForest);
    }

    protected TokenizationCover tokenizeParseNode(Tokens tokens, ParseNode parseNode) {
        TokenizationCover result = null;

        if(parseNode != null && parseNode.startPosition.offset < parseNode.endPosition.offset) {
            IProduction production = parseNodeProduction(parseNode);

            if(production.isContextFree()) {
                Iterable<Derivation> derivations = parseNodeDerivations(parseNode);

                for(Derivation derivation : derivations) {
                    if(result == null)
                        result = tokenizeDerivation(tokens, derivation, production);
                    else
                        tokenizeDerivation(tokens, derivation, production);
                }

                return result;
            } else {
                IToken token = tokens.makeToken(parseNode, production);

                parseNode.token = token;

                result = new TokenizationCover(token, token);
            }
        }

        if(result != null) {
            parseNode.firstToken = result.firstToken;
            parseNode.lastToken = result.lastToken;

            if(result.firstToken == result.lastToken)
                parseNode.token = result.firstToken;
        }

        return result;
    }

    protected TokenizationCover tokenizeDerivation(Tokens tokens, Derivation derivation, IProduction production) {
        IToken firstToken = null, lastToken = null;

        for(ParseForest parseForest : derivation.parseForests()) {
            @SuppressWarnings("unchecked") ParseNode parseNode = (ParseNode) parseForest;

            TokenizationCover tokenizationCover = tokenizeParseNode(tokens, parseNode);

            if(tokenizationCover != null) {
                if(firstToken == null && tokenizationCover.firstToken != null)
                    firstToken = tokenizationCover.firstToken;

                if(tokenizationCover.lastToken != null)
                    lastToken = tokenizationCover.lastToken;
            }
        }

        return new TokenizationCover(firstToken, lastToken);
    }

    protected abstract IProduction parseNodeProduction(ParseNode parseNode);

    protected abstract Iterable<Derivation> parseNodeDerivations(ParseNode parseNode);

}
