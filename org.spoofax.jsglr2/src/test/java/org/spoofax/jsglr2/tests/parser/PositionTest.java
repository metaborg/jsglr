package org.spoofax.jsglr2.tests.parser;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.spoofax.jsglr2.parser.Position;

public class PositionTest {

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

    private void test(String string, int offset, int line, int column) {
        Position actual = Position.atOffset(string, offset);
        Position expected = new Position(offset, line, column);

        assertEquals(expected, actual);
    }

}