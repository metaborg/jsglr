package org.spoofax.jsglr2.tokens;

import javax.annotation.Nullable;

import org.apache.commons.vfs2.FileObject;
import org.spoofax.jsglr2.imploder.ITokenizer;
import org.spoofax.jsglr2.imploder.TokenizedImplodeResult;

public class NullTokenizer<IntermediateResult, AbstractSyntaxTree>
    implements ITokenizer<TokenizedImplodeResult<IntermediateResult, AbstractSyntaxTree>> {

    @Override public Tokens tokenize(String input, @Nullable FileObject resource,
        TokenizedImplodeResult<IntermediateResult, AbstractSyntaxTree> implodeResult) {
        return implodeResult.tokens();
    }

}
