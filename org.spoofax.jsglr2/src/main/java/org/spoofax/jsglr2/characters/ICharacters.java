package org.spoofax.jsglr2.characters;

/**
 * ASCII characters: integer representation [0, 255]
 *
 * End-of-file marker (EOF): integer representation 256
 */
public interface ICharacters {

    int EOF_INT = 256;

    static final ICharacters EOF_SINGLETON = new CharactersClassSingle(EOF_INT);

    static CharacterClassFactory factory() {
        return new CharacterClassFactory(true);
    }

    boolean containsCharacter(int character);

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
