package org.spoofax.jsglr2.messages;

import org.spoofax.jsglr2.parser.Position;

public class Message {

    public final String message;
    public final Severity severity;
    public final Category category;
    public final SourceRegion region;

    public Message(String message, Severity severity, Category category, SourceRegion region) {
        this.message = message;
        this.severity = severity;
        this.category = category;
        this.region = region;
    }

    public Message atRegion(SourceRegion otherRegion) {
        return new Message(message, severity, category, otherRegion);
    }

    @Override public String toString() {
        return "\"" + message + "\" " + severity + " @ " + region;
    }

    public static Message error(String message, Category category, Position position) {
        if(position == null)
            return errorAtTop(message, category);
        else {
            SourceRegion region = new SourceRegion(position);

            return new Message(message, Severity.ERROR, category, region);
        }
    }

    public static Message error(String message, Category category, Position startPosition, Position endPosition) {
        return new Message(message, Severity.ERROR, category, new SourceRegion(startPosition, endPosition));
    }

    public static Message error(String message, Category category, SourceRegion region) {
        return new Message(message, Severity.ERROR, category, region);
    }

    public static Message atTop(String message, Severity severity, Category category) {
        return new Message(message, severity, category, null);
    }

    public static Message errorAtTop(String message, Category category) {
        return atTop(message, Severity.ERROR, category);
    }

    public static Message warning(String message, Category category, Position startPosition, Position endPosition) {
        return new Message(message, Severity.WARNING, category, new SourceRegion(startPosition, endPosition));
    }

}
