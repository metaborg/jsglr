package org.spoofax.jsglr2.actions;

import org.spoofax.jsglr2.characterclasses.ICharacterClass;

/*
 * Basically a tuple of a character class and an action.
 */
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
