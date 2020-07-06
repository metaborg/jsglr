package org.spoofax.jsglr2.imploder;

public interface IImplodeResult<IntermediateResult, Cache, AbstractSyntaxTree> {

    IntermediateResult intermediateResult();

    Cache resultCache();

    AbstractSyntaxTree ast();

    boolean isAmbiguous();

}
