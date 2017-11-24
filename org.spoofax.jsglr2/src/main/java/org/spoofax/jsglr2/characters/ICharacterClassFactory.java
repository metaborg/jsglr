package org.spoofax.jsglr2.characters;

public interface ICharacterClassFactory {

    ICharacterClass fromEmpty();

    ICharacterClass fromSingle(int character);

    ICharacterClass fromRange(int from, int to);

    ICharacterClass union(ICharacterClass one, ICharacterClass two);

    /*
     * Character classes in the parse table can be composed by taking the union of multiple character classes. This
     * method is called after these operations on the result, e.g. to do optimizations.
     */
    ICharacterClass finalize(ICharacterClass characterClass);

}
