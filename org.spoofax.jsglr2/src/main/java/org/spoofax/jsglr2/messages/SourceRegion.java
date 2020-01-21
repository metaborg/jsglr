package org.spoofax.jsglr2.messages;

public class SourceRegion {

    public final int startOffset;
    public final int startRow;
    public final int startColumn;
    public final int endOffset;
    public final int endRow;
    public final int endColumn;

    public SourceRegion(int startOffset, int startRow, int startColumn, int endOffset, int endRow, int endColumn) {
        this.startOffset = startOffset;
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.endOffset = endOffset;
        this.endRow = endRow;
        this.endColumn = endColumn;
    }

}
