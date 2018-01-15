package org.spoofax.jsglr2.characterclasses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * ASCII characters: integer representation [0, 255]
 *
 * End-of-file marker (EOF): integer representation 256
 */
public interface ICharacterClass {

    int EOF_INT = 256;

    static final ICharacterClass EOF_SINGLETON = new CharacterClassSingle(EOF_INT);

    boolean contains(int character);

    int min();

    int max();

    static String intToString(int character) {
        if(character == EOF_INT)
            return "EOF";
        else
            return "" + (char) character;
    }

    static boolean isNewLine(int character) {
        return character != EOF_INT && ((char) character) == '\n';
    }

    static Comparator<ICharacterClass> comparator() {
        return (one, two) -> {
            return Integer.compare(one.min(), two.min());
        };
    }

    static boolean disjointSortable(List<ICharacterClass> original) {
        List<ICharacterClass> sorted = new ArrayList<>(original);

        Collections.sort(sorted, comparator());

        return disjointSorted(sorted);
    }

    /*
     * Returns true if each character class only contains characters bigger than the characters in the previous
     * character class.
     */
    static boolean disjointSorted(List<ICharacterClass> characterClasses) {
        for(int i = 0; i < characterClasses.size() - 1; i++) {
            if(characterClasses.get(i).max() >= characterClasses.get(i + 1).min())
                return false;
        }

        return true;
    }

}
