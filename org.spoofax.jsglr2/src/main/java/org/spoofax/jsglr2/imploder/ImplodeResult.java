package org.spoofax.jsglr2.imploder;

public class ImplodeResult<IntermediateResult, Cache, AbstractSyntaxTree>
    implements IImplodeResult<IntermediateResult, Cache, AbstractSyntaxTree> {

    public final IntermediateResult intermediateResult;
    public final Cache cache;
    public final AbstractSyntaxTree ast;

    public ImplodeResult(IntermediateResult intermediateResult, Cache cache, AbstractSyntaxTree ast) {
        this.intermediateResult = intermediateResult;
        this.cache = cache;
        this.ast = ast;
    }

    @Override public IntermediateResult intermediateResult() {
        return intermediateResult;
    }

    @Override public Cache resultCache() {
        return cache;
    }

    @Override public AbstractSyntaxTree ast() {
        return ast;
    }
}
