package org.spoofax.jsglr2.characters;

public class CharactersEOF implements ICharacters {

    public static final CharactersEOF INSTANCE = new CharactersEOF();

    public boolean containsCharacter(byte character) {
        return false;
    }

    public boolean containsEOF() {
        return true;
    }

    public CharacterClassRangeSet rangeSetUnion(CharacterClassRangeSet rangeSet) {
        return rangeSet.updateEOF(true);
    }

    @Override public String toString() {
        return "[EOF]";
    }

}
