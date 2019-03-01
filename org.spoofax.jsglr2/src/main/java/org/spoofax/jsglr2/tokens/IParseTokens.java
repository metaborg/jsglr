package org.spoofax.jsglr2.tokens;

import org.metaborg.parsetable.IProduction;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr.client.imploder.ITokens;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;

public interface IParseTokens extends ITokens {

    IToken startToken();

    IToken endToken();

    void makeStartToken(AbstractParseForest parseForest);

    void makeEndToken(AbstractParseForest parseForest);

    IToken makeToken(AbstractParseForest parseForest, IProduction production);

}
