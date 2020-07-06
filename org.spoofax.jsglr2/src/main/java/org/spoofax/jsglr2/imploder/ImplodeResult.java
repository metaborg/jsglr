package org.spoofax.jsglr2.imploder;

public final class ImplodeResult<IntermediateResult, Cache, AbstractSyntaxTree>
    implements IImplodeResult<IntermediateResult, Cache, AbstractSyntaxTree> {

    private final IntermediateResult intermediateResult;
    private final Cache cache;
    private final AbstractSyntaxTree ast;
    private final boolean isAmbiguous;

    public ImplodeResult(IntermediateResult intermediateResult, Cache cache, AbstractSyntaxTree ast, boolean isAmbiguous) {
        this.intermediateResult = intermediateResult;
        this.cache = cache;
        this.ast = ast;
        this.isAmbiguous = isAmbiguous;
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

    @Override
    public boolean isAmbiguous() {
        return isAmbiguous;
    }
}
