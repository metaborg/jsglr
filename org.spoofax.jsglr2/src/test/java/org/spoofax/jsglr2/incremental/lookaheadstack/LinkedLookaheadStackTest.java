package org.spoofax.jsglr2.incremental.lookaheadstack;

import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseNode;

public class LinkedLookaheadStackTest extends AbstractLookaheadStackTest {

    @Override protected ILookaheadStack getStack(IncrementalParseNode root) {
        return new LinkedLookaheadStack(root);
    }

}
