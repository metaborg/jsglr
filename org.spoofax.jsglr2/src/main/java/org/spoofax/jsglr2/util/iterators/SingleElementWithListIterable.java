package org.spoofax.jsglr2.util.iterators;

import java.util.Iterator;
import java.util.List;

public class SingleElementWithListIterable<T> implements Iterable<T> {

    /*
     * TODO: generalize tail argument; move to appropriate place
     */
    public static final <T> Iterable<T> of(T head, List<T> tail) {
        // final Iterator<T> iterator =
        // Stream.concat(Stream.of(head), tail.stream()).iterator();
        //
        // return () -> iterator;

        return new SingleElementWithListIterable<>(head, tail);
    }

    private T element;
    private List<T> list;

    @Deprecated
    public SingleElementWithListIterable(T element, List<T> list) {
        this.element = element;
        this.list = list;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < list.size() + 1;
            }

            @Override
            public T next() {
                cursor++;

                if(cursor == 1)
                    return element;
                else
                    return list.get(cursor - 2);
            }
        };
    }

}
