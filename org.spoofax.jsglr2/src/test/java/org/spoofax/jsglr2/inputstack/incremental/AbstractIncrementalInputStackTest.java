package org.spoofax.jsglr2.inputstack.incremental;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.spoofax.jsglr2.incremental.parseforest.IncrementalCharacterNode.EOF_NODE;

import java.util.function.BiConsumer;

import org.junit.Test;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalCharacterNode;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseNode;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalSkippedNode;

public abstract class AbstractIncrementalInputStackTest {

    protected abstract IIncrementalInputStack getStack(IncrementalParseNode root, String inputString);

    protected IIncrementalInputStack getStack(IncrementalParseNode root) {
        return getStack(root, root.getYield());
    }

    @Test public void testOneNode() {
        IncrementalCharacterNode node = new IncrementalCharacterNode(42);
        IncrementalParseNode root = new IncrementalParseNode(node);
        IIncrementalInputStack stack = getStack(root);
        assertLeftBreakdown(node, stack);
        assertPoppingEOF(stack);

        assertPoppingRoot(root);
    }

    @Test public void testThreeChildrenNoYield() {
        IncrementalParseNode node1 = new IncrementalParseNode();
        IncrementalParseNode node2 = new IncrementalParseNode();
        IncrementalParseNode node3 = new IncrementalParseNode();
        IncrementalParseNode root = new IncrementalParseNode(node1, node2, node3);

        IIncrementalInputStack stack = getStack(root);
        assertLeftBreakdown(node1, stack);
        assertPopLookahead(node2, stack);
        assertLeftBreakdown(node3, stack);
        assertPoppingEOF(stack);

        assertPoppingRoot(root);
    }

    @Test public void testThreeChildren() {
        IncrementalCharacterNode node1 = new IncrementalCharacterNode(97);
        IncrementalCharacterNode node2 = new IncrementalCharacterNode(98);
        IncrementalCharacterNode node3 = new IncrementalCharacterNode(99);
        IncrementalParseNode root = new IncrementalParseNode(node1, node2, node3);

        IIncrementalInputStack stack = getStack(root);
        assertLeftBreakdown(node1, stack);
        assertLeftBreakdown(node1, stack);
        assertPopLookahead(node2, stack);
        assertLeftBreakdown(node2, stack);
        assertPopLookahead(node3, stack);
        assertLeftBreakdown(node3, stack);
        assertPoppingEOF(stack);

        assertPoppingRoot(root);
    }

    @Test public void testSkippedNode() {
        IncrementalCharacterNode node1 = new IncrementalCharacterNode(97);
        IncrementalParseNode node2 = new IncrementalSkippedNode(null,
            new IncrementalParseForest[] { new IncrementalCharacterNode(98), new IncrementalCharacterNode(99) });
        IncrementalCharacterNode node3 = new IncrementalCharacterNode(100);
        IncrementalParseNode root = new IncrementalParseNode(node1, node2, node3);

        IIncrementalInputStack stack = getStack(root, "abcd");
        assertLeftBreakdown(node1, stack);
        assertLeftBreakdown(node1, stack);
        assertPopLookahead(node2, stack);
        stack.breakDown();
        assertEquals(98, ((IncrementalCharacterNode) stack.getNode()).character);
        stack.next();
        assertEquals(99, ((IncrementalCharacterNode) stack.getNode()).character);
        assertPopLookahead(node3, stack);
        assertLeftBreakdown(node3, stack);
        assertPoppingEOF(stack);

        assertPoppingEOF(getStack(root, "abcd"));
    }

    @Test public void testTwoSubtrees() {
        IncrementalCharacterNode node1 = new IncrementalCharacterNode(97);
        IncrementalCharacterNode node2 = new IncrementalCharacterNode(98);
        IncrementalCharacterNode node3 = new IncrementalCharacterNode(99);
        IncrementalCharacterNode node4 = new IncrementalCharacterNode(100);
        IncrementalParseNode parseNode1 = new IncrementalParseNode(node1, node2);
        IncrementalParseNode parseNode2 = new IncrementalParseNode(node3, node4);
        IncrementalParseNode root = new IncrementalParseNode(parseNode1, parseNode2);

        IIncrementalInputStack stack = getStack(root);
        assertLeftBreakdown(parseNode1, stack);
        assertLeftBreakdown(node1, stack);
        assertLeftBreakdown(node1, stack);
        assertPopLookahead(node2, stack);
        assertLeftBreakdown(node2, stack);
        assertPopLookahead(parseNode2, stack);
        assertLeftBreakdown(node3, stack);
        assertLeftBreakdown(node3, stack);
        assertPopLookahead(node4, stack);
        assertLeftBreakdown(node4, stack);
        assertPoppingEOF(stack);

        stack = getStack(root);
        assertLeftBreakdown(parseNode1, stack);
        assertLeftBreakdown(node1, stack);
        assertLeftBreakdown(node1, stack);
        assertPopLookahead(node2, stack);
        assertLeftBreakdown(node2, stack);
        assertPopLookahead(parseNode2, stack);
        assertPoppingEOF(stack);

        stack = getStack(root);
        assertLeftBreakdown(parseNode1, stack);
        assertPopLookahead(parseNode2, stack);
        assertLeftBreakdown(node3, stack);
        assertLeftBreakdown(node3, stack);
        assertPopLookahead(node4, stack);
        assertLeftBreakdown(node4, stack);
        assertPoppingEOF(stack);

        assertPoppingRoot(root);
    }

