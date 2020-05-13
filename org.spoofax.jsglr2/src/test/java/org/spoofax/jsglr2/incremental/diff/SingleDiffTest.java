package org.spoofax.jsglr2.incremental.diff;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.spoofax.jsglr2.incremental.EditorUpdate;

public class SingleDiffTest {

    private final SingleDiff diff = new SingleDiff();

    @Test public void testDiff() {
        assertEquals(new EditorUpdate(2, 4, "fghij"), diff.diff("abcde", "abfghije").get(0));
        assertEquals(new EditorUpdate(5, 5, ""), diff.diff("abcde", "abcde").get(0));
        assertEquals(new EditorUpdate(0, 5, "fghij"), diff.diff("abcde", "fghij").get(0));
        assertEquals(new EditorUpdate(0, 0, "xyz"), diff.diff("abcde", "xyzabcde").get(0));
        assertEquals(new EditorUpdate(5, 5, "xyz"), diff.diff("abcde", "abcdexyz").get(0));
        assertEquals(new EditorUpdate(3, 4, "*"), diff.diff("x+x+x", "x+x*x").get(0));
        assertEquals(new EditorUpdate(1, 12, "uvwde\nfghij\nxyz"),
            diff.diff("abcde\nfghij\nklmno", "auvwde\nfghij\nxyzklmno").get(0));
    }

}
