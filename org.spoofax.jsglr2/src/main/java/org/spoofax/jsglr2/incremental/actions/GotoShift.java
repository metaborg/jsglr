package org.spoofax.jsglr2.incremental.actions;

import org.metaborg.parsetable.actions.Shift;

public class GotoShift extends Shift {
    public GotoShift(int shiftStateId) {
        super(shiftStateId);
    }

    @Override public String toString() {
        return "goto_" + super.toString();
    }
}
