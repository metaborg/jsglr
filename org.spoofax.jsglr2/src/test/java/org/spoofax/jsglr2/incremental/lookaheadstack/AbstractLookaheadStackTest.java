package org.spoofax.jsglr2.incremental.lookaheadstack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.metaborg.characterclasses.CharacterClassFactory.EOF_INT;
import static org.spoofax.jsglr2.incremental.parseforest.IncrementalCharacterNode.EOF_NODE;

import org.junit.Test;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalCharacterNode;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseNode;

public abstract class AbstractLookaheadStackTest {
    protected abstract ILookaheadStack getStack(IncrementalParseNode root);

    @Test public void testOneNode() {
        IncrementalCharacterNode node = new IncrementalCharacterNode(42);
        IncrementalParseNode root = new IncrementalParseNode(node);
        ILookaheadStack stack = getStack(root);
        assertLeftBreakdown(node, stack);
        assertPoppingEOF(stack);

        assertPoppingRoot(root);
    }

    @Test public void testThreeChildrenNoYield() {
        IncrementalParseNode node1 = new IncrementalParseNode();
        IncrementalParseNode node2 = new IncrementalParseNode();
        IncrementalParseNode node3 = new IncrementalParseNode();
        IncrementalParseNode root = new IncrementalParseNode(node1, node2, node3);

        ILookaheadStack stack = getStack(root);
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

        ILookaheadStack stack = getStack(root);
        assertLeftBreakdown(node1, stack);
        assertLeftBreakdown(node1, stack);
        assertPopLookahead(node2, stack);
        assertLeftBreakdown(node2, stack);
        assertPopLookahead(node3, stack);
        assertLeftBreakdown(node3, stack);
        assertPoppingEOF(stack);

        assertPoppingRoot(root);
    }

    @Test public void testTwoSubtrees() {
        IncrementalCharacterNode node1 = new IncrementalCharacterNode(97);
        IncrementalCharacterNode node2 = new IncrementalCharacterNode(98);
        IncrementalCharacterNode node3 = new IncrementalCharacterNode(99);
        IncrementalCharacterNode node4 = new IncrementalCharacterNode(100);
        IncrementalParseNode parseNode1 = new IncrementalParseNode(node1, node2);
        IncrementalParseNode parseNode2 = new IncrementalParseNode(node3, node4);
        IncrementalParseNode root = new IncrementalParseNode(parseNode1, parseNode2);

        ILookaheadStack stack = getStack(root);
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

    @Test public void testLookaheadQuery() {
        IncrementalCharacterNode node1 = new IncrementalCharacterNode(97);
        IncrementalCharacterNode node2 = new IncrementalCharacterNode(98);
        IncrementalCharacterNode node3 = new IncrementalCharacterNode(99);
        IncrementalCharacterNode node4 = new IncrementalCharacterNode(100);
        IncrementalParseNode parseNode1 = new IncrementalParseNode(node1, node2);
        IncrementalParseNode parseNode2 = new IncrementalParseNode(node3, node4);
        IncrementalParseNode root = new IncrementalParseNode(parseNode1, parseNode2);

        ILookaheadStack stack = getStack(root);
        assertEquals('a', stack.actionQueryCharacter());
        assertEquals("b", stack.actionQueryLookahead(1));
        assertEquals("bc", stack.actionQueryLookahead(2));
        assertEquals("bcd", stack.actionQueryLookahead(3));
        assertEquals("bcd" + (char) EOF_INT, stack.actionQueryLookahead(4));
        assertEquals("bcd" + (char) EOF_INT, stack.actionQueryLookahead(5));
    }

    private void assertPoppingRoot(IncrementalParseNode root) {
        assertPoppingEOF(getStack(root));
    }

    private static void assertPoppingEOF(ILookaheadStack stack) {
        assertPopLookahead(EOF_NODE, stack);
        assertLeftBreakdown(EOF_NODE, stack);
        assertPopLookahead(null, stack);
    }

    private static void assertLeftBreakdown(IncrementalParseForest forest, ILookaheadStack stack) {
        stack.leftBreakdown();
        assertSame(forest, stack.get());
    }

    private static void assertPopLookahead(IncrementalParseForest forest, ILookaheadStack stack) {
        stack.popLookahead();
        assertSame(forest, stack.get());
    }
}
