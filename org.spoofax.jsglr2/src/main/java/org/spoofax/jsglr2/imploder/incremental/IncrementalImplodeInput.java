package org.spoofax.jsglr2.imploder.incremental;

import java.util.WeakHashMap;

import org.spoofax.jsglr2.imploder.TreeImploder;
import org.spoofax.jsglr2.imploder.input.ImplodeInput;
import org.spoofax.jsglr2.parseforest.IParseNode;

class IncrementalImplodeInput<ParseNode extends IParseNode, Tree> extends ImplodeInput {

    final WeakHashMap<ParseNode, TreeImploder.SubTree<Tree>> resultCache;

    IncrementalImplodeInput(String inputString, WeakHashMap<ParseNode, TreeImploder.SubTree<Tree>> resultCache) {
        super(inputString);
        this.resultCache = resultCache;
    }

}
