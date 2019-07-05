package org.spoofax.jsglr2.tokens;

public enum TokenizerVariant {
    Null, Recursive, Iterative;

    public static TokenizerVariant standard() {
        return Null;
    }
}
