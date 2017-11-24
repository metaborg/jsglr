package org.spoofax.jsglr2.characters;

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

    static ICharacterClassFactory factory() {
        return new CharacterClassFactory(true);
    }

    boolean contains(int character);

    int min();

    int max();

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

    static Comparator<ICharacterClass> comparator() {
        return (one, two) -> {
            if(one.max() < two.min())
                return -1;
            else if(one.min() > two.max())
                return 1;
            else
                return 0;
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
        if(characterClasses.size() <= 1)
            return true;

        for(int i = 0; i < characterClasses.size() - 1; i++) {
            if(comparator().compare(characterClasses.get(i), characterClasses.get(i + 1)) != -1)
                return false;
        }

        return true;
    }

}
