package org.spoofax.jsglr2.actions;

import org.spoofax.jsglr2.characters.ICharacters;

public interface IAction {

    ActionType actionType();
    
    ICharacters characters();
    
    public default boolean appliesTo(int character) {
        return characters().containsCharacter(character);
    }
    
}
