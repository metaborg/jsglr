package org.spoofax.jsglr2.imploder;

import java.util.Collection;

import javax.annotation.Nullable;

import org.apache.commons.vfs2.FileObject;
import org.spoofax.jsglr2.messages.Message;

public class ImplodeResult<IntermediateResult, AbstractSyntaxTree>
    implements IImplodeResult<IntermediateResult, AbstractSyntaxTree> {

    public final @Nullable FileObject resource;
    public final IntermediateResult intermediateResult;
    public final AbstractSyntaxTree ast;
    public final Collection<Message> messages;

    public ImplodeResult(@Nullable FileObject resource, IntermediateResult intermediateResult, AbstractSyntaxTree ast,
        Collection<Message> messages) {
        this.resource = resource;
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
