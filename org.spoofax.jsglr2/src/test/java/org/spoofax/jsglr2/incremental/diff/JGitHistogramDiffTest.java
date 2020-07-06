package org.spoofax.jsglr2.incremental.diff;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.spoofax.jsglr2.incremental.EditorUpdate;

public class JGitHistogramDiffTest {

    private final JGitHistogramDiff diff = new JGitHistogramDiff();

    @Test public void testDiff() {
        assertEquals(asList(new EditorUpdate(2, 4, "fghij")), diff.diff("abcde", "abfghije"));
        assertEquals(asList(), diff.diff("abcde", "abcde"));
        assertEquals(asList(new EditorUpdate(0, 5, "fghij")), diff.diff("abcde", "fghij"));
        assertEquals(asList(new EditorUpdate(0, 0, "xyz")), diff.diff("abcde", "xyzabcde"));
        assertEquals(asList(new EditorUpdate(5, 5, "xyz")), diff.diff("abcde", "abcdexyz"));
        assertEquals(asList(new EditorUpdate(3, 4, "*")), diff.diff("x+x+x", "x+x*x"));
        assertEquals(asList(new EditorUpdate(1, 3, "uvw"), new EditorUpdate(12, 12, "xyz")),
            diff.diff("abcde\nfghij\nklmno", "auvwde\nfghij\nxyzklmno"));
    }

}
