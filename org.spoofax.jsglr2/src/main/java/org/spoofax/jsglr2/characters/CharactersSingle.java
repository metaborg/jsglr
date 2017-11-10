package org.spoofax.jsglr2.characters;

import com.google.common.collect.Range;

public class CharactersSingle implements ICharacters {

    private byte character; // Signed byte with range [-128, 127] representing ASCII [0, 255]

    public CharactersSingle(int character) {
        this.character = (byte) (character - 128);
    }

    public boolean containsCharacter(byte character) {
        return this.character == character;
    }

    public boolean containsEOF() {
        return false;
    }

    public CharacterClassRangeSet rangeSetUnion(CharacterClassRangeSet rangeSet) {
        return rangeSet.updateRangeSet(ranges -> ranges.add(Range.singleton(character)));
    }

    @Override public String toString() {
        return "{" + ICharacters.byteToString(character) + "}";
    }

}
