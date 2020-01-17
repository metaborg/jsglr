package org.spoofax.jsglr2.imploder;

import java.util.Collection;

import org.metaborg.core.messages.IMessage;

public interface IImplodeResult<IntermediateResult, AbstractSyntaxTree> {

    IntermediateResult intermediateResult();

    AbstractSyntaxTree ast();

    Collection<IMessage> messages();

}
