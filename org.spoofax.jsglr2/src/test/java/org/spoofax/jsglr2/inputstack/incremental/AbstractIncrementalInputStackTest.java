package org.spoofax.jsglr2.inputstack.incremental;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;
import org.spoofax.jsglr2.incremental.EditorUpdate;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalCharacterNode;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseNode;

public abstract class AbstractIncrementalInputStackTest extends AbstractPreprocessingIncrementalInputStackTest {

    @Override protected IIncrementalInputStack getStack(IncrementalParseNode root, String inputString) {
        return getStack(inputString, inputString, root);
    }

    protected abstract IIncrementalInputStack getStack(String inputString, String previousInput,
        IncrementalParseNode previousResult, EditorUpdate... editorUpdates);

    @Test public void testThreeChildrenFullChange() {
        IncrementalParseNode root = new IncrementalParseNode(new IncrementalCharacterNode('a'),
            new IncrementalCharacterNode('b'), new IncrementalCharacterNode('c'));

        IIncrementalInputStack stack = getStack("xyz", "abc", root, new EditorUpdate(0, 3, "xyz"));
        assertTopOfStack('x', stack);
        assertLeftBreakdown('x', stack);
        assertPopLookahead('y', stack);
        assertLeftBreakdown('y', stack);
        assertPopLookahead('z', stack);
        assertLeftBreakdown('z', stack);
        assertPoppingEOF(stack);
    }

    @Test public void testThreeChildrenPartialChange() {
        IncrementalCharacterNode node1 = new IncrementalCharacterNode('a');
        IncrementalCharacterNode node2 = new IncrementalCharacterNode('b');
        IncrementalCharacterNode node3 = new IncrementalCharacterNode('c');
        IncrementalParseNode root = new IncrementalParseNode(node1, node2, node3);

        IIncrementalInputStack stack = getStack("ayc", "abc", root, new EditorUpdate(1, 2, "y"));
        assertTopOfStack(node1, stack);
        assertLookaheadIsUnchanged(stack, false);
        assertLeftBreakdown(node1, stack);

        assertPopLookahead('y', stack);
        assertLookaheadIsUnchanged(stack, true);
        assertLeftBreakdown('y', stack);

        assertPopLookahead(node3, stack);
        assertLookaheadIsUnchanged(stack, true);
        assertLeftBreakdown(node3, stack);

        assertPoppingEOF(stack);
    }

    @Test public void testNestedChange() {
        IncrementalCharacterNode node1 = new IncrementalCharacterNode('a');
        IncrementalCharacterNode node2 = new IncrementalCharacterNode('b');
        IncrementalCharacterNode node3 = new IncrementalCharacterNode('c');
        IncrementalCharacterNode node4 = new IncrementalCharacterNode('d');
        IncrementalParseNode parseNode1 = new IncrementalParseNode(node1, node2);
        IncrementalParseNode parseNode2 = new IncrementalParseNode(node3, node4);
        IncrementalParseNode root = new IncrementalParseNode(parseNode1, parseNode2);

        IIncrementalInputStack stack = getStack("wbcd", "abcd", root, new EditorUpdate(0, 1, "w"));
        assertTopOfStack('w', stack);
        assertLookaheadIsUnchanged(stack, true);
        assertLeftBreakdown('w', stack);
        assertPopLookahead(node2, stack);
        assertLookaheadIsUnchanged(stack, true);
        assertLeftBreakdown(node2, stack);
        assertPopLookahead(parseNode2, stack);
        assertLookaheadIsUnchanged(stack, true);
        assertPoppingEOF(stack);

        stack = getStack("axcd", "abcd", root, new EditorUpdate(1, 2, "x"));
        assertTopOfStack(node1, stack);
        assertLookaheadIsUnchanged(stack, false);
        assertLeftBreakdown(node1, stack);
        assertPopLookahead('x', stack);
        assertLookaheadIsUnchanged(stack, true);
        assertLeftBreakdown('x', stack);
        assertPopLookahead(parseNode2, stack);
        assertLookaheadIsUnchanged(stack, true);
        assertPoppingEOF(stack);

        stack = getStack("abyd", "abcd", root, new EditorUpdate(2, 3, "y"));
        assertTopOfStack(node1, stack);
        assertLookaheadIsUnchanged(stack, false);
        assertLeftBreakdown(node1, stack);
        assertPopLookahead(node2, stack);
        assertLookaheadIsUnchanged(stack, false);
        assertLeftBreakdown(node2, stack);
        assertPopLookahead('y', stack);
        assertLookaheadIsUnchanged(stack, true);
        assertLeftBreakdown('y', stack);
        assertPopLookahead(node4, stack);
        assertLookaheadIsUnchanged(stack, true);
        assertLeftBreakdown(node4, stack);
        assertPoppingEOF(stack);

        stack = getStack("abcz", "abcd", root, new EditorUpdate(3, 4, "z"));
        assertTopOfStack(parseNode1, stack);
        assertLookaheadIsUnchanged(stack, true);
        assertPopLookahead(node3, stack);
        assertLookaheadIsUnchanged(stack, false);
        assertLeftBreakdown(node3, stack);
        assertPopLookahead('z', stack);
        assertLookaheadIsUnchanged(stack, true);
        assertLeftBreakdown('z', stack);
        assertPoppingEOF(stack);
    }

