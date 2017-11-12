package org.spoofax.jsglr2.characters;

public abstract class CharacterClassFactory implements ICharacterClassFactory {

    @Override public final ICharacters fromSingle(int character) {
        return new CharactersSingle(character);
    }

}
