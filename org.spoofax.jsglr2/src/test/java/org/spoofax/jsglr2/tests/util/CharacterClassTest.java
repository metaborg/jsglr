package org.spoofax.jsglr2.tests.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.spoofax.jsglr2.characters.ICharacterClassFactory;
import org.spoofax.jsglr2.characters.ICharacters;

public class CharacterClassTest {

    ICharacterClassFactory factory = ICharacters.factory();

    ICharacters AZ = factory.fromRange(65, 90);
    ICharacters az = factory.fromRange(97, 122);

    ICharacters x = factory.fromSingle(120);

    @Test public void testLowerCaseLettersRange() {
        for(int i = 0; i <= 255; i++) {
            assertEquals(az.containsCharacter(ICharacters.charToByte((char) i)), 97 <= i && i <= 122);
        }

        assertEquals(az.containsCharacter(ICharacters.charToByte('a')), true);
        assertEquals(az.containsCharacter(ICharacters.charToByte('A')), false);
    }

    @Test public void testUppercaseCaseLettersRange() {
        for(int i = 0; i <= 255; i++) {
            assertEquals(AZ.containsCharacter(ICharacters.charToByte((char) i)), 65 <= i && i <= 90);
        }
    }

    @Test public void testLettersUnionRange() {
        for(int i = 0; i <= 255; i++) {
            ICharacters letters = factory.union(az, AZ);

            assertEquals(letters.containsCharacter(ICharacters.charToByte((char) i)),
                65 <= i && i <= 90 || 97 <= i && i <= 122);
        }
    }

    @Test public void testSingletonRange() {
        for(int i = 0; i <= 255; i++) {
            assertEquals(x.containsCharacter(ICharacters.charToByte((char) i)), i == 120);
        }
    }

    @Test public void testSingletonRangeUnion() {
        ICharacters characters = factory.union(x, AZ);

        for(int i = 0; i <= 255; i++) {
            assertEquals(characters.containsCharacter(ICharacters.charToByte((char) i)),
                65 <= i && i <= 90 || i == 120);
        }

        assertEquals(characters.containsCharacter(ICharacters.charToByte('a')), false);
        assertEquals(characters.containsCharacter(ICharacters.charToByte('B')), true);
        assertEquals(characters.containsCharacter(ICharacters.charToByte('x')), true);
    }

    @Test public void testEOF() {
        ICharacters characters = factory.fromEOF();

        assertEquals(characters.containsEOF(), true);

        for(int i = 0; i <= 255; i++) {
            assertEquals(characters.containsCharacter(ICharacters.charToByte((char) i)), false);
        }
    }

    @Test public void testRangeEOnion() {
        ICharacters characters = factory.union(az, factory.fromEOF());

        for(int i = 0; i <= 255; i++) {
            assertEquals(characters.containsCharacter(ICharacters.charToByte((char) i)), 97 <= i && i <= 122);
        }

        assertEquals(characters.containsEOF(), true);
    }

    @Test public void testNewLineDetection() {
        char newLineChar = '\n';
        byte newLineByte = ICharacters.charToByte(newLineChar);

        assertEquals(ICharacters.isNewLine(newLineChar), true);
        assertEquals(ICharacters.isNewLine(newLineByte), true);
    }

}
