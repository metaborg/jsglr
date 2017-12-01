package org.spoofax.jsglr2.actions;

import java.util.Arrays;
import java.util.List;

import org.spoofax.jsglr2.characterclasses.ICharacterClass;

/*
 * Groups a set of actions that are applicable to a single character class. This is the representation that maps
 * one-on-one to the representation from the original ATerm parse table format.
 */
public final class ActionsPerCharacterClass {

    public final ICharacterClass characterClass;
    public final List<IAction> actions;

    public ActionsPerCharacterClass(ICharacterClass characterClass, IAction[] actions) {
        this.characterClass = characterClass;
        this.actions = Arrays.asList(actions);
    }

    public final boolean appliesTo(int character) {
        return characterClass.contains(character);
    }

    @Override
    public String toString() {
        return characterClass.toString() + "->" + actions.toString();
    }

}
