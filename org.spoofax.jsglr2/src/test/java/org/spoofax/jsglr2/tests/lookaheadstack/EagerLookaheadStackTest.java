package org.spoofax.jsglr2.tests.lookaheadstack;

import org.spoofax.jsglr2.incremental.lookaheadstack.EagerLookaheadStack;
import org.spoofax.jsglr2.incremental.lookaheadstack.ILookaheadStack;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseNode;

public class EagerLookaheadStackTest extends AbstractLookaheadStackTest {
    @Override protected ILookaheadStack getStack(IncrementalParseNode root) {
        return new EagerLookaheadStack(root);
    }
}
