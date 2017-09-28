package org.spoofax.jsglr2.util.iterators;

import java.util.Iterator;

public class SingleElementIterable<T> implements Iterable<T> {

    private T element;
    
    public SingleElementIterable(T element) {
        this.element = element;
    }
    
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private boolean hasNext = true;
            
            public boolean hasNext() {
                return hasNext;
            }
            
            public T next() {
                hasNext = false;
                
                return element;
            }
        };
    }
    
}
