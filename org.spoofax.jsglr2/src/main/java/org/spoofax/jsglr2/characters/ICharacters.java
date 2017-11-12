package org.spoofax.jsglr2.characters;

/**
 * ASCII characters: integer representation [0, 255]
 *
 * End-of-file marker (EOF): integer representation 256
 */
public interface ICharacters {

    int EOF_INT = 256;

    static RangeSetCharacterClassFactory factory() {
        return new IntegerRangeSetCharacterClassFactory(true);
    }

    static ICharacters eof() {
        return new CharactersSingle(EOF_INT);
    }

    boolean containsCharacter(int character);

    <C extends Number & Comparable<C>> CharacterClassRangeSet<C> rangeSetUnion(CharacterClassRangeSet<C> rangeSet);

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

}
