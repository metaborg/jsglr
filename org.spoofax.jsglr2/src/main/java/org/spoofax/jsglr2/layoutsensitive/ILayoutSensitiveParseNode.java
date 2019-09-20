package org.spoofax.jsglr2.layoutsensitive;

import org.spoofax.jsglr2.parseforest.basic.IBasicParseNode;
import org.spoofax.jsglr2.parser.PositionInterval;

import java.util.List;

public interface ILayoutSensitiveParseNode
//@formatter:off
   <ParseForest extends ILayoutSensitiveParseForest,
    Derivation  extends ILayoutSensitiveDerivation<ParseForest>>
//@formatter:on
    extends IBasicParseNode<ParseForest, Derivation>, ILayoutSensitiveParseForest {

    void filterLongestMatchDerivations();

    List<PositionInterval> getLongestMatchPositions();

}
