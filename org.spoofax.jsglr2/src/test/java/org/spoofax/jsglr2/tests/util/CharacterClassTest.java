package org.spoofax.jsglr2.tests.util;

import static org.junit.Assert.assertEquals;

import java.util.function.Predicate;

import org.junit.Test;
import org.spoofax.jsglr2.characters.ICharacterClassFactory;
import org.spoofax.jsglr2.characters.ICharacters;

public class CharacterClassTest {

    ICharacterClassFactory factory = ICharacters.factory();

    ICharacters AZ = factory.fromRange(65, 90);
    ICharacters az = factory.fromRange(97, 122);

    ICharacters x = factory.fromSingle(120);

    private void testCharacterClass(ICharacters characters, Predicate<Integer> contains) {
        for(int i = 0; i <= 255; i++) {
            assertEquals("Character " + i + " ('" + ICharacters.byteIntToString(i) + "') for characters "
                + characters.toString() + ":", contains.test(i),
                characters.containsCharacter(ICharacters.charToInt((char) i)));
        }
    }

    @Test public void testLowerCaseLettersRange() {
        testCharacterClass(az, character -> {
            return 97 <= character && character <= 122;
        });

        assertEquals(az.containsCharacter(ICharacters.charToInt('a')), true);
        assertEquals(az.containsCharacter(ICharacters.charToInt('A')), false);
    }

    @Test public void testUppercaseCaseLettersRange() {
        testCharacterClass(AZ, character -> {
            return 65 <= character && character <= 90;
        });
    }

    @Test public void testLettersUnionRange() {
        ICharacters letters = factory.union(az, AZ);

        testCharacterClass(letters, character -> {
            return 65 <= character && character <= 90 || 97 <= character && character <= 122;
        });
    }

    @Test public void testSingletonRange() {
        testCharacterClass(x, character -> {
            return character == 120;
        });
    }

    @Test public void testSingletonRangeUnion() {
        ICharacters characters = factory.union(x, AZ);

        testCharacterClass(characters, character -> {
            return 65 <= character && character <= 90 || character == 120;
        });

        assertEquals(characters.containsCharacter(ICharacters.charToInt('a')), false);
        assertEquals(characters.containsCharacter(ICharacters.charToInt('B')), true);
        assertEquals(characters.containsCharacter(ICharacters.charToInt('x')), true);
    }

    @Test public void testEOF() {
        ICharacters characters = factory.fromEOF();

        assertEquals(characters.containsEOF(), true);

        testCharacterClass(characters, character -> {
            return false;
        });
    }

    @Test public void testRangeEOFnion() {
        ICharacters characters = factory.union(az, factory.fromEOF());

        testCharacterClass(characters, character -> {
            return 97 <= character && character <= 122;
        });

        assertEquals(characters.containsEOF(), true);
    }

    @Test public void testNewLineDetection() {
        char newLineChar = '\n';
        int newLineInt = ICharacters.charToInt(newLineChar);

        assertEquals(ICharacters.isNewLine(newLineChar), true);
        assertEquals(ICharacters.isNewLine(newLineInt), true);
    }

}
