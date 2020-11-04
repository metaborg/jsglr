package org.spoofax.jsglr2.messages;

import static org.spoofax.jsglr2.messages.Severity.ERROR;
import static org.spoofax.jsglr2.messages.Severity.WARNING;

public enum Category {
    PARSING(ERROR), RECOVERY(ERROR), CYCLE(ERROR), AMBIGUITY(WARNING), NON_ASSOC(ERROR);

    public final Severity severity;

    Category(Severity severity) {
        this.severity = severity;
    }
}
