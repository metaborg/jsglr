package org.spoofax.jsglr2.characters;

public interface ICharacterClassFactory {

    ICharacters fromEmpty();

    ICharacters fromSingle(int character);

    ICharacters fromRange(int from, int to);

    ICharacters fromEOF();

    ICharacters union(ICharacters one, ICharacters two);

}
