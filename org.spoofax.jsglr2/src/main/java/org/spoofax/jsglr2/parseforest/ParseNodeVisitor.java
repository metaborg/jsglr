package org.spoofax.jsglr2.parseforest;

import org.spoofax.jsglr2.parser.Position;

public interface ParseNodeVisitor
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>>
//@formatter:on
{

    default boolean visitAmbiguousDerivations() {
        return true;
    }

    default boolean preVisit(ParseNode parseNode, Position startPosition) {
        return true;
    }

    void postVisit(ParseNode parseNode, Position startPosition, Position endPosition);

}
