package org.spoofax.jsglr2.stack;

public enum StackRepresentation {
    Basic, Hybrid, BasicElkhound, HybridElkhound;

    public static StackRepresentation standard() {
        return HybridElkhound;
    }
}