package org.spoofax.jsglr2.imploder;

import org.spoofax.jsglr2.tokens.Tokens;

public interface ITokenizedImplodeResult<IntermediateResult, AbstractSyntaxTree>
    extends IImplodeResult<IntermediateResult, AbstractSyntaxTree> {

    Tokens tokens();

}
