package org.spoofax.jsglr2.incremental.diff;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.spoofax.jsglr2.incremental.EditorUpdate;

public class SingleDiffTest {

    private final SingleDiff diff = new SingleDiff();

    @Test public void testDiff() {
        assertEquals(new EditorUpdate(2, 4, "fghij"), diff.diff("abcde", "abfghije").get(0));
        assertEquals(new EditorUpdate(5, 5, ""), diff.diff("abcde", "abcde").get(0));
        assertEquals(new EditorUpdate(0, 5, "fghij"), diff.diff("abcde", "fghij").get(0));
        assertEquals(new EditorUpdate(1, 12, "uvw\nfghij\nxyz"),
            diff.diff("abcde\nfghij\nklmno", "auvwde\nfghij\nxyzklmno").get(0));
    }

}
