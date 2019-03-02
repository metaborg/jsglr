package org.spoofax.jsglr2.tokens;

import org.metaborg.parsetable.IProduction;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr.client.imploder.ITokens;
import org.spoofax.jsglr2.parser.Position;

public interface IParseTokens extends ITokens {

    IToken startToken();

    IToken endToken();

    void makeStartToken();

    void makeEndToken(Position endPosition);

    IToken makeToken(Position startPosition, Position endPosition, IProduction production);

}
