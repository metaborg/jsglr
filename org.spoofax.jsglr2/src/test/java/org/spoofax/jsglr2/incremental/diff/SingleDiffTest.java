package org.spoofax.jsglr2.incremental.diff;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.spoofax.jsglr2.incremental.EditorUpdate;

public class SingleDiffTest {

    @Test public void testDiff() {
        assertEquals(new EditorUpdate(2, 4, "fghij"), new SingleDiff().diff("abcde", "abfghije").get(0));
        assertEquals(new EditorUpdate(5, 5, ""), new SingleDiff().diff("abcde", "abcde").get(0));
        assertEquals(new EditorUpdate(0, 5, "fghij"), new SingleDiff().diff("abcde", "fghij").get(0));
    }
}
