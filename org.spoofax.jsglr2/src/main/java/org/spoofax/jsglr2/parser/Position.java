package org.spoofax.jsglr2.parser;

public class Position {

    /**
     * The offset in the input string, starts counting at 0 (consistent with Java string indexing).
     */
    final public int offset;

    /**
     * The line number of the current offset, starts counting at 1 (consistent with IDE cursor position).
     */
    final public int line;

    /**
     * The column number of the current offset, starts counting at 1 (consistent with IDE cursor position).
     */
    final public int column;

    /**
     * Basic constructor.
     */
    public Position(int offset, int line, int column) {
        this.offset = offset;
        this.line = line;
        this.column = column;
    }

    /**
     * Clones a Position.
     *
     * @param p
     *            The Position to clone.
     */
    public Position(Position p) {
        this.offset = p.offset;
        this.line = p.line;
        this.column = p.column;
    }

    public static Position atEnd(String string) {
        String[] lines = string.split("\n");

        if(lines.length > 0) {
            int line = lines.length;
            int column = lines[line - 1].length() + 1;

            if(string.endsWith("\n")) {
                line++;
                column = 1;
            }

            return new Position(string.length(), line, column);
        } else
            return new Position(string.length(), 2, 1);
    }

    public static Position atOffset(String string, int offset) {
        return atEnd(string.substring(0, offset));
    }

    /**
     * @return A new position that represents the position right of the current position.
     */
    public Position nextColumn() {
        return new Position(offset + 1, line, column + 1);
    }

    /**
     * Should only be called when the character at the current position is a newline character.
     *
     * @return A new position that represents the first column on the line after the current position.
     */
    public Position nextLine() {
        return new Position(offset + 1, line + 1, 1);
    }

    /**
     * Step from the current position in the input string by the given width.
     *
     * @return A new position that presents the position after the step in the given string.
     */
    public Position step(String inputString, int width) {
        int offset = this.offset;
        int line = this.line;
        int column = this.column;
        int end = Integer.min(inputString.length(), offset + width);
        for(; offset < end; offset++) {
            if(inputString.charAt(offset) == '\n') {
                line++;
                column = 1;
            } else {
                column++;
            }
        }
        return new Position(offset, line, column);
    }

    public String coordinatesToString() {
        return line + ":" + column;
    }

    @Override public String toString() {
        return "l: " + line + " c: " + column + " offset: " + offset;
    }

    @Override public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + column;
        result = prime * result + line;
        result = prime * result + offset;
        return result;
    }

    @Override public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        Position other = (Position) obj;
        return column == other.column && line == other.line && offset == other.offset;
    }


}
