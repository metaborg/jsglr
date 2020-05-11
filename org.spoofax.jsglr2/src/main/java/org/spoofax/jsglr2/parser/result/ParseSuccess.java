package org.spoofax.jsglr2.parser.result;

import java.util.Collection;
import java.util.Collections;

import org.spoofax.jsglr2.messages.Message;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParseState;

public class ParseSuccess<ParseForest extends IParseForest> extends ParseResult<ParseForest> {

    public final ParseForest parseResult;

    public ParseSuccess(AbstractParseState<?, ?> parseState, ParseForest parseResult, Collection<Message> messages) {
        super(parseState, messages);

        this.parseResult = parseResult;
    }

    public boolean isSuccess() {
        return true;
    }

}
