package org.spoofax.jsglr2.parser.result;

import java.util.Collection;
import java.util.Collections;

import org.spoofax.jsglr2.messages.Message;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.parser.Position;

public class ParseFailure<ParseForest extends IParseForest> extends ParseResult<ParseForest> {

    public final ParseFailureCause failureCause;

    public ParseFailure(AbstractParseState<?, ?> parseState, ParseFailureCause failureCause) {
        super(parseState, Collections.singletonList(failureCause.toMessage()));

        this.failureCause = failureCause;
    }

    public ParseFailure(AbstractParseState<?, ?> parseState, Collection<Message> messages,
        ParseFailureCause failureCause) {
        super(parseState, messages);

        this.failureCause = failureCause;
    }

    public boolean isSuccess() {
        return false;
    }

    public ParseException exception() {
        Position position = failureCause.position;

        return new ParseException(failureCause, position != null ? parseState.inputStack.safeCharacter() : null);
    }

}
