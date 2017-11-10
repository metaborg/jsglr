package org.spoofax.jsglr2.actions;

import org.spoofax.jsglr2.characters.ICharacters;

public class Shift extends Action implements IShift {

    private final int shiftState;

    public Shift(ICharacters characters, int shiftState) {
        super(characters);

        this.shiftState = shiftState;
    }

    @Override public int shiftState() {
        return shiftState;
    }

}
