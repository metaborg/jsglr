package org.spoofax.jsglr2.imploder;

import javax.annotation.Nullable;

import org.apache.commons.vfs2.FileObject;
import org.spoofax.jsglr2.tokens.Tokens;

public interface ITokenizer<ImplodeResult> {

    Tokens tokenize(String input, @Nullable FileObject resource, ImplodeResult tree);

}
