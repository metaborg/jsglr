package org.spoofax.jsglr2.util;

public class Range {

    public final int from, to;

    public Range(int min, int max) {
        this.from = min;
        this.to = max;
    }

    public boolean contains(int i) {
        return from <= i && i <= to;
    }

    @Override public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }

        Range that = (Range) o;

        return from == that.from && to == that.to;
    }

    @Override public String toString() {
        return "[" + from + "," + to + "]";
    }

}