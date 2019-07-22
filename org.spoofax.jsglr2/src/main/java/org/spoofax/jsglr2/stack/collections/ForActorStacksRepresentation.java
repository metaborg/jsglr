package org.spoofax.jsglr2.stack.collections;

public enum ForActorStacksRepresentation {
    ArrayDeque, LinkedHashMap;

    public static ForActorStacksRepresentation standard() {
        return ArrayDeque;
    }
}
