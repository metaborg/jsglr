package org.spoofax.jsglr2.actions;

import org.spoofax.jsglr2.characters.ICharacterClass;

public final class ActionForCharacterClass {

    public final ICharacterClass characterClass;
    public final IAction action;

    public ActionForCharacterClass(ICharacterClass characterClass, IAction action) {
        this.characterClass = characterClass;
        this.action = action;
    }

    public final boolean appliesTo(int character) {
        return characterClass.contains(character);
    }

    @Override public String toString() {
        return characterClass.toString() + "->" + action.toString();
    }

}
