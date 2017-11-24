package org.spoofax.jsglr2.characters;

/**
 * ASCII characters: integer representation [0, 255]
 *
 * End-of-file marker (EOF): integer representation 256
 */
public interface ICharacterClass {

    int EOF_INT = 256;

    static final ICharacterClass EOF_SINGLETON = new CharacterClassSingle(EOF_INT);

    static ICharacterClassFactory factory() {
        return new CharacterClassFactory(true);
    }

    boolean contains(int character);

    CharacterClassRangeSet rangeSetUnion(CharacterClassRangeSet rangeSet);

    static String intToString(int character) {
        if(character == EOF_INT)
            return "EOF";
        else
            return "" + (char) character;
    }

    static boolean isNewLine(int character) {
        return character != EOF_INT && ((char) character) == '\n';
    }

}
