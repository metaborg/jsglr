package org.spoofax.jsglr2.util;

import java.util.HashMap;
import java.util.Map;

public class Cache<E> {

    private final Map<E, E> cache;

    public Cache() {
        this.cache = new HashMap<>();
    }

    public E cached(E element) {
        E cached = cache.get(element);

        if(cached != null)
            return cached;
        else {
            cache.put(element, element);

            return element;
        }
    }

}
