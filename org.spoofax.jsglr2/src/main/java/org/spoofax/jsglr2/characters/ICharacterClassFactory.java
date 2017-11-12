package org.spoofax.jsglr2.characters;

public interface ICharacterClassFactory {

    ICharacters fromEmpty();

    ICharacters fromSingle(int character);

    ICharacters fromRange(int from, int to);

    ICharacters union(ICharacters one, ICharacters two);

    ICharacters optimize(ICharacters characters);

}
