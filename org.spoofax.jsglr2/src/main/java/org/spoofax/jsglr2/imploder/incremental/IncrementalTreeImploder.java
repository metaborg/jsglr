package org.spoofax.jsglr2.imploder.incremental;

import java.util.Collections;
import java.util.WeakHashMap;

import org.spoofax.jsglr2.JSGLR2Request;
import org.spoofax.jsglr2.imploder.ImplodeResult;
import org.spoofax.jsglr2.imploder.TreeImploder;
import org.spoofax.jsglr2.imploder.input.IImplodeInputFactory;
import org.spoofax.jsglr2.imploder.treefactory.ITreeFactory;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;

public abstract class IncrementalTreeImploder
//@formatter:off
   <ParseForest extends IParseForest,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    Derivation  extends IDerivation<ParseForest>,
    Cache       extends IncrementalTreeImploder.ResultCache<ParseForest, Tree>,
    Tree,
    Input       extends IncrementalImplodeInput<ParseNode, Cache, Tree>>
//@formatter:on
    extends TreeImploder<ParseForest, ParseNode, Derivation, Cache, Tree, Input> {

    private final IIncrementalImplodeInputFactory<ParseNode, Cache, Tree, Input> incrementalInputFactory;
    private final TreeImploder<ParseForest, ParseNode, Derivation, Void, Tree, Input> regularImplode;

    public IncrementalTreeImploder(IImplodeInputFactory<Input> inputFactory,
        IIncrementalImplodeInputFactory<ParseNode, Cache, Tree, Input> incrementalInputFactory,
        ITreeFactory<Tree> treeFactory) {
        super(inputFactory, treeFactory);
        this.regularImplode = new TreeImploder<>(inputFactory, treeFactory);
        this.incrementalInputFactory = incrementalInputFactory;
    }

    @Override public ImplodeResult<SubTree<Tree>, Cache, Tree> implode(JSGLR2Request request, ParseForest parseForest,
        Cache resultCache) {

        if(resultCache == null) {
            ImplodeResult<SubTree<Tree>, Void, Tree> result = regularImplode.implode(request, parseForest);
            return new ImplodeResult<>(result.intermediateResult, null, result.ast, result.messages);
        }

        SubTree<Tree> result =
            implodeParseNode(incrementalInputFactory.get(request.input, resultCache), parseForest, 0);

        return new ImplodeResult<>(result, resultCache, result.tree, Collections.emptyList());
    }

    @Override protected SubTree<Tree> implodeParseNode(Input input, ParseForest parseNode, int startOffset) {
        if(input.resultCache.cache.containsKey(parseNode))
            return input.resultCache.cache.get(parseNode);

        SubTree<Tree> result = super.implodeParseNode(input, parseNode, startOffset);
        input.resultCache.cache.put(parseNode, result);
        return result;
    }

    public static class ResultCache<ParseForest extends IParseForest, Tree> {
        protected final WeakHashMap<ParseForest, SubTree<Tree>> cache = new WeakHashMap<>();
    }

}
