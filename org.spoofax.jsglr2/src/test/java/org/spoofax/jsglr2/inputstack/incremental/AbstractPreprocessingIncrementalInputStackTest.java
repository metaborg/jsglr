package org.spoofax.jsglr2.inputstack.incremental;

import static org.junit.jupiter.api.Assertions.*;
import static org.spoofax.jsglr2.incremental.parseforest.IncrementalCharacterNode.EOF_NODE;
import static org.spoofax.jsglr2.parser.PositionTest.SMILEY_CODEPOINT;
import static org.spoofax.jsglr2.parser.PositionTest.SMILEY_STRING;

import java.util.function.BiConsumer;

import org.junit.jupiter.api.Test;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalCharacterNode;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseNode;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalSkippedNode;

public abstract class AbstractPreprocessingIncrementalInputStackTest {

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
        IncrementalCharacterNode node1 = new IncrementalCharacterNode('a');
        IncrementalCharacterNode node2 = new IncrementalCharacterNode('b');
        IncrementalCharacterNode node3 = new IncrementalCharacterNode('c');
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
        IncrementalCharacterNode node1 = new IncrementalCharacterNode('a');
        IncrementalParseNode node2 = new IncrementalSkippedNode(null,
            new IncrementalParseForest[] { new IncrementalCharacterNode('b'), new IncrementalCharacterNode('c') });
        IncrementalCharacterNode node3 = new IncrementalCharacterNode('d');
        IncrementalParseNode root = new IncrementalParseNode(node1, node2, node3);

        IIncrementalInputStack stack = getStack(root, "abcd");
        assertLeftBreakdown(node1, stack);
        assertLeftBreakdown(node1, stack);
        assertPopLookahead(node2, stack);
        assertLeftBreakdown('b', stack);
        assertPopLookahead('c', stack);
        assertPopLookahead(node3, stack);
        assertLeftBreakdown(node3, stack);
        assertPoppingEOF(stack);

        assertPoppingEOF(getStack(root, "abcd"));
    }

    @Test public void testSkippedNodeUnicode() {
        IncrementalCharacterNode node1 = new IncrementalCharacterNode('a');
        IncrementalParseNode node2 = new IncrementalSkippedNode(null, new IncrementalParseForest[] {
            new IncrementalCharacterNode('b'), new IncrementalCharacterNode(SMILEY_CODEPOINT) });
        IncrementalCharacterNode node3 = new IncrementalCharacterNode('d');
        IncrementalParseNode root = new IncrementalParseNode(node1, node2, node3);

        String inputString = "ab" + SMILEY_STRING + "d";
        IIncrementalInputStack stack = getStack(root, inputString);
        assertLeftBreakdown(node1, stack);
        assertLeftBreakdown(node1, stack);
        assertPopLookahead(node2, stack);
        assertLeftBreakdown('b', stack);
        assertPopLookahead(SMILEY_CODEPOINT, stack);
        assertPopLookahead(node3, stack);
        assertLeftBreakdown(node3, stack);
        assertPoppingEOF(stack);

        assertPoppingEOF(getStack(root, inputString));
    }

    @Test public void testTwoSubtrees() {
        IncrementalCharacterNode node1 = new IncrementalCharacterNode('a');
        IncrementalCharacterNode node2 = new IncrementalCharacterNode('b');
        IncrementalCharacterNode node3 = new IncrementalCharacterNode('c');
        IncrementalCharacterNode node4 = new IncrementalCharacterNode('d');
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
        IncrementalCharacterNode node1 = new IncrementalCharacterNode('a');
        IncrementalCharacterNode node2 = new IncrementalCharacterNode('b');
        IncrementalCharacterNode node3 = new IncrementalCharacterNode('c');
        IncrementalCharacterNode node4 = new IncrementalCharacterNode('d');
        IncrementalParseNode parseNode1 = new IncrementalParseNode(node1, node2);
        IncrementalParseNode parseNode2 = new IncrementalParseNode(node3, node4);
        IncrementalParseNode root = new IncrementalParseNode(parseNode1, parseNode2);

        TestTuple[] actions =
            new TestTuple[] { new TestTuple(AbstractPreprocessingIncrementalInputStackTest::assertLeftBreakdown, parseNode1),
                new TestTuple(AbstractPreprocessingIncrementalInputStackTest::assertLeftBreakdown, node1),
                new TestTuple(AbstractPreprocessingIncrementalInputStackTest::assertLeftBreakdown, node1),
                new TestTuple(AbstractPreprocessingIncrementalInputStackTest::assertPopLookahead, node2),
                new TestTuple(AbstractPreprocessingIncrementalInputStackTest::assertLeftBreakdown, node2),
                new TestTuple(AbstractPreprocessingIncrementalInputStackTest::assertPopLookahead, parseNode2),
                new TestTuple(AbstractPreprocessingIncrementalInputStackTest::assertLeftBreakdown, node3),
                new TestTuple(AbstractPreprocessingIncrementalInputStackTest::assertLeftBreakdown, node3),
                new TestTuple(AbstractPreprocessingIncrementalInputStackTest::assertPopLookahead, node4),
                new TestTuple(AbstractPreprocessingIncrementalInputStackTest::assertLeftBreakdown, node4) };

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
        IncrementalCharacterNode node1 = new IncrementalCharacterNode('a');
        IncrementalCharacterNode node2 = new IncrementalCharacterNode('b');
        IncrementalCharacterNode node3 = new IncrementalCharacterNode('c');
        IncrementalCharacterNode node4 = new IncrementalCharacterNode('d');
        IncrementalParseNode parseNode1 = new IncrementalParseNode(node1, node2);
        IncrementalParseNode parseNode2 = new IncrementalParseNode(node3, node4);
        IncrementalParseNode root = new IncrementalParseNode(parseNode1, parseNode2);

        IIncrementalInputStack original = getStack(root);
        IIncrementalInputStack clone = original.clone();
        for(IIncrementalInputStack stack : new IIncrementalInputStack[] { original, clone }) {
            assertEquals('a', stack.actionQueryCharacter());
            assertArrayEquals("b".codePoints().toArray(), stack.actionQueryLookahead(1));
            assertArrayEquals("bc".codePoints().toArray(), stack.actionQueryLookahead(2));
            assertArrayEquals("bcd".codePoints().toArray(), stack.actionQueryLookahead(3));
            assertArrayEquals("bcd".codePoints().toArray(), stack.actionQueryLookahead(4));
            assertArrayEquals("bcd".codePoints().toArray(), stack.actionQueryLookahead(5));
        }
    }

    protected void assertPoppingRoot(IncrementalParseNode root) {
        assertPoppingEOF(getStack(root));
    }

    protected static void assertPoppingEOF(IIncrementalInputStack stack) {
        assertPopLookahead(EOF_NODE, stack);
        assertLeftBreakdown(EOF_NODE, stack);
        assertPopLookahead(null, stack);
    }

    protected static void assertLeftBreakdown(IncrementalParseForest forest, IIncrementalInputStack stack) {
        stack.breakDown();
        assertSame(forest, stack.getNode());
    }

    protected static void assertLeftBreakdown(int character, IIncrementalInputStack stack) {
        stack.breakDown();
        assertEquals(character, ((IncrementalCharacterNode) stack.getNode()).character());
    }

    protected static void assertPopLookahead(IncrementalParseForest forest, IIncrementalInputStack stack) {
        stack.next();
        assertSame(forest, stack.getNode());
    }

    protected static void assertPopLookahead(int character, IIncrementalInputStack stack) {
        stack.next();
        assertEquals(character, ((IncrementalCharacterNode) stack.getNode()).character());
    }
}
