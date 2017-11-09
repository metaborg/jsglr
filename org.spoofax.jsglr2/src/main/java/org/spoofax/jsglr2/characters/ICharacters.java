package org.spoofax.jsglr2.characters;

/**
 * Index 0 - 255: ASCII characters
 *
 * Index 256: End-of-file marker (EOF)
 *
 * TODO: represent EOF as a constant ICharacters instead of an int?
 */
public interface ICharacters {

    int EOF = 256;

    static CharacterClassFactory factory() {
        return CharacterClassFactory.INSTANCE;
    }

    boolean containsCharacter(int character);

    static String charToString(int character) {
        if(character == EOF) {
            return "EOF";
        } else {
            return String.valueOf(character);
        }
    }

    static boolean isNewLine(int character) {
        return character != EOF && (char) character == '\n';
    }

}
