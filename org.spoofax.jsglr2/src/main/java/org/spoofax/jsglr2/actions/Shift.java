package org.spoofax.jsglr2.actions;

public class Shift implements IShift {

    private final int shiftState;

    public Shift(int shiftState) {
        this.shiftState = shiftState;
    }

    @Override public int shiftState() {
        return shiftState;
    }

    @Override public String toString() {
        return "SHIFT(" + shiftState + ")";
    }

}
