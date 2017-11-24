package org.spoofax.jsglr2.actions;

import org.spoofax.jsglr2.characters.ICharacters;

public final class ActionForCharacterClass {

    public final ICharacters characters;
    public final IAction action;

    public ActionForCharacterClass(ICharacters characters, IAction action) {
        this.characters = characters;
        this.action = action;
    }

    public final boolean appliesTo(int character) {
        return characters.containsCharacter(character);
    }

    @Override public String toString() {
        return characters.toString() + "->" + action.toString();
    }

}
