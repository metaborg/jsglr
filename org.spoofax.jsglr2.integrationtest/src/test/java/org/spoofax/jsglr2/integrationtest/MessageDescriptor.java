package org.spoofax.jsglr2.integrationtest;

import org.spoofax.jsglr2.messages.Message;
import org.spoofax.jsglr2.messages.Severity;
import org.spoofax.jsglr2.messages.SourceRegion;

public final class MessageDescriptor {

    public final String message;
    public final Severity severity;
    public final int offset, line, column;
    /** Inclusive. */
    public final int endOffset, endLine, endColumn;

    public MessageDescriptor(String message, Severity severity, int offset, int line, int column, int endOffset,
        int endLine, int endColumn) {
        this.message = message;
        this.severity = severity;
        this.offset = offset;
        this.line = line;
        this.column = column;
        this.endOffset = endOffset;
        this.endLine = endLine;
        this.endColumn = endColumn;
    }

    public MessageDescriptor(String message, Severity severity, int offset, int line, int column, int width) {
        // We need to subtract 1 from the end offset/column because they represent an inclusive bound.
        // Now, the `width` represents the exact number of characters that will be underlined for the error in the IDE.
        this(message, severity, offset, line, column, offset + width - 1, line, column + width - 1);
    }

    public MessageDescriptor(String message, Severity severity) {
        this(message, severity, -1, -1, -1, -1, -1, -1);
    }

    public static MessageDescriptor from(Message message) {
        SourceRegion region = message.region;

        return region == null ? new MessageDescriptor(message.message, message.severity)
            : new MessageDescriptor(message.message, message.severity, region.startOffset, region.startRow,
                region.startColumn, region.endOffset, region.endRow, region.endColumn);
    }

    @Override public boolean equals(Object obj) {
        if(obj == null || obj.getClass() != this.getClass())
            return false;

        MessageDescriptor other = (MessageDescriptor) obj;

        return message.equals(other.message) && severity == other.severity && offset == other.offset
            && line == other.line && column == other.column && endOffset == other.endOffset && endLine == other.endLine
            && endColumn == other.endColumn;
    }

    @Override public String toString() {
        return "<'" + message + "';" + severity + ";" + offset + ":" + line + "," + column + ";" + endOffset + ":"
            + endLine + "," + endColumn + ">";
    }

}
