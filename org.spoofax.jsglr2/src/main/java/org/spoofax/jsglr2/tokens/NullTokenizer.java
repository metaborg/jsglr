package org.spoofax.jsglr2.tokens;

import java.util.Collections;

import javax.annotation.Nullable;

import org.apache.commons.vfs2.FileObject;
import org.spoofax.jsglr2.imploder.ITokenizer;
import org.spoofax.jsglr2.imploder.TokenizeResult;
import org.spoofax.jsglr2.imploder.TokenizedImplodeResult;

public class NullTokenizer<IntermediateResult, AbstractSyntaxTree>
    implements ITokenizer<TokenizedImplodeResult<IntermediateResult, AbstractSyntaxTree>> {

    @Override public TokenizeResult tokenize(String input, @Nullable FileObject resource,
        TokenizedImplodeResult<IntermediateResult, AbstractSyntaxTree> implodeResult) {
        return new TokenizeResult(implodeResult.tokens(), Collections.emptyList());
    }

}
