package org.spoofax.jsglr2.parser;

import org.metaborg.parsetable.characterclasses.CharacterClassFactory;

import jsglr.shared.IToken;

public class Position {

    /**
     * The offset in the input string, starts counting at 0.<br>
     * A Unicode character outside the Basic Multilingual Plane (thus consisting of a high- and low-surrogate pair)
     * counts as two, consistent with Java String indexing.
     */
    public final int offset;

    /**
     * The line number of the current offset, starts counting at 1 (consistent with IDE cursor position).
     */
    public final int line;

    /**
     * The column number of the current offset, starts counting at 1.<br>
     * A Unicode character outside the Basic Multilingual Plane (thus consisting of a high- and low-surrogate pair)
     * counts as one, consistent with IDE cursor position.
     */
    public final int column;

    /**
     * The position at the start of a string; with offset 0, line 1, and column 1.<br>
     * Is returned when calling {@link Position#atOffset} with offset 0.
     */
    public static final Position START_POSITION = new Position(0, 1, 1);

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
        return atOffset(string, string.length());
    }

    public static Position atOffset(String string, int offset) {
        if(offset == 0)
            return START_POSITION;
        int lines = 1;
        for(int i = 0; i < offset; i++) {
            if(string.charAt(i) == '\n')
                lines++;
        }

        int column = string.codePointBefore(offset) == '\n' ? 1
            : 1 + string.codePointCount(string.lastIndexOf('\n', offset - 1) + 1, offset);

        return new Position(offset, lines, column);
    }

    /**
     * @param character
     *            Based on the character, the {@link #offset} is incremented by 1 or 2, according to
     *            {@link Character#charCount}.<br>
     *            If the character is a newline, {@link #nextLine()} is called instead.
     * @return A new position that represents the position right of the current position.
     */
    public Position next(int character) {
        return CharacterClassFactory.isNewLine(character) ? nextLine()
            : new Position(offset + Character.charCount(character), line, column + 1);
    }

    /**
     * @return A new position that represents the position left of the current position.
     */
    public Position previous(String inputString) {
        int previousChar = inputString.codePointBefore(offset);
        int previousCharWidth = Character.charCount(previousChar);

        return CharacterClassFactory.isNewLine(previousChar)
            ? Position.atOffset(inputString, offset - previousCharWidth)
            : new Position(offset - previousCharWidth, line, column - 1);
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
     * @param width
     *            The number of <b>characters</b> to step (not number of code points).
     * @return A new position that presents the position after the step in the given string.
     */
    public Position step(String inputString, int width) {
        int offset = this.offset;
        int line = this.line;
        int column = this.column;
        int end = Integer.min(inputString.length(), offset + width);
        for(; offset < end; offset++) {
            char c = inputString.charAt(offset);
            if(CharacterClassFactory.isNewLine(c)) {
                line++;
                column = 1;
            } else {
                if(!Character.isLowSurrogate(c))
                    column++;
            }
        }
        return new Position(offset, line, column);
    }

    public String coordinatesToString() {
        return line + ":" + column;
    }

    @Override public String toString() {
        return "offset: " + offset + " l: " + line + " c: " + column;
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

    public static Position atStartOfToken(IToken token) {
        return new Position(token.getStartOffset(), token.getLine(), token.getColumn());
    }

}
