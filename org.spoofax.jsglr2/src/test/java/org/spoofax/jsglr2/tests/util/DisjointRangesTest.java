package org.spoofax.jsglr2.tests.util;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.junit.Test;
import org.spoofax.jsglr2.characterclasses.ICharacterClass;
import org.spoofax.jsglr2.characterclasses.ICharacterClassFactory;
import org.spoofax.jsglr2.util.DisjointRanges;
import org.spoofax.jsglr2.util.Range;

public class DisjointRangesTest {

    ICharacterClassFactory factory = ICharacterClass.factory();

    ICharacterClass AZ = factory.fromRange(65, 90);
    ICharacterClass az = factory.fromRange(97, 122);
    ICharacterClass by = factory.fromRange(98, 121);

    ICharacterClass AE = factory.fromRange(65, 69);
    ICharacterClass CG = factory.fromRange(67, 71);

    ICharacterClass x = factory.fromSingle(120);
    ICharacterClass eof = factory.fromSingle(ICharacterClass.EOF_INT);

    @Test public void testDisjointRanges() {
        assertEquals(disjointCharacterClassRanges(az, AZ), Arrays.asList(range(65, 90), range(97, 122)));
        assertEquals(disjointCharacterClassRanges(AZ, az), Arrays.asList(range(65, 90), range(97, 122)));
    }

    @Test public void testDisjointRangeAndSingle() {
        assertEquals(disjointCharacterClassRanges(az, eof),
            Arrays.asList(range(97, 122), range(ICharacterClass.EOF_INT, ICharacterClass.EOF_INT)));
        assertEquals(disjointCharacterClassRanges(eof, az),
            Arrays.asList(range(97, 122), range(ICharacterClass.EOF_INT, ICharacterClass.EOF_INT)));
    }

    @Test public void testOneOverlappedBySingle() {
        assertEquals(disjointCharacterClassRanges(az, x),
            Arrays.asList(range(97, 119), range(120, 120), range(121, 122)));
        assertEquals(disjointCharacterClassRanges(x, az),
            Arrays.asList(range(97, 119), range(120, 120), range(121, 122)));
    }

    @Test public void testOverlappingRange() {
        assertEquals(disjointCharacterClassRanges(az, by),
            Arrays.asList(range(97, 97), range(98, 121), range(122, 122)));
        assertEquals(disjointCharacterClassRanges(by, az),
            Arrays.asList(range(97, 97), range(98, 121), range(122, 122)));
    }

    @Test public void testTwoPartlyOverlapping() {
        assertEquals(disjointCharacterClassRanges(AE, CG), Arrays.asList(range(65, 66), range(67, 69), range(70, 71)));
        assertEquals(disjointCharacterClassRanges(CG, AE), Arrays.asList(range(65, 66), range(67, 69), range(70, 71)));
    }

    @Test public void testTwoEqualStart() {
        assertEquals(disjointCharacterClassRanges(factory.fromRange(10, 30), factory.fromRange(10, 20)),
            Arrays.asList(range(10, 20), range(21, 30)));
    }

    @Test public void testHigherMaxThanMinIntermediate() {
        // This example fires the max < min branch in the !mins.isEmpty() branch of DisjointRanges::get
        
        assertEquals(
            disjointCharacterClassRanges(factory.fromRange(10, 20), factory.fromRange(15, 15), factory.fromSingle(30)),
            Arrays.asList(range(10, 14), range(15, 15), range(16, 20), range(30, 30)));
    }

    @Test public void testThree() {
        assertEquals(
            disjointCharacterClassRanges(factory.fromRange(10, 30), factory.fromRange(20, 40),
                factory.fromRange(15, 35)),
            Arrays.asList(range(10, 14), range(15, 19), range(20, 30), range(31, 35), range(36, 40)));
    }

    private List<Range> disjointCharacterClassRanges(ICharacterClass... characterClasses) {
        return DisjointRanges.get(characterClasses, characterClassRange);
    }

    private Function<ICharacterClass, Range> characterClassRange = (characterClass) -> {
        return range(characterClass.min(), characterClass.max());
    };

    private Range range(int from, int to) {
        return new Range(from, to);
    }

}
