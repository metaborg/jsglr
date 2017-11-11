package org.spoofax.jsglr2.characters;

public class CharactersEOF implements ICharacters {

    public static final CharactersEOF INSTANCE = new CharactersEOF();

    public boolean containsCharacter(byte character) {
        return false;
    }

    public final boolean containsEOF() {
        return true;
    }

    public final <C extends Number & Comparable<C>> CharacterClassRangeSet<C>
        rangeSetUnion(CharacterClassRangeSet<C> rangeSet) {
        return rangeSet.addEOF();
    }

    @Override public final String toString() {
        return "{EOF}";
    }

}
