package org.spoofax.jsglr.client;

import static org.spoofax.jsglr.client.SGLR.EOF;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * A series of character ranges.
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class RangeList implements Serializable {

    private static final long serialVersionUID = 16593569;

    public static final int NONE = -2;

    /** An ordered array of low-high pairs. Both ends of each range are inclusive. */
    private final int[] ranges;
    /** @see SGLR#EOF */
    private final boolean containsEOF;

    private final int singularRange;

    public RangeList(int[] ranges, boolean containsEOF) {
        this.containsEOF = containsEOF;
        if(!containsEOF && (ranges.length == 1 || ranges.length == 2 && ranges[0] == ranges[1])) {
            this.ranges = null;
            singularRange = ranges[0];
        } else if(ranges.length == 0 && containsEOF) {
            // In case that the range list only contains EOF, it can also be a singluarRange
            this.ranges = null;
            singularRange = EOF;
        } else {
            this.ranges = ranges;
            singularRange = NONE;
        }
    }

    public final boolean within(int c) {
        if(c == EOF)
            return containsEOF;
        if(ranges == null)
            return c == singularRange;
        for(int i = 0; i < ranges.length; i += 2) {
            int low = ranges[i];
            if(low <= c) {
                int high = ranges[i + 1];
                if(c <= high) {
                    return true;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * Gets the character of a single-character range.
     * 
     * @return The single range character, possibly {@link SGLR#EOF}, or {@link RangeList#NONE} if not applicable.
     */
    public int getSingularRange() {
        return singularRange;
    }

    /*
     * Returns a char value that can be used for "brute-force" recovery
     */
    public int getFirstRangeElement() {
        return ranges == null ? singularRange : ranges[0];
    }

    public int getLastRangeElement() {
        return ranges == null ? singularRange : ranges[ranges.length - 1];
    }

    @Override public boolean equals(Object obj) {
        if(!(obj instanceof RangeList))
            return false;
        RangeList rangeList = (RangeList) obj;
        return containsEOF == rangeList.containsEOF && singularRange == rangeList.singularRange
            && Arrays.equals(ranges, rangeList.ranges);
    }

    @Override public int hashCode() {
        int result = Objects.hash(containsEOF, singularRange);
        result = 31 * result + Arrays.hashCode(ranges);
        return result;
    }

    @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        if(singularRange != NONE) {
            sb.append(singularRange);
        } else {
            sb.append('[');
            for(int i = 0, end = ranges.length - 1; i < end; i += 2) {
                int low = ranges[i];
                int high = ranges[i + 1];
                sb.append(low);
                if(low != high) {
                    sb.append('-');
                    sb.append(high);
                }
                sb.append(',');
            }
            sb.replace(sb.length() - 1, sb.length(), "]");
        }
        return sb.toString();
    }
}
