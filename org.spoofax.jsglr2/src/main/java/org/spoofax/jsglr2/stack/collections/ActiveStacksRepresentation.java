package org.spoofax.jsglr2.stack.collections;

public enum ActiveStacksRepresentation {
    ArrayList, ArrayListHashMap, LinkedHashMap;

    public static ActiveStacksRepresentation standard() {
        return ArrayList;
    }
}
