package org.spoofax.jsglr2.actions;

import org.spoofax.jsglr2.characters.ICharacterClass;

public final class ActionsPerCharacterClass {

    public final ICharacterClass characterClass;
    public final IAction[] actions;

    public ActionsPerCharacterClass(ICharacterClass characterClass, IAction[] actions) {
        this.characterClass = characterClass;
        this.actions = actions;
    }

    public final boolean appliesTo(int character) {
        return characterClass.contains(character);
    }

    @Override public String toString() {
        return characterClass.toString() + "->" + actions.toString();
    }

}
