package org.spoofax.jsglr2.layoutsensitive;

import org.spoofax.jsglr2.parser.Position;

public class Shape {

    public final Position start, end, left, right;

    public Shape(Position start, Position end, Position left, Position right) {
        this.start = start;
        this.end = end;
        this.left = left;
        this.right = right;
    }

    @Override public String toString() {
        return "[" + start + ", " + end + ", " + left + ", " + right + "]";
    }

}
