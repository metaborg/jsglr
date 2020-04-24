package org.spoofax.jsglr2.imploder;

import java.util.Collection;

import org.spoofax.jsglr2.messages.Message;

public interface IImplodeResult<IntermediateResult, Cache, AbstractSyntaxTree> {

    IntermediateResult intermediateResult();

    Cache resultCache();

    AbstractSyntaxTree ast();

    Collection<Message> messages();

}
