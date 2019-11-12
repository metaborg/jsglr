package org.spoofax.jsglr2.inputstack.incremental;

import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseNode;

public class LazyIncrementalInputStackTest extends AbstractIncrementalInputStackTest {

    @Override protected IIncrementalInputStack getStack(IncrementalParseNode root) {
        return new LazyIncrementalInputStack(root);
    }

}
