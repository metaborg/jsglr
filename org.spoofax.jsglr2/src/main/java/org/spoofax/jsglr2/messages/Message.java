package org.spoofax.jsglr2.messages;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.spoofax.jsglr2.parser.Position;

public class Message {

    public final String message;
    public final Category category;
    public final Severity severity;
    /** If the region of a message is null, it should be displayed at the top of the program. */
    public final SourceRegion region;

    public Message(@Nonnull String message, @Nonnull Category category, @Nullable SourceRegion region) {
        this.message = message;
        this.category = category;
        this.severity = category.severity;
        this.region = region;
    }

    public Message(@Nonnull String message, @Nonnull Category category, @Nonnull Position startPosition,
        @Nonnull Position endPosition) {
        this(message, category, new SourceRegion(startPosition, endPosition));
    }

    public Message(@Nonnull String message, @Nonnull Category category, @Nullable Position position) {
        this(message, category, position == null ? null : new SourceRegion(position, position));
    }

    public Message atRegion(SourceRegion otherRegion) {
        return new Message(message, category, otherRegion);
    }

    @Override public String toString() {
        return "\"" + message + "\" " + severity + " @ " + region;
    }

}
