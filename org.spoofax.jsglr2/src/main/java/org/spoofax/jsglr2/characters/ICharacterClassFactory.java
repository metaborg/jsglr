package org.spoofax.jsglr2.characters;

public interface ICharacterClassFactory {

    ICharacters fromEmpty();

    ICharacters fromSingle(int character);

    ICharacters fromRange(int from, int to);

    ICharacters union(ICharacters one, ICharacters two);

    /*
     * Character classes in the parse table can be composed by taking the union of multiple character classes. This
     * method is called after these operations on the result, e.g. to do optimizations.
     */
    ICharacters finalize(ICharacters characters);

}
