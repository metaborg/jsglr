package org.spoofax.jsglr2.parseforest;

import org.spoofax.jsglr2.parser.Position;

public interface ParseForestVisitor
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>>
//@formatter:on
{

    default void visitDerivation(Derivation derivation, Position startPosition, Position endPosition) {
    }

    default void visitParseNode(ParseNode parseNode, Position startPosition, Position endPosition) {
    }

}
