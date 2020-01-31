package org.spoofax.jsglr2.parseforest;

public enum ParseForestConstruction {
    Full, Optimized;

    public static ParseForestConstruction standard() {
        return Optimized;
    }
}
