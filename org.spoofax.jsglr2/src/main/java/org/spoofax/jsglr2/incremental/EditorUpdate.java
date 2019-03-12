package org.spoofax.jsglr2.incremental;

public class EditorUpdate {
    public final int deletedStart;
    public final int deletedEnd;
    public final String inserted;

    public EditorUpdate(int deletedStart, int deletedEnd, String inserted) {
        this.deletedStart = deletedStart;
        this.deletedEnd = deletedEnd;
        this.inserted = inserted;
    }
}
