package org.spoofax.jsglr2.actions;

import org.spoofax.jsglr2.characters.ICharacters;

public final class ActionsPerCharacterClass {

    public final ICharacters characters;
    public final IAction[] actions;

    public ActionsPerCharacterClass(ICharacters characters, IAction[] actions) {
        this.characters = characters;
        this.actions = actions;
    }

    public final boolean appliesTo(int character) {
        return characters.containsCharacter(character);
    }

    @Override public String toString() {
        return characters.toString() + "->" + actions.toString();
    }

}
