package org.spoofax.jsglr2.imploder.incremental;

import org.spoofax.jsglr2.parseforest.IParseNode;

public interface IIncrementalImplodeInputFactory<ParseNode extends IParseNode<?, ?>, Cache, Tree, Input extends IncrementalImplodeInput<ParseNode, Cache, Tree>> {

    Input get(String inputString, Cache resultCache);

}
