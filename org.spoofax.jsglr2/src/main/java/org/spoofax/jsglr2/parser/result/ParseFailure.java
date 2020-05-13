package org.spoofax.jsglr2.parser.result;

import java.util.Collection;
import java.util.Collections;

import org.spoofax.jsglr2.messages.Message;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.ParseException;

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
        int offset = failureCause.position != null ? failureCause.position.offset : parseState.inputStack.offset();
        return new ParseException(failureCause, offset, parseState.inputStack.inputString().codePointAt(offset));
    }

}
