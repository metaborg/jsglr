package org.spoofax.jsglr2.incremental.diff;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.spoofax.jsglr2.incremental.EditorUpdate;
import org.spoofax.jsglr2.incremental.IncrementalParse;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalCharacterNode;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseNode;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestRepresentation;
import org.spoofax.jsglr2.parser.ParserVariant;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.reducing.Reducing;
import org.spoofax.jsglr2.stack.StackRepresentation;
import org.spoofax.jsglr2.stack.collections.ActiveStacksRepresentation;
import org.spoofax.jsglr2.stack.collections.ForActorStacksRepresentation;

public class ProcessUpdatesTest {

    private final ProcessUpdates<?> processUpdates =
        new ProcessUpdates<>(new IncrementalParse<>(new ParserVariant(ActiveStacksRepresentation.standard(),
            ForActorStacksRepresentation.standard(), ParseForestRepresentation.standard(),
            ParseForestConstruction.standard(), StackRepresentation.standard(), Reducing.standard()), "", "",
            new ParserObserving<>()));

    @Test public void testDeleteSubtree() {
        IncrementalParseNode previous = node(node(0, 1), node(2, 3), node(4, 5));

        IncrementalParseNode expected = node(node(0, 1), node(4, 5));

        testUpdate(previous, expected, new EditorUpdate(2, 4, ""));
    }

    @Test public void testPrepend() {
        IncrementalParseNode previous = node(node(0), node(1, 2));

        IncrementalParseNode expected = node(node(node(4, 5), node(0)), node(1, 2));

        testUpdate(previous, expected, new EditorUpdate(0, 0, "\4\5"));
    }

    @Test public void testPrependBeforeEmpty() {
        IncrementalParseNode previous = node(node(), node(0), node(1, 2));

        IncrementalParseNode expected = node(node(node(4, 5), node(0)), node(1, 2));

        testUpdate(previous, expected, new EditorUpdate(0, 0, "\4\5"));
    }

    @Test public void testAppend() {
        IncrementalParseNode previous = node(node(0, 1), node(2, 3));

        IncrementalParseNode expected = node(node(0, 1), node(node(2), node(node(3), node(4, 5))));

        testUpdate(previous, expected, new EditorUpdate(4, 4, "\4\5"));
    }

    @Test public void testAppendBeforeEmpty() {
        IncrementalParseNode previous = node(node(0, 1), node(2, 3), node());

        IncrementalParseNode expected = node(node(0, 1), node(node(2), node(node(3), node(4, 5))), node());

        testUpdate(previous, expected, new EditorUpdate(4, 4, "\4\5"));
    }

    @Test public void testInsert() {
        IncrementalParseNode previous = node(node(0, 1), node(2, 3));

        IncrementalParseNode expected = node(node(node(0), node(node(1), node(4, 5))), node(2, 3));

        testUpdate(previous, expected, new EditorUpdate(2, 2, "\4\5"));
    }

    @Test public void testReplace() {
        IncrementalParseNode previous = node(node(0, 1), node(2, 3));

        IncrementalParseNode expected = node(node(node(0), node(4, 5)), node(2, 3));

        testUpdate(previous, expected, new EditorUpdate(1, 2, "\4\5"));
    }

    @Test public void testReplaceFirst() {
        IncrementalParseNode previous = node(node(0, 1), node(2, 3));

        IncrementalParseNode expected = node(node(node(4, 5, 6), node(1)), node(2, 3));

        testUpdate(previous, expected, new EditorUpdate(0, 1, "\4\5\6"));
    }

    @Test public void testReplaceFirstAfterEmpty() {
        IncrementalParseNode previous = node(node(), node(0, 1), node(2, 3));

        IncrementalParseNode expected = node(node(node(4, 5, 6), node(1)), node(2, 3));

        testUpdate(previous, expected, new EditorUpdate(0, 1, "\4\5\6"));
    }

    @Test public void testReplaceEnd() {
        IncrementalParseNode previous = node(node(node(0, 1), node(2)), node(3));

        IncrementalParseNode expected = node(node(node(0, 1), node(4, 5, 6)));

        testUpdate(previous, expected, new EditorUpdate(2, 4, "\4\5\6"));
    }

    @Test public void testReplaceMultipleWithGap() {
        IncrementalParseNode previous = node(node(node(0, 1), node(2)), node(3));

        IncrementalParseNode expected = node(node(node(node(0), node(4, 5)), node(2)), node(6, 7));

        testUpdate(previous, expected, new EditorUpdate(1, 2, "\4\5"), new EditorUpdate(3, 4, "\6\7"));
    }

    @Test public void testReplaceMultipleWithoutGap() {
        IncrementalParseNode previous = node(node(node(0, 1), node(2)), node(3));

        IncrementalParseNode expected = node(node(node(node(0), node(4, 5)), node(6, 7)), node(3));

        testUpdate(previous, expected, new EditorUpdate(1, 2, "\4\5"), new EditorUpdate(2, 3, "\6\7"));
    }


    private void testUpdate(IncrementalParseNode previous, IncrementalParseNode expected, EditorUpdate... updates) {
        assertEquals(expected.toString(), processUpdates.processUpdates(previous, updates).toString());
    }

    private static IncrementalCharacterNode node(int i) {
        return new IncrementalCharacterNode(i);
    }

    private static IncrementalParseNode node(int... characters) {
        IncrementalParseForest[] children = new IncrementalParseForest[characters.length];
        for(int i = 0; i < characters.length; i++) {
            children[i] = node(characters[i]);
        }
        return new IncrementalParseNode(children);
    }

    private static IncrementalParseNode node(IncrementalParseForest... children) {
        return new IncrementalParseNode(children);
    }

    private static IncrementalParseNode node() {
        return new IncrementalParseNode();
    }

}
