package org.spoofax.jsglr2.characters;

/**
 * ASCII characters: integer representation [0, 255], internal representation [-128, 127]
 *
 * End-of-file marker (EOF): integer representation 256, internal representation n.a. (i.e. implemented with a boolean
 * flag)
 */
public interface ICharacters {

    /*
     * Constructing character classes uses integer representation [0, 255], checking character membership in a class
     * uses byte representation [-128, 127]
     */

    int EOF_INT = 256;

    static RangeSetCharacterClassFactory factory() {
        return new IntegerRangeSetCharacterClassFactory(true);
    }

    boolean containsCharacter(int character);

    boolean containsEOF();

    <C extends Number & Comparable<C>> CharacterClassRangeSet<C> rangeSetUnion(CharacterClassRangeSet<C> rangeSet);

    static int charToNumber(char c) {
        return ((int) c) - 128;
    }

    static char numberToChar(int i) {
        return (char) (i + 128);
    }

    static String intToString(int character) {
        if(character == EOF_INT) {
            return "EOF";
        } else {
            return "" + (char) character;
        }
    }

    static boolean isNewLine(int character) {
        return character != EOF_INT && ((char) character) == '\n';
    }

    static boolean isNumberNewLine(int character) {
        return numberToChar(character) == '\n';
    }

}
