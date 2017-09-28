package org.spoofax.jsglr2.util.iterators;

import java.util.Iterator;
import java.util.List;

public class SingleElementWithListIterable<T> implements Iterable<T> {

    private T element;
    private List<T> list;
    
    public SingleElementWithListIterable(T element, List<T> list) {
        this.element = element;
        this.list = list;
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int cursor = 0;
            
            public boolean hasNext() {
                return cursor < list.size() + 1;
            }

            public T next() {
                cursor++;
                
                if (cursor == 1)
                    return element;
                else
                    return list.get(cursor - 2);
            }
        };
    }
    
}
