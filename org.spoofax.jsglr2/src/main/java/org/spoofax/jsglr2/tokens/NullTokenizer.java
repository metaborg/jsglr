package org.spoofax.jsglr2.tokens;

import javax.annotation.Nullable;

import org.apache.commons.vfs2.FileObject;
import org.spoofax.jsglr2.imploder.ITokenizer;
import org.spoofax.jsglr2.imploder.TokenizeResult;

public class NullTokenizer<Tree> implements ITokenizer<TokenizeResult<Tree>, Tree> {

    @Override public TokenizeResult<Tree> tokenize(String input, @Nullable FileObject resource,
        TokenizeResult<Tree> tree) {
        return tree;
    }

}
