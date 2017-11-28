package org.spoofax.jsglr2.actions;

import java.util.Arrays;

public class Goto implements IGoto {

    private final int[] productions;
    private final int gotoStateId;

    public Goto(int[] productions, int gotoState) {
        this.productions = productions;
        this.gotoStateId = gotoState;
    }

    @Override public int[] productions() {
        return productions;
    }

    @Override public int gotoStateId() {
        return gotoStateId;
    }

    @Override public String toString() {
        return "GOTO(" + gotoStateId + ")";
    }

    @Override public int hashCode() {
        return (int) productions.hashCode() ^ gotoStateId;
    }

    @Override public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }

        Goto that = (Goto) o;

        return gotoStateId == that.gotoStateId && Arrays.equals(productions, that.productions);
    }

}
