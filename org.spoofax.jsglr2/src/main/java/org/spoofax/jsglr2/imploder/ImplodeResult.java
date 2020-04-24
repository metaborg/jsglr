package org.spoofax.jsglr2.imploder;

import java.util.Collection;

import org.spoofax.jsglr2.messages.Message;

public class ImplodeResult<IntermediateResult, Cache, AbstractSyntaxTree>
    implements IImplodeResult<IntermediateResult, Cache, AbstractSyntaxTree> {

    public final IntermediateResult intermediateResult;
    public final Cache cache;
    public final AbstractSyntaxTree ast;
    public final Collection<Message> messages;

    public ImplodeResult(IntermediateResult intermediateResult, Cache cache, AbstractSyntaxTree ast,
        Collection<Message> messages) {
        this.intermediateResult = intermediateResult;
        this.cache = cache;
        this.ast = ast;
        this.messages = messages;
    }

    @Override public IntermediateResult intermediateResult() {
        return intermediateResult;
    }

    @Override public Cache resultCache() {
        return cache;
    }

    @Override public AbstractSyntaxTree ast() {
        return ast;
    }

    @Override public Collection<Message> messages() {
        return messages;
    }
}
