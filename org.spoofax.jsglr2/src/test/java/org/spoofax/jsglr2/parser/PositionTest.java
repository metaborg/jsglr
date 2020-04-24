package org.spoofax.jsglr2.parser;

import static org.junit.Assert.assertEquals;
import static org.spoofax.jsglr2.parser.Position.START_POSITION;

import org.junit.Test;

public class PositionTest {

    public static final String SMILEY_STRING = "😀";
    public static final int SMILEY_CODEPOINT = SMILEY_STRING.codePointAt(0);

    @Test(expected = StringIndexOutOfBoundsException.class) public void outOfBounds() {
        test("", 1, 1, 2);
    }

    @Test public void empty() {
        test("", 0, 1, 1);
    }

    @Test public void singleLine() {
        test("foo", 0, 1, 1);
        test("foo", 1, 1, 2);
        test("foo", 2, 1, 3);
        test("foo", 3, 1, 4);
    }

    @Test public void doubleLine() {
        test("foo\nbar", 3, 1, 4);
        test("foo\nbar", 4, 2, 1);
        test("foo\nbar", 5, 2, 2);
        test("foo\nbar", 6, 2, 3);
        test("foo\nbar", 7, 2, 4);
    }

    @Test public void newLine() {
        test("\n", 0, 1, 1);
        test("\n", 1, 2, 1);
    }

    @Test public void endByNewLine() {
        test("foo\n", 0, 1, 1);
        test("foo\n", 1, 1, 2);
        test("foo\n", 2, 1, 3);
        test("foo\n", 3, 1, 4);
        test("foo\n", 4, 2, 1);
    }

    @Test public void unicode() {
        // The 😀 character takes up two `char`s, which do count towards the offset but do not count towards the column
        test("😀", 0, 1, 1);
        test("😀", 1, 1, 2);
        test("😀", 2, 1, 2);
        test("a😀b\na😀b", 0, 1, 1);
        test("a😀b\na😀b", 1, 1, 2);
        test("a😀b\na😀b", 2, 1, 3);
        test("a😀b\na😀b", 3, 1, 3);
        test("a😀b\na😀b", 4, 1, 4);
        test("a😀b\na😀b", 5, 2, 1);
        test("a😀b\na😀b", 6, 2, 2);
        test("a😀b\na😀b", 7, 2, 3);
        test("a😀b\na😀b", 8, 2, 3);
        test("a😀b\na😀b", 9, 2, 4);
    }

    private static void test(String string, int offset, int line, int column) {
        assertEquals(new Position(offset, line, column), Position.atOffset(string, offset));
    }

    @Test public void testNext() {
        assertEquals(new Position(1, 1, 2), START_POSITION.next('a'));
        assertEquals(new Position(1, 2, 1), START_POSITION.next('\n'));
        assertEquals(new Position(2, 1, 2), START_POSITION.next(SMILEY_CODEPOINT));

        assertEquals(new Position(1, 2, 1), START_POSITION.nextLine());
    }

    @Test public void testStep() {
        assertEquals(new Position(1, 1, 2), START_POSITION.step("a", 1));
        assertEquals(new Position(1, 2, 1), START_POSITION.step("\n", 1));
        assertEquals(new Position(1, 1, 2), START_POSITION.step(SMILEY_STRING, 1));
        assertEquals(new Position(2, 1, 2), START_POSITION.step(SMILEY_STRING, 2));
    }

}
