package org.spoofax.jsglr2.characters;

public final class CharactersSingle implements ICharacters {

    private final int character; // Signed byte with range representing ASCII [0, 255] \/ EOF [256]

    public CharactersSingle(int character) {
        this.character = character;
    }

    public final boolean containsCharacter(int character) {
        return this.character == character;
    }

    public final <C extends Number & Comparable<C>> CharacterClassRangeSet<C>
        rangeSetUnion(CharacterClassRangeSet<C> rangeSet) {
        return rangeSet.addSingle(character);
    }

    @Override public final String toString() {
        return "{" + ICharacters.intToString(character) + "}";
    }

}
