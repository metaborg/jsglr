package org.spoofax.jsglr2.tokens;

import org.metaborg.parsetable.productions.IProduction;
import org.spoofax.jsglr2.parser.Position;

import jsglr.shared.IToken;
import jsglr.shared.ITokens;

public interface IParseTokens extends ITokens {

    IToken startToken();

    IToken endToken();

    void makeStartToken();

    void makeEndToken(Position endPosition);

    IToken makeToken(Position startPosition, Position endPosition, IProduction production);

}
