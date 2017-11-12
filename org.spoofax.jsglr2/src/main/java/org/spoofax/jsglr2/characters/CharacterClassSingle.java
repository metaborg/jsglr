package org.spoofax.jsglr2.characters;

public final class CharacterClassSingle implements ICharacters {

    private final int containsCharacter; // [-128, 128]

    public CharacterClassSingle(int containsCharacter) {
        this.containsCharacter = containsCharacter - 128;
    }

    public final boolean containsCharacter(byte character) {
        return containsCharacter == character;
    }

    public final boolean containsEOF() {
        return containsCharacter + 128 == ICharacters.EOF_INT;
    }

    public final <C extends Number & Comparable<C>> CharacterClassRangeSet<C>
        rangeSetUnion(CharacterClassRangeSet<C> rangeSet) {
        return rangeSet.addSingle(containsCharacter + 128);
    }

    @Override public final String toString() {
        return "{..optimized..}";
    }

}
