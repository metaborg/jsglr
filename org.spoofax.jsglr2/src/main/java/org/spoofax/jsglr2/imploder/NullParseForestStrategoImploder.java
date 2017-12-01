package org.spoofax.jsglr2.imploder;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseForest;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public class NullParseForestStrategoImploder<StackNode extends AbstractStackNode<HybridParseForest>>
    implements IImploder<HybridParseForest, StackNode, IStrategoTerm> {

    @Override
    public ImplodeResult<HybridParseForest, StackNode, IStrategoTerm> implode(Parse<HybridParseForest, StackNode> parse,
        HybridParseForest parseForest) {
        return null;
    }

}
