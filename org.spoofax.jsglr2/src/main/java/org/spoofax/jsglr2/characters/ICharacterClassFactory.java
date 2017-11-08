package org.spoofax.jsglr2.characters;

public interface ICharacterClassFactory<T extends ICharacters> {

  T fromEmpty();

  T fromSingle(int character);

  T fromRange(int from, int to);

  T union(ICharacters one, ICharacters two);

}
