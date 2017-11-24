package org.spoofax.jsglr2.actions;

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

}
