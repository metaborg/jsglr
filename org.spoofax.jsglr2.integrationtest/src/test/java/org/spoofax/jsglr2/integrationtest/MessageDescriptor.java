package org.spoofax.jsglr2.integrationtest;

import org.spoofax.jsglr2.messages.Message;
import org.spoofax.jsglr2.messages.Severity;
import org.spoofax.jsglr2.messages.SourceRegion;

public final class MessageDescriptor {

    public final String message;
    public final Severity severity;
    public final int offset, line, column;

    public MessageDescriptor(String message, Severity severity, int offset, int line, int column) {
        this.message = message;
        this.severity = severity;
        this.offset = offset;
        this.line = line;
        this.column = column;
    }

    public MessageDescriptor(String message, Severity severity) {
        this.message = message;
        this.severity = severity;
        this.offset = -1;
        this.line = -1;
        this.column = -1;
    }

    public static MessageDescriptor from(Message message) {
        SourceRegion region = message.region;

        return new MessageDescriptor(message.message, message.severity, region != null ? region.startOffset : -1,
            region != null ? region.startRow : -1, region != null ? region.startColumn : -1);
    }

    @Override public boolean equals(Object obj) {
        if(obj == null || obj.getClass() != this.getClass())
            return false;

        MessageDescriptor other = (MessageDescriptor) obj;

        return message.equals(other.message) && severity == other.severity && offset == other.offset
            && line == other.line && column == other.column;
    }

    @Override public String toString() {
        return "<'" + message + "';" + severity + ";" + offset + ";" + line + "," + column + ">";
    }

}
