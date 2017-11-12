package org.spoofax.jsglr2.characters;

public final class CharactersClassSingle implements ICharacters {

    private final int containsCharacter;

    public CharactersClassSingle(int containsCharacter) {
        this.containsCharacter = containsCharacter - 128;
    }

    public final boolean containsCharacter(int character) {
        return containsCharacter == character;
    }

    public final boolean containsEOF() {
        return false;
    }

    public final <C extends Number & Comparable<C>> CharacterClassRangeSet<C>
        rangeSetUnion(CharacterClassRangeSet<C> rangeSet) {
        return rangeSet.addSingle(((int) containsCharacter) + 128);
    }

    @Override public final String toString() {
        return "{EOF}";
    }

}
