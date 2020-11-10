package org.spoofax.jsglr2.layoutsensitive;

import org.spoofax.jsglr2.parseforest.basic.IBasicParseNode;

public interface ILayoutSensitiveParseNode
//@formatter:off
   <ParseForest extends ILayoutSensitiveParseForest,
    Derivation  extends ILayoutSensitiveDerivation<ParseForest>>
//@formatter:on
    extends IBasicParseNode<ParseForest, Derivation>, ILayoutSensitiveParseForest {
}
