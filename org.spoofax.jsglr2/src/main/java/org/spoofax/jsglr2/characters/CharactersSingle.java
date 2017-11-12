package org.spoofax.jsglr2.characters;

public final class CharactersSingle implements ICharacters {

    private final int character; // Signed byte with range [-128, 127] representing ASCII [0, 255]

    public CharactersSingle(int character) {
        this.character = character - 128;
    }

    public final boolean containsCharacter(int character) {
        return this.character == character;
    }

    public final boolean containsEOF() {
        return false;
    }

    public final <C extends Number & Comparable<C>> CharacterClassRangeSet<C>
        rangeSetUnion(CharacterClassRangeSet<C> rangeSet) {
        return rangeSet.addSingle(character);
    }

    @Override public final String toString() {
        return "{" + ICharacters.byteIntToString(character) + "}";
    }

}
