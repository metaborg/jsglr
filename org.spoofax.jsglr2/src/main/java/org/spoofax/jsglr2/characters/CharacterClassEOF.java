package org.spoofax.jsglr2.characters;

public final class CharacterClassEOF implements ICharacters {

    public CharacterClassEOF() {
    }

    public final boolean containsCharacter(byte character) {
        return false;
    }

    public final boolean containsEOF() {
        return true;
    }

    public final <C extends Number & Comparable<C>> CharacterClassRangeSet<C>
        rangeSetUnion(CharacterClassRangeSet<C> rangeSet) {
        return rangeSet.addSingle(ICharacters.EOF_INT);
    }

    @Override public final String toString() {
        return "{EOF}";
    }

}
