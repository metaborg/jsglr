package org.spoofax.jsglr2.imploder;

import java.util.Collection;

import org.spoofax.jsglr2.messages.Message;

public class ImplodeResult<IntermediateResult, AbstractSyntaxTree>
    implements IImplodeResult<IntermediateResult, AbstractSyntaxTree> {

    public final IntermediateResult intermediateResult;
    public final AbstractSyntaxTree ast;
    public final Collection<Message> messages;

    public ImplodeResult(IntermediateResult intermediateResult, AbstractSyntaxTree ast, Collection<Message> messages) {
        this.intermediateResult = intermediateResult;
        this.ast = ast;
        this.messages = messages;
    }

    @Override public IntermediateResult intermediateResult() {
        return intermediateResult;
    }

    @Override public AbstractSyntaxTree ast() {
        return ast;
    }

    @Override public Collection<Message> messages() {
        return messages;
    }
}
