package org.spoofax.jsglr2.reducing;

public enum Reducing {
    Basic, Elkhound, DataDependent;

    public static Reducing standard() {
        return Elkhound;
    }
}