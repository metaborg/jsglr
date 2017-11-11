package org.spoofax.jsglr2.characters;

public class CharactersSingle implements ICharacters {

    private final byte character; // Signed byte with range [-128, 127] representing ASCII [0, 255]

    public CharactersSingle(int character) {
        this.character = (byte) (character - 128);
    }

    public final boolean containsCharacter(byte character) {
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
        return "{" + ICharacters.byteToString(character) + "}";
    }

}
