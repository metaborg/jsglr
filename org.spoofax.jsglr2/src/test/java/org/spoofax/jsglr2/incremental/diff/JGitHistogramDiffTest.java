package org.spoofax.jsglr2.incremental.diff;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.spoofax.jsglr2.incremental.EditorUpdate;

public class JGitHistogramDiffTest {

    private final JGitHistogramDiff diff = new JGitHistogramDiff();

    @Test public void testDiff() {
        assertEquals(asList(new EditorUpdate(2, 4, "fghij")), diff.diff("abcde", "abfghije"));
        assertEquals(asList(), diff.diff("abcde", "abcde"));
        assertEquals(asList(new EditorUpdate(0, 5, "fghij")), diff.diff("abcde", "fghij"));
        assertEquals(asList(new EditorUpdate(1, 3, "uvw"), new EditorUpdate(12, 12, "xyz")),
            diff.diff("abcde\nfghij\nklmno", "auvwde\nfghij\nxyzklmno"));
    }

}
