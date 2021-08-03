package org.spoofax.jsglr2.messages;

import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr2.parser.Position;

import javax.annotation.Nullable;
import java.io.Serializable;

public class SourceRegion implements Serializable {

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

    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final SourceRegion that = (SourceRegion)o;
        if(startOffset != that.startOffset) return false;
        if(startRow != that.startRow) return false;
        if(startColumn != that.startColumn) return false;
        if(endOffset != that.endOffset) return false;
        if(endRow != that.endRow) return false;
        return endColumn == that.endColumn;
    }

    @Override public int hashCode() {
        int result = startOffset;
        result = 31 * result + startRow;
        result = 31 * result + startColumn;
        result = 31 * result + endOffset;
        result = 31 * result + endRow;
        result = 31 * result + endColumn;
        return result;
    }
}
