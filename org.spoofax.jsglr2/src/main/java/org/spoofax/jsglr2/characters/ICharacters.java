package org.spoofax.jsglr2.characters;

/**
 * ASCII characters: integer representation [0, 255], byte representation [-128, 127]
 *
 * End-of-file marker (EOF): integer representation 256, byte presentation n.a.
 */
public interface ICharacters {

    /*
     * Constructing character classes uses integer representation [0, 255], checking character membership in a class
     * uses byte representation [-128, 127]
     */

    int EOF_INT = 256;

    static RangeSetCharacterClassFactory factory() {
        return new ByteRangeSetCharacterClassFactory(true);
    }

    static CharactersEOF eof() {
        return CharactersEOF.INSTANCE;
    }

    boolean containsCharacter(int character);

    boolean containsEOF();

    <C extends Number & Comparable<C>> CharacterClassRangeSet<C> rangeSetUnion(CharacterClassRangeSet<C> rangeSet);

    static int charToInt(char c) {
        return c - 128;
    }

    static String byteIntToString(int i) {
        return "" + (char) (i + 128);
    }

    static String intToString(int character) {
        if(character == EOF_INT) {
            return "EOF";
        } else {
            return "" + (char) character;
        }
    }

    static boolean isNewLine(int character) {
        return character != EOF_INT && (char) character == '\n';
    }

    static boolean isNewLine(byte character) {
        return (char) (((int) character) + 128) == '\n';
    }

}
