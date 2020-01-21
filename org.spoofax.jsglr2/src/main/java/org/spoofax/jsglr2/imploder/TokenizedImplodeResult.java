package org.spoofax.jsglr2.imploder;

import java.util.Collection;

import javax.annotation.Nullable;

import org.apache.commons.vfs2.FileObject;
import org.spoofax.jsglr2.messages.Message;
import org.spoofax.jsglr2.tokens.Tokens;

public class TokenizedImplodeResult<IntermediateResult, AbstractSyntaxTree>
    extends ImplodeResult<IntermediateResult, AbstractSyntaxTree>
    implements ITokenizedImplodeResult<IntermediateResult, AbstractSyntaxTree> {

    public final Tokens tokens;

    public TokenizedImplodeResult(@Nullable FileObject resource, IntermediateResult intermediateResult,
        AbstractSyntaxTree ast, Collection<Message> messages, Tokens tokens) {
        super(resource, intermediateResult, ast, messages);
        this.tokens = tokens;
    }

    @Override public Tokens tokens() {
        return tokens;
    }
}
