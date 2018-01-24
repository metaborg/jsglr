package org.spoofax.jsglr2.actions;

import org.metaborg.parsetable.actions.IShift;

public class Shift implements IShift {

    private final int shiftStateId;

    public Shift(int shiftStateId) {
        this.shiftStateId = shiftStateId;
    }

    @Override
    public final int shiftStateId() {
        return shiftStateId;
    }

    @Override
    public String toString() {
        return "SHIFT(" + shiftStateId + ")";
    }

    @Override
    public int hashCode() {
        return shiftStateId;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }

        Shift that = (Shift) o;

        return shiftStateId == that.shiftStateId;
    }

}
