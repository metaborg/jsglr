package org.spoofax.jsglr2.messages;

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
