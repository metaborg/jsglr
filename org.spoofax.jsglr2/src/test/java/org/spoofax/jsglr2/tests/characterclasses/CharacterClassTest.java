package org.spoofax.jsglr2.tests.characterclasses;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.Test;
import org.metaborg.characterclasses.CharacterClassFactory;
import org.metaborg.characterclasses.ICharacterClassFactory;
import org.metaborg.parsetable.characterclasses.ICharacterClass;

public class CharacterClassTest {

    ICharacterClassFactory factory = new CharacterClassFactory(true, true);

    ICharacterClass AZ = factory.fromRange(65, 90);
    ICharacterClass az = factory.fromRange(97, 122);

    ICharacterClass x = factory.fromSingle(120);
    ICharacterClass eof = factory.fromSingle(CharacterClassFactory.EOF_INT);

    private void testCharacterClass(ICharacterClass characters, Predicate<Integer> contains) {
        for(int i = 0; i <= CharacterClassFactory.EOF_INT; i++) {
            boolean expected = characters.contains(i);

            assertEquals("Character " + i + " ('" + CharacterClassFactory.intToString(i) + "') for characters "
                + characters.toString() + ":", contains.test(i), expected);
        }
    }

    private void testCharacterClass(ICharacterClass one, ICharacterClass two) {
        for(int i = 0; i <= CharacterClassFactory.EOF_INT; i++) {
            boolean expected = one.contains(i);
            boolean actual = two.contains(i);

            assertEquals("Character " + i + " ('" + CharacterClassFactory.intToString(i) + "') for characters "
                + one.toString() + " vs. " + two.toString() + ":", actual, expected);
        }
    }

    @Test
    public void testLowerCaseLettersRange() {
        testCharacterClass(az, character -> {
            return 97 <= character && character <= 122;
        });

        assertEquals(az.contains('a'), true);
        assertEquals(az.contains('A'), false);
    }

    @Test
    public void testUppercaseCaseLettersRange() {
        testCharacterClass(AZ, character -> {
            return 65 <= character && character <= 90;
        });
    }

    @Test
    public void testLettersUnionRange() {
        ICharacterClass letters = factory.union(az, AZ);

        testCharacterClass(letters, character -> {
            return 65 <= character && character <= 90 || 97 <= character && character <= 122;
        });
    }

    @Test
    public void testLettersIntersectionRange() {
        ICharacterClass letters = factory.intersection(az, AZ);

        testCharacterClass(letters, factory.fromEmpty());
    }

    @Test
    public void testLettersDifferenceRange() {
        ICharacterClass letters = factory.intersection(az, AZ);

        testCharacterClass(letters, factory.fromEmpty());
    }

    @Test
    public void testSingletonRange() {
        testCharacterClass(x, character -> {
            return character == 120;
        });
    }

    @Test
    public void testSingletonRangeUnion() {
        ICharacterClass characters = factory.union(x, AZ);

        testCharacterClass(characters, character -> {
            return 65 <= character && character <= 90 || character == 120;
        });

        assertEquals(characters.contains('a'), false);
        assertEquals(characters.contains('B'), true);
        assertEquals(characters.contains('x'), true);
    }

    @Test
    public void testEOF() {
        ICharacterClass characters = factory.fromSingle(CharacterClassFactory.EOF_INT);

        testCharacterClass(characters, character -> {
            return character == CharacterClassFactory.EOF_INT;
        });
    }

    @Test
    public void testRangeEOFunion() {
        ICharacterClass characters = factory.union(az, factory.fromSingle(CharacterClassFactory.EOF_INT));

        testCharacterClass(characters, character -> {
            return 97 <= character && character <= 122 || character == CharacterClassFactory.EOF_INT;
        });
    }

    @Test
    public void testRangeEOFintersect() {
        testCharacterClass(factory.intersection(factory.union(az, eof), eof), eof);
        testCharacterClass(factory.intersection(eof, factory.union(az, eof)), eof);
    }

    @Test
    public void testRangeEOFdifference() {
        testCharacterClass(factory.difference(factory.union(az, eof), eof), az);
        testCharacterClass(factory.difference(eof, factory.union(az, eof)), factory.fromEmpty());
    }

    @Test
    public void testRangeIntersect() {
        testCharacterClass(factory.intersection(factory.fromRange(10, 20), factory.fromSingle(15)),
            factory.fromSingle(15));
        testCharacterClass(factory.intersection(factory.fromSingle(15), factory.fromRange(10, 20)),
            factory.fromSingle(15));

        testCharacterClass(factory.intersection(factory.fromRange(10, 20), factory.fromRange(15, 25)),
            factory.fromRange(15, 20));

        testCharacterClass(factory.intersection(factory.fromRange(10, 20), factory.fromRange(20, 30)),
            factory.fromSingle(20));
        testCharacterClass(factory.intersection(factory.fromRange(10, 20), factory.fromSingle(20)),
            factory.fromSingle(20));
        testCharacterClass(factory.intersection(factory.fromSingle(20), factory.fromRange(10, 20)),
            factory.fromSingle(20));
    }

    @Test
    public void testRangeDifference() {
        testCharacterClass(factory.difference(factory.fromRange(65, 75), factory.fromSingle(70)),
            factory.union(factory.fromRange(65, 69), factory.fromRange(71, 75)));
        testCharacterClass(factory.difference(factory.fromSingle(15), factory.fromRange(10, 20)), factory.fromEmpty());

        testCharacterClass(factory.difference(factory.fromRange(65, 70), factory.fromSingle(70)),
            factory.fromRange(65, 69));
    }

    @Test
    public void testNewLineDetection() {
        char newLineChar = '\n';
        int newLineInt = newLineChar;

        assertEquals(CharacterClassFactory.isNewLine(newLineChar), true);
        assertEquals(CharacterClassFactory.isNewLine(newLineInt), true);
    }

    @Test
    public void testComparisons() {
        assertEquals(ICharacterClass.comparator().compare(AZ, az), -1);
        assertEquals(ICharacterClass.comparator().compare(az, AZ), 1);
        assertEquals(ICharacterClass.comparator().compare(az, az), 0);
        assertEquals(ICharacterClass.comparator().compare(AZ, AZ), 0);
        assertEquals(ICharacterClass.comparator().compare(AZ, x), -1);
        assertEquals(ICharacterClass.comparator().compare(x, AZ), 1);
        assertEquals(ICharacterClass.comparator().compare(x, az), 1);
        assertEquals(ICharacterClass.comparator().compare(az, x), -1);
        assertEquals(ICharacterClass.comparator().compare(az, eof), -1);
        assertEquals(ICharacterClass.comparator().compare(eof, az), 1);
    }

    @Test
    public void testDisjointSortanble() {
        assertEquals(ICharacterClass.disjointSorted(Arrays.asList(AZ, az)), true);
        assertEquals(ICharacterClass.disjointSorted(Arrays.asList(az, AZ)), false);
        assertEquals(ICharacterClass.disjointSorted(Arrays.asList(AZ, AZ)), false);

        assertEquals(ICharacterClass.disjointSortable(Arrays.asList(AZ, az)), true);
        assertEquals(ICharacterClass.disjointSortable(Arrays.asList(az, AZ)), true);
        assertEquals(ICharacterClass.disjointSortable(Arrays.asList(AZ, AZ)), false);

        assertEquals(ICharacterClass.disjointSortable(Arrays.asList(az, x)), false);
        assertEquals(ICharacterClass.disjointSortable(Arrays.asList(az, AZ, eof)), true);
    }

}