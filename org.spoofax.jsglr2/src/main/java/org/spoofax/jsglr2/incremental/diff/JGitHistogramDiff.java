package org.spoofax.jsglr2.incremental.diff;

import java.util.ArrayList;
import java.util.List;

import org.spoofax.jsglr2.incremental.EditorUpdate;
import org.spoofax.jsglr2.incremental.diff.jgit.*;

// TODO the JGit HistogramDiff algorithm can be simplified because we are only using it in one very specific way
public class JGitHistogramDiff implements IStringDiff {

    private final HistogramDiff diff = new HistogramDiff();

    @Override public List<EditorUpdate> diff(String oldString, String newString) {
        // By default, RawText is split per line.
        // By passing a `lineMap` which is a list that contains all integers from 0 to length, this is avoided.
        RawText oldText = new RawText(oldString.toCharArray(), new IncrementingIntList(oldString.length()));
        RawText newText = new RawText(newString.toCharArray(), new IncrementingIntList(newString.length()));

        EditList edits = diff.diff(RawTextComparator.DEFAULT, oldText, newText);
        List<EditorUpdate> updates = new ArrayList<>(edits.size());
        for(Edit edit : edits) {
            updates.add(new EditorUpdate(edit.getBeginA(), edit.getEndA(),
                newString.substring(edit.getBeginB(), edit.getEndB())));
        }
        return updates;
    }

    /**
     * This list always contains (size + 2) integers: namely Integer.MIN_VALUE followed by the sequence 0 up to and
     * including size. This class avoids storing an entire list of integers because the entries in the list are trivial
     * to compute when queried.
     */
    static class IncrementingIntList extends IntList {
        private final int size;

        IncrementingIntList(int size) {
            super(0);
            this.size = size;
        }

        @Override public void add(int n) {
            throw new UnsupportedOperationException();
        }

        @Override public boolean contains(int value) {
            return value == Integer.MIN_VALUE || 0 <= value && value <= size;
        }

        @Override public void fillTo(int toIndex, int val) {
            throw new UnsupportedOperationException();
        }

        @Override public int get(int i) {
            if(i == 0)
                return Integer.MIN_VALUE;
            if(i >= size())
                throw new IndexOutOfBoundsException();
            return i - 1;
        }

        @Override public void set(int index, int n) {
            throw new UnsupportedOperationException();
        }

        @Override public int size() {
            return size + 2;
        }

        @Override public String toString() {
            return "[" + Integer.MIN_VALUE + ", 0, 1, ..., " + size + "]";
        }
    }

}
