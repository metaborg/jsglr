package org.spoofax.jsglr2.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.Function;

public class DisjointRanges {

    public static <E> List<Range> get(E[] elements, Function<E, Range> rangeForElement) {
        // Sort by increasing minima of ranges
        Arrays.sort(elements, (one, two) -> {
            Range oneRange = rangeForElement.apply(one);
            Range twoRange = rangeForElement.apply(two);

            return Integer.compare(oneRange.from, twoRange.from);
        });

        Queue<Integer> mins = new PriorityQueue<>(), maxs = new PriorityQueue<>();

        // Collect unique min and max bounds
        for(E element : elements) {
            int min = rangeForElement.apply(element).from;
            int max = rangeForElement.apply(element).to;

            if(!mins.contains(min))
                mins.add(min);

            if(!maxs.contains(max))
                maxs.add(max);
        }

        List<Range> disjointRanges = new ArrayList<>();
        Range lastRange = null;

        while(!mins.isEmpty() || !maxs.isEmpty()) {
            int newRangeMin, newRangeMax;

            if(!mins.isEmpty() && maxs.peek() >= mins.peek()) {
                newRangeMin = mins.poll();

                if(mins.isEmpty())
                    newRangeMax = maxs.peek();
                else
                    newRangeMax = Math.min(mins.peek() - 1, maxs.peek());

                if(newRangeMax == maxs.peek())
                    maxs.poll();
            } else {
                newRangeMin = lastRange.to + 1;
                newRangeMax = maxs.poll();
            }

            Range range = new Range(newRangeMin, newRangeMax);

            disjointRanges.add(range);

            lastRange = range;
        }

        return disjointRanges;
    }

}
