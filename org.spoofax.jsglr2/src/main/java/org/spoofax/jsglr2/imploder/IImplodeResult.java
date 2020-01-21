package org.spoofax.jsglr2.imploder;

import java.util.Collection;

import org.spoofax.jsglr2.messages.Message;

public interface IImplodeResult<IntermediateResult, AbstractSyntaxTree> {

    IntermediateResult intermediateResult();

    AbstractSyntaxTree ast();

    Collection<Message> messages();

}
