package org.spoofax.jsglr2.incremental;

public class EditorUpdate {
    public final int deletedStart;
    public final int deletedEnd;
    public final String insterted;

    public EditorUpdate(int deletedStart, int deletedEnd, String insterted) {
        this.deletedStart = deletedStart;
        this.deletedEnd = deletedEnd;
        this.insterted = insterted;
    }
}
