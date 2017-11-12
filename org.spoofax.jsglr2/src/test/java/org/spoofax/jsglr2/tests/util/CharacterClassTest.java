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
        for(int i = 0; i <= ICharacters.EOF_INT; i++) {
            boolean expected = i < ICharacters.EOF_INT
                ? characters.containsCharacter(ICharacters.charToNumber((char) i)) : characters.containsEOF();

            assertEquals("Character " + i + " ('" + ICharacters.intToString(i) + "') for characters "
                + characters.toString() + ":", contains.test(i), expected);
        }
    }

    @Test public void testLowerCaseLettersRange() {
        testCharacterClass(az, character -> {
            return 97 <= character && character <= 122;
        });

        assertEquals(az.containsCharacter(ICharacters.charToNumber('a')), true);
        assertEquals(az.containsCharacter(ICharacters.charToNumber('A')), false);
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

        assertEquals(characters.containsCharacter(ICharacters.charToNumber('a')), false);
        assertEquals(characters.containsCharacter(ICharacters.charToNumber('B')), true);
        assertEquals(characters.containsCharacter(ICharacters.charToNumber('x')), true);
    }

    @Test public void testEOF() {
        ICharacters characters = factory.fromSingle(ICharacters.EOF_INT);

        testCharacterClass(characters, character -> {
            return character == ICharacters.EOF_INT;
        });
    }

    @Test public void testRangeEOFunion() {
        ICharacters characters = factory.union(az, factory.fromSingle(ICharacters.EOF_INT));

        testCharacterClass(characters, character -> {
            return 97 <= character && character <= 122 || character == ICharacters.EOF_INT;
        });
    }

    @Test public void testNewLineDetection() {
        char newLineChar = '\n';
        int newLineInt = newLineChar;

        assertEquals(ICharacters.isNewLine(newLineChar), true);
        assertEquals(ICharacters.isNewLine(newLineInt), true);

        assertEquals(ICharacters.isNumberNewLine(ICharacters.charToNumber(newLineChar)), true);
        assertEquals(ICharacters.isNumberNewLine(ICharacters.charToNumber((char) newLineInt)), true);
    }

}
