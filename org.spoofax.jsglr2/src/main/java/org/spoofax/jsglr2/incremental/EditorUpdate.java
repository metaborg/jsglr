package org.spoofax.jsglr2.incremental;

import java.util.Objects;

public class EditorUpdate {

    public enum Type {
        DELETION, INSERTION, REPLACEMENT
    }

    public final int deletedStart;
    public final int deletedEnd;
    public final String inserted;

    public EditorUpdate(int deletedStart, int deletedEnd, String inserted) {
        this.deletedStart = deletedStart;
        this.deletedEnd = deletedEnd;
        this.inserted = inserted;
    }

    public Type getType() {
        if(deletedStart == deletedEnd)
            return Type.INSERTION;
        if(inserted.length() == 0)
            return Type.DELETION;
        return Type.REPLACEMENT;
    }

    @Override public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        EditorUpdate that = (EditorUpdate) o;
        return deletedStart == that.deletedStart && deletedEnd == that.deletedEnd && inserted.equals(that.inserted);
    }

    @Override public int hashCode() {
        return Objects.hash(deletedStart, deletedEnd, inserted);
    }

    @Override public String toString() {
        return "EditorUpdate{" + "deletedStart=" + deletedStart + ", deletedEnd=" + deletedEnd + ", inserted='"
            + inserted + '\'' + '}';
    }
}
