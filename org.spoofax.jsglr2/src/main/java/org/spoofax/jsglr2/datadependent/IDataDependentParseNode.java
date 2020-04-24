package org.spoofax.jsglr2.datadependent;

import org.spoofax.jsglr2.parseforest.basic.IBasicParseNode;

public interface IDataDependentParseNode
//@formatter:off
   <ParseForest extends IDataDependentParseForest,
    Derivation  extends IDataDependentDerivation<ParseForest>>
//@formatter:on
    extends IBasicParseNode<ParseForest, Derivation>, IDataDependentParseForest {
}
