package org.spoofax.jsglr2.measure.parsing;

import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.hybrid.HybridStackNode;

class StandardParserMeasureObserver
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>>
//@formatter:on
    extends
    ParserMeasureObserver<ParseForest, Derivation, ParseNode, HybridStackNode<ParseForest>, AbstractParseState<?, HybridStackNode<ParseForest>>> {

    @Override int stackNodeLinkCount(HybridStackNode<ParseForest> stackNode) {
        int linksOutCount = 0;

        for(StackLink<?, ?> link : stackNode.getLinks())
            linksOutCount++;

        return linksOutCount;
    }

}
