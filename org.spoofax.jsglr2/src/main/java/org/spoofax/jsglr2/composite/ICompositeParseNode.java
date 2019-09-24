package org.spoofax.jsglr2.composite;

import org.spoofax.jsglr2.datadependent.IDataDependentParseNode;
import org.spoofax.jsglr2.layoutsensitive.ILayoutSensitiveParseNode;
import org.spoofax.jsglr2.parseforest.basic.IBasicParseNode;

public interface ICompositeParseNode
//@formatter:off
   <ParseForest extends ICompositeParseForest,
    Derivation  extends ICompositeDerivation<ParseForest>>
    extends
        IBasicParseNode<ParseForest, Derivation>,
        IDataDependentParseNode<ParseForest, Derivation>,
        ILayoutSensitiveParseNode<ParseForest, Derivation>,
        ICompositeParseForest
//@formatter:on
{
}
