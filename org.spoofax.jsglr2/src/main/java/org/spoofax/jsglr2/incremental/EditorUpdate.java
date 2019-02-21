package org.spoofax.jsglr2.incremental;

import org.spoofax.jsglr2.parser.PositionInterval;

public class EditorUpdate {
    public final PositionInterval deleted;
    public final String insterted;

    public EditorUpdate(PositionInterval deleted, String insterted) {
        this.deleted = deleted;
        this.insterted = insterted;
    }
}
