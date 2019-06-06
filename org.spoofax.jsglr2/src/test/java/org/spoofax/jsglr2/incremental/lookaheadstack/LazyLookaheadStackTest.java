package org.spoofax.jsglr2.incremental.lookaheadstack;

import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseNode;

public class LazyLookaheadStackTest extends AbstractLookaheadStackTest {

    @Override protected ILookaheadStack getStack(IncrementalParseNode root) {
        return new LazyLookaheadStack(root);
    }

}