    private static class TestTuple {
        final BiConsumer<IncrementalParseForest, IIncrementalInputStack> action;
        final IncrementalParseForest expected;

        TestTuple(BiConsumer<IncrementalParseForest, IIncrementalInputStack> action, IncrementalParseForest expected) {
            this.action = action;
            this.expected = expected;
        }
    }

    @Test public void testClone() {
        IncrementalCharacterNode node1 = new IncrementalCharacterNode(97);
        IncrementalCharacterNode node2 = new IncrementalCharacterNode(98);
        IncrementalCharacterNode node3 = new IncrementalCharacterNode(99);
        IncrementalCharacterNode node4 = new IncrementalCharacterNode(100);
        IncrementalParseNode parseNode1 = new IncrementalParseNode(node1, node2);
        IncrementalParseNode parseNode2 = new IncrementalParseNode(node3, node4);
        IncrementalParseNode root = new IncrementalParseNode(parseNode1, parseNode2);

        TestTuple[] actions =
            new TestTuple[] { new TestTuple(AbstractIncrementalInputStackTest::assertLeftBreakdown, parseNode1),
                new TestTuple(AbstractIncrementalInputStackTest::assertLeftBreakdown, node1),
                new TestTuple(AbstractIncrementalInputStackTest::assertLeftBreakdown, node1),
                new TestTuple(AbstractIncrementalInputStackTest::assertPopLookahead, node2),
                new TestTuple(AbstractIncrementalInputStackTest::assertLeftBreakdown, node2),
                new TestTuple(AbstractIncrementalInputStackTest::assertPopLookahead, parseNode2),
                new TestTuple(AbstractIncrementalInputStackTest::assertLeftBreakdown, node3),
                new TestTuple(AbstractIncrementalInputStackTest::assertLeftBreakdown, node3),
                new TestTuple(AbstractIncrementalInputStackTest::assertPopLookahead, node4),
                new TestTuple(AbstractIncrementalInputStackTest::assertLeftBreakdown, node4) };

        IIncrementalInputStack original = getStack(root);
        IIncrementalInputStack[] clones = new IIncrementalInputStack[actions.length];
        for(int i = 0; i < actions.length; i++) {
            // Before each action, create a clone
            clones[i] = original.clone();
            // Check whether the action works correctly for both the original and all clones created so far
            actions[i].action.accept(actions[i].expected, original);
            for(int j = 0; j <= i; j++) {
                actions[i].action.accept(actions[i].expected, clones[j]);
                // Also check that the position of the clone matches that of the original, after the action
                assertEquals(((AbstractInputStack) original).currentOffset,
                    ((AbstractInputStack) clones[j]).currentOffset);
            }
        }
        assertPoppingEOF(original);
        for(IIncrementalInputStack clone : clones) {
            assertPoppingEOF(clone);
        }
    }

    @Test public void testLookaheadQuery() {
        IncrementalCharacterNode node1 = new IncrementalCharacterNode(97);
        IncrementalCharacterNode node2 = new IncrementalCharacterNode(98);
        IncrementalCharacterNode node3 = new IncrementalCharacterNode(99);
        IncrementalCharacterNode node4 = new IncrementalCharacterNode(100);
        IncrementalParseNode parseNode1 = new IncrementalParseNode(node1, node2);
        IncrementalParseNode parseNode2 = new IncrementalParseNode(node3, node4);
        IncrementalParseNode root = new IncrementalParseNode(parseNode1, parseNode2);

        IIncrementalInputStack original = getStack(root);
        IIncrementalInputStack clone = original.clone();
        for(IIncrementalInputStack stack : new IIncrementalInputStack[] { original, clone }) {
            assertEquals('a', stack.actionQueryCharacter());
            assertEquals("b", stack.actionQueryLookahead(1));
            assertEquals("bc", stack.actionQueryLookahead(2));
            assertEquals("bcd", stack.actionQueryLookahead(3));
            assertEquals("bcd", stack.actionQueryLookahead(4));
            assertEquals("bcd", stack.actionQueryLookahead(5));
        }
    }

    private void assertPoppingRoot(IncrementalParseNode root) {
        assertPoppingEOF(getStack(root));
    }

    private static void assertPoppingEOF(IIncrementalInputStack stack) {
        assertPopLookahead(EOF_NODE, stack);
        assertLeftBreakdown(EOF_NODE, stack);
        assertPopLookahead(null, stack);
    }

    private static void assertLeftBreakdown(IncrementalParseForest forest, IIncrementalInputStack stack) {
        stack.breakDown();
        assertSame(forest, stack.getNode());
    }

    private static void assertPopLookahead(IncrementalParseForest forest, IIncrementalInputStack stack) {
        stack.next();
        assertSame(forest, stack.getNode());
    }
}