    @Test public void testUnchangedLookaheadForTerminal() {
        IncrementalCharacterNode node1 = new IncrementalCharacterNode('a');
        IncrementalCharacterNode node2 = new IncrementalCharacterNode('b');
        IncrementalCharacterNode node3 = new IncrementalCharacterNode('c');
        IncrementalCharacterNode node4 = new IncrementalCharacterNode('d');
        IncrementalParseNode parseNode1 = new IncrementalParseNode(node1, node2);
        IncrementalParseNode parseNode3 = new IncrementalParseNode(node4);
        IncrementalParseNode root = new IncrementalParseNode(parseNode1, node3, parseNode3);

        IIncrementalInputStack stack = getStack("abcz", "abcd", root, new EditorUpdate(3, 4, "z"));
        assertTopOfStack(parseNode1, stack);
        assertLookaheadIsUnchanged(stack, true);
        assertPopLookahead(node3, stack);
        // `node3` should actually have `lookaheadIsUnchanged == false`, because it is followed by the update.
        // However, since it is a terminal node, we do not care, since it cannot be broken down anyway.
        assertLookaheadIsUnchanged(stack, true);
        assertPopLookahead('z', stack);
        assertLookaheadIsUnchanged(stack, true);
        assertLeftBreakdown('z', stack);
        assertPoppingEOF(stack);
    }

    @Test public void testChangeBeforeNullNode() {
        IncrementalCharacterNode node1 = new IncrementalCharacterNode('a');
        IncrementalCharacterNode node2 = new IncrementalCharacterNode('b');
        IncrementalCharacterNode node3 = new IncrementalCharacterNode('c');
        IncrementalCharacterNode node4 = new IncrementalCharacterNode('d');
        IncrementalParseNode parseNode1 = new IncrementalParseNode(node1, node2);
        IncrementalParseNode parseNode2 = new IncrementalParseNode();
        IncrementalParseNode parseNode3 = new IncrementalParseNode(node3, node4);
        IncrementalParseNode root = new IncrementalParseNode(parseNode1, parseNode2, parseNode3);

        IIncrementalInputStack stack = getStack("axcd", "abcd", root, new EditorUpdate(1, 2, "x"));
        assertTopOfStack(node1, stack);
        assertLookaheadIsUnchanged(stack, false);
        assertLeftBreakdown(node1, stack);
        assertPopLookahead('x', stack);
        assertLookaheadIsUnchanged(stack, true);
        assertLeftBreakdown('x', stack);
        // Note that the null-yield node `parseNode2` has been broken down, because it directly follows an update
        assertPopLookahead(parseNode3, stack);
        assertLookaheadIsUnchanged(stack, true);
        assertPoppingEOF(stack);
    }

    protected static void assertTopOfStack(IncrementalParseForest forest, IIncrementalInputStack stack) {
        assertSame(forest, stack.getNode());
    }

    protected static void assertTopOfStack(int character, IIncrementalInputStack stack) {
        assertEquals(character, ((IncrementalCharacterNode) stack.getNode()).character());
    }

    protected static void assertLookaheadIsUnchanged(IIncrementalInputStack stack, boolean lookaheadIsUnchanged) {
        assertEquals(lookaheadIsUnchanged, stack.lookaheadIsUnchanged(), "Node " + stack.getNode()
            + " was expected to have " + (lookaheadIsUnchanged ? "UN" : "") + "changed lookahead");
    }
}
