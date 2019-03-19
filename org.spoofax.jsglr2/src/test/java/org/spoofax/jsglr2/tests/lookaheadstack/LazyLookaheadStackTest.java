package org.spoofax.jsglr2.tests.lookaheadstack;

import org.spoofax.jsglr2.incremental.lookaheadstack.ILookaheadStack;
import org.spoofax.jsglr2.incremental.lookaheadstack.LazyLookaheadStack;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseNode;

public class LazyLookaheadStackTest extends AbstractLookaheadStackTest {

    @Override protected ILookaheadStack getStack(IncrementalParseNode root) {
        return new LazyLookaheadStack(root);
    }
}
