package org.spoofax.jsglr2.imploder;

public enum ImploderVariant {
    TokenizedRecursive, Recursive, RecursiveIncremental, Iterative;

    public static ImploderVariant standard() {
        return TokenizedRecursive;
    }
}
