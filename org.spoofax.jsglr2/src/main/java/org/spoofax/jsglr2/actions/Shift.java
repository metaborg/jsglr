package org.spoofax.jsglr2.actions;

public class Shift implements IShift {

    private final int shiftStateId;

    public Shift(int shiftStateId) {
        this.shiftStateId = shiftStateId;
    }

    @Override public int shiftStateId() {
        return shiftStateId;
    }

    @Override public String toString() {
        return "SHIFT(" + shiftStateId + ")";
    }

}
