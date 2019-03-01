package org.spoofax.jsglr2.integrationtest.grammars;

import org.junit.Test;
import org.spoofax.jsglr2.incremental.EditorUpdate;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.parser.PositionInterval;
import org.spoofax.terms.ParseError;

public class LexicalTest extends BaseTestWithSdf3ParseTables {

    public LexicalTest() {
        super("lexical-id.sdf3");
    }

    @Test public void identifiers() throws ParseError {
        testSuccessByExpansions("a", "\"a\")");
        testSuccessByExpansions("aaaaa", "\"aaaaa\")");
    }

    @Test public void incrementalIdentifiers() throws ParseError {
        testIncrementalSuccessByExpansions("a",
            new EditorUpdate[] {
                new EditorUpdate(new PositionInterval(new Position(1, 1, 2), new Position(1, 1, 2)), "bcde"),
                new EditorUpdate(new PositionInterval(new Position(2, 1, 3), new Position(3, 1, 5)), "fghij") },
            new String[] { "\"a\"", "\"abcde\"", "\"abfghije\"" });
    }

}