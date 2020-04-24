package org.spoofax.jsglr2.imploder.incremental;

import org.spoofax.jsglr2.imploder.input.ImplodeInput;
import org.spoofax.jsglr2.parseforest.IParseNode;

public class IncrementalImplodeInput<ParseNode extends IParseNode<?, ?>, Cache, Tree> extends ImplodeInput {

    final Cache resultCache;

    IncrementalImplodeInput(String inputString, Cache resultCache) {
        super(inputString);
        this.resultCache = resultCache;
    }

}
