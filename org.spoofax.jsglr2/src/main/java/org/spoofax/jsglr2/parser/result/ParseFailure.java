package org.spoofax.jsglr2.parser.result;

import java.util.Collections;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.ParseException;

public class ParseFailure<ParseForest extends IParseForest> extends ParseResult<ParseForest> {

    public final ParseFailureType failureType;

    public ParseFailure(AbstractParseState<?, ?> parseState, ParseFailureType failureType) {
        super(parseState, Collections.singletonList(failureType.toMessage(parseState)));

        this.failureType = failureType;
    }

    public boolean isSuccess() {
        return false;
    }

    public ParseException exception() {
        int offset = parseState.inputStack.offset();
        return new ParseException(failureType, offset, parseState.inputStack.inputString().codePointAt(offset));
    }

}
