package org.spoofax.jsglr2.actions;

import org.spoofax.jsglr2.characters.ICharacters;

public abstract class Action implements IAction {

    private final ICharacters characters;

    public Action(ICharacters characters) {
        this.characters = characters;
    }

    @Override public ICharacters characters() {
        return characters;
    }

}