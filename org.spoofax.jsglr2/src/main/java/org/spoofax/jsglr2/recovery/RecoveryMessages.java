package org.spoofax.jsglr2.recovery;

import org.metaborg.parsetable.productions.IProduction;
import org.spoofax.jsglr2.messages.Message;
import org.spoofax.jsglr2.messages.SourceRegion;
import org.spoofax.jsglr2.parser.Position;

public class RecoveryMessages {

    public static Message get(IProduction production, Position start, Position end) {
        SourceRegion region =
            new SourceRegion(start.offset, start.line, start.column, end.offset, end.line, end.column);

        String message;

        if("WATER".equals(production.constructor()))
            message = "Not expected";
        else if("INSERTION".equals(production.constructor()))
            message = "Token expected";
        else
            message = "Invalid syntax";

        return Message.error(message, region);
    }

}
