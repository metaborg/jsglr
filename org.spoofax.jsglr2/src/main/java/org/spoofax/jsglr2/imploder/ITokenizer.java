package org.spoofax.jsglr2.imploder;

import org.spoofax.jsglr2.JSGLR2Request;

public interface ITokenizer<ImplodeIntermediateResult> {

    TokenizeResult tokenize(JSGLR2Request request, ImplodeIntermediateResult tree);

}
