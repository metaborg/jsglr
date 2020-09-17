package org.spoofax.jsglr2.messages;

import org.spoofax.jsglr2.parser.Position;

public class Message {

    public final String message;
    public final Severity severity;
    public final SourceRegion region;

    public Message(String message, Severity severity, SourceRegion region) {
        this.message = message;
        this.severity = severity;
        this.region = region;
    }

    public Message atRegion(SourceRegion otherRegion) {
        return new Message(message, severity, otherRegion);
    }

    @Override public String toString() {
        return "\"" + message + "\" " + severity + " @ " + region;
    }

    public static Message error(String message, Position position) {
        if(position == null)
            return errorAtTop(message);
        else {
            SourceRegion region = new SourceRegion(position);

            return new Message(message, Severity.ERROR, region);
        }
    }

    public static Message error(String message, Position startPosition, Position endPosition) {
        return new Message(message, Severity.ERROR, new SourceRegion(startPosition, endPosition));
    }

    public static Message error(String message, SourceRegion region) {
        return new Message(message, Severity.ERROR, region);
    }

    public static Message atTop(String message, Severity severity) {
        return new Message(message, severity, null);
    }

    public static Message errorAtTop(String message) {
        return atTop(message, Severity.ERROR);
    }

}
