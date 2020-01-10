package org.spoofax.jsglr2.imploder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.messages.IMessage;
import org.metaborg.core.messages.MessageFactory;
import org.metaborg.core.source.ISourceRegion;
import org.metaborg.core.source.SourceRegion;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr2.tokens.Tokens;

import javax.annotation.Nullable;

public class TokenizeResult<AbstractSyntaxTree> {

    public final @Nullable FileObject resource;
    public final Tokens tokens;
    public final AbstractSyntaxTree ast;
    public final Collection<IMessage> messages;

    public TokenizeResult(@Nullable FileObject resource, Tokens tokens, AbstractSyntaxTree ast) {
        this.resource = resource;
        this.tokens = tokens;
        this.ast = ast;
        this.messages = messagesFromTokens(tokens);
    }

    private Collection<IMessage> messagesFromTokens(Tokens tokens) {
        List<IMessage> messages = new ArrayList<>();

        for(IToken token : tokens) {
            if(token.getKind() == IToken.TK_ERROR)
                messages.add(errorMessageFromToken(token));
        }

        return messages;
    }

    private IMessage errorMessageFromToken(IToken token) {
        ISourceRegion region = new SourceRegion(token.getStartOffset(), token.getLine(), token.getColumn(),
            token.getEndOffset(), token.getEndLine(), token.getEndColumn());

        return MessageFactory.newParseError(resource, region, "Syntax error", null);
    }

}
