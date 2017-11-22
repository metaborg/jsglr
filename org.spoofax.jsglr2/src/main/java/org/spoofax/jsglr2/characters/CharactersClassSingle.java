package org.spoofax.jsglr2.characters;

public final class CharactersClassSingle implements ICharacters {

    private final int containsCharacter;

    public CharactersClassSingle(int containsCharacter) {
        this.containsCharacter = containsCharacter;
    }

    public final boolean containsCharacter(int character) {
        return containsCharacter == character;
    }

    public final CharacterClassRangeSet rangeSetUnion(CharacterClassRangeSet rangeSet) {
        return rangeSet.addSingle(containsCharacter);
    }

    @Override public int hashCode() {
        return Integer.hashCode(containsCharacter);
    }

    @Override public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }

        CharactersClassSingle that = (CharactersClassSingle) o;

        return containsCharacter == that.containsCharacter;
    }

    @Override public final String toString() {
        return "{" + ICharacters.intToString(containsCharacter) + "}";
    }

}
