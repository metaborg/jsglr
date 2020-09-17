package org.spoofax.jsglr2.messages;

import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr2.parser.Position;

public class SourceRegion {

    public final int startOffset, startRow, startColumn;
    /** Inclusive. */
    public final int endOffset, endRow, endColumn;

    public SourceRegion(int startOffset, int startRow, int startColumn, int endOffset, int endRow, int endColumn) {
        this.startOffset = startOffset;
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.endOffset = endOffset;
        this.endRow = endRow;
        this.endColumn = endColumn;
    }

    public SourceRegion(Position start, Position end) {
        this.startOffset = start.offset;
        this.startRow = start.line;
        this.startColumn = start.column;
        this.endOffset = end.offset;
        this.endRow = end.line;
        this.endColumn = end.column;
    }

    public SourceRegion(Position position) {
        this(position, position);
    }

    public Position position() {
        return new Position(startOffset, startRow, startColumn);
    }

    @Override public String toString() {
        return "[" + startOffset + "," + startRow + "," + startColumn + "," + endOffset + "," + endRow + "," + endColumn
            + ']';
    }

    public static SourceRegion fromToken(IToken token) {
        return new SourceRegion(token.getStartOffset(), token.getLine(), token.getColumn(), token.getEndOffset(),
            token.getEndLine(), token.getEndColumn());
    }

}
