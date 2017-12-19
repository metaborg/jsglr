package org.spoofax.jsglr2.parser;

public class Position {

    final public int offset, line, column;

    public Position(int offset, int line, int column) {
        this.offset = offset;
        this.line = line;
        this.column = column;
    }

    public Position nextColumn() {
        return new Position(offset + 1, line, column + 1);
    }

    public Position nextLine() {
        return new Position(offset + 1, line + 1, 1);
    }

    public String coordinatesToString() {
        return "" + line + ":" + column;
    }

}
