package org.spoofax.jsglr2.characterclasses;

public final class CharacterClassSingle implements ICharacterClass {

    private final int character;

    public CharacterClassSingle(int character) {
        this.character = character;
    }

    public final boolean contains(int character) {
        return this.character == character;
    }

    public int min() {
        return character;
    }

    public int max() {
        return character;
    }

    public final CharacterClassRangeSet rangeSetUnion(CharacterClassRangeSet rangeSet) {
        return rangeSet.addSingle(character);
    }

    @Override public int hashCode() {
        return Integer.hashCode(character);
    }

    @Override public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }

        CharacterClassSingle that = (CharacterClassSingle) o;

        return this.character == that.character;
    }

    @Override public final String toString() {
        return "{" + ICharacterClass.intToString(character) + "}";
    }

}
