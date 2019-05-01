package org.spoofax.jsglr2;

import org.spoofax.jsglr2.imploder.ImplodeResult;
import org.spoofax.jsglr2.tokens.Tokens;

public final class JSGLR2Result<AbstractSyntaxTree> {

    public final boolean isSuccess;
    public final Tokens tokens;
    public final AbstractSyntaxTree ast;

    /**
     * Constructs a result in the case that the parse failed. The fields `tokens` and `ast` are set to `null`.
     */
    JSGLR2Result() {
        this.isSuccess = false;
        this.tokens = null;
        this.ast = null;
    }

    /**
     * Constructs a result in the case that the parse succeeded.
     * 
     * @param implodeResult
     *            The implode result.
     */
    JSGLR2Result(ImplodeResult<AbstractSyntaxTree> implodeResult) {
        this.isSuccess = true;
        this.tokens = implodeResult.tokens;
        this.ast = implodeResult.ast;
    }

}
