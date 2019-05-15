package org.spoofax.jsglr2;

import java.util.Collection;

import org.spoofax.jsglr2.messages.Message;
import org.spoofax.jsglr2.parser.result.ParseFailure;

public class JSGLR2Failure<AbstractSyntaxTree> extends JSGLR2Result<AbstractSyntaxTree> {

    public final ParseFailure<?> parseFailure;

    JSGLR2Failure(JSGLR2Request request, ParseFailure<?> parseFailure, Collection<Message> messages) {
        super(request, messages);
        this.parseFailure = parseFailure;
    }

    public boolean isSuccess() {
        return false;
    }

}
