package org.spoofax.jsglr.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A series of character ranges.
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class RangeList {
    
    public static final int NONE = -1;
    
    private final int[] ranges;
    
    private final int singularRange;
    
    public RangeList(Range... ranges) {
        if (ranges.length == 1 && ranges[0].low == ranges[0].high) {
            this.ranges = null;
            singularRange = ranges[0].low;
        } else {
            // Assume unsanitized input
            List<Range> sortedRanges = toSortedList(ranges);
            List<Range> sanitizedRanges = mergeOverlap(sortedRanges);
            this.ranges = rangesToArray(sanitizedRanges);
            singularRange = NONE;
        }
    }
    
    private static List<Range> mergeOverlap(List<Range> ranges) {
        for (int i = 0; i < ranges.size(); i++) {
            final Range range = ranges.get(i);            
            final int j = i + 1;
            
            while (j < ranges.size() && ranges.get(j).low <= range.high) {
                if (ranges.get(j).high > range.high)
                   range.high = ranges.get(j).high;
                ranges.remove(j);
            }
        }
        
        return ranges;
    }

    private static List<Range> toSortedList(Range[] ranges) {
        Arrays.sort(ranges);
        List<Range> results = new ArrayList<Range>(ranges.length);
        for (Range range : ranges)
            results.add(range);
        return results;
    }
    
    private static int[] rangesToArray(List<Range> ranges) {
        int[] results = new int[ranges.size() * 2];
        int i = 0;
        
        for (Range range : ranges) {
            results[i++] = range.low;
            results[i++] = range.high;
        }
        
        return results;
    }
    
    public final boolean within(int c) {
        if (singularRange != NONE) return c == singularRange;
        for (int i = 0; i < ranges.length; i += 2) {
            int low = ranges[i];
            if (low <= c) {
                int high = ranges[i + 1];
                if (c <= high) {
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
     * @return  The single range character, or {@link NONE} if not applicable.
     */
    public int getSingularRange() {
        return singularRange;
    }
    
    /*
     * Returns a char value that can be used for "brute-force" recovery
     */
    public int getFirstRangeElement() {
        return singularRange == NONE ? ranges[0] : singularRange;
    }
    
    public int getLastRangeElement() {
        return singularRange == NONE ? ranges[ranges.length - 1] : singularRange;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RangeList))
            return false;
        if (singularRange == NONE) {
            return Arrays.equals(((RangeList) obj).ranges, ranges);
        } else {
            return singularRange == ((RangeList) obj).singularRange;
        }
    }
    
    @Override
    public int hashCode() {
        return singularRange == NONE ? Arrays.hashCode(ranges) : singularRange;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (singularRange != NONE) {
            sb.append(singularRange);
        } else {
            sb.append('[');
            for (int i = 0, end = ranges.length - 1; i < end; i++) {
                int low = ranges[i];
                int high = ranges[i + 1];
                sb.append(low);
                if (low != high) {
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
