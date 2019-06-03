package org.spoofax.jsglr2.imploder.incremental;

import java.util.*;

import org.metaborg.parsetable.IProduction;
import org.spoofax.jsglr2.imploder.TreeImploder;
import org.spoofax.jsglr2.imploder.treefactory.ITreeFactory;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;

public abstract class IncrementalTreeImploder
//@formatter:off
   <ParseForest extends IParseForest,
    ParseNode extends IParseNode<ParseForest, Derivation>,
    Derivation extends IDerivation<ParseForest>,
    Tree>
//@formatter:on
    extends TreeImploder<ParseForest, ParseNode, Derivation, Tree> {

    private Map<String, WeakHashMap<ParseNode, SubTree<Tree>>> cache = new HashMap<>();

    public IncrementalTreeImploder(ITreeFactory<Tree> treeFactory) {
        super(treeFactory);
    }

    @Override public SubTree<Tree> implode(String inputString, String filename, ParseForest parseForest) {
        @SuppressWarnings("unchecked") ParseNode topParseNode = (ParseNode) parseForest;

        if(filename.equals("")) {
            return implodeParseNode(inputString, topParseNode, 0);
        }

        if(!cache.containsKey(filename)) {
            cache.put(filename, new WeakHashMap<>());
        }

        return implodeParseNode(inputString, topParseNode, 0, cache.get(filename));
    }

    // Implementation note: the code in implodeParseNode and implodeDerivation is exactly equal to the superclass,
    // except for the usage of the cache. Since it's an extra parameter that needs to be passed and the changes are
    // nested deeply, it's not easy to reuse code, unfortunately...
    private SubTree<Tree> implodeParseNode(String inputString, ParseNode parseNode, int startOffset,
        WeakHashMap<ParseNode, SubTree<Tree>> oldResult) {
        if(oldResult.containsKey(parseNode))
            return oldResult.get(parseNode);

        IProduction production = parseNode.production();

        if(production.isContextFree()) {
            List<Derivation> filteredDerivations = applyDisambiguationFilters(parseNode);

            if(filteredDerivations.size() > 1) {
                List<Tree> trees = new ArrayList<>(filteredDerivations.size());
                List<SubTree<Tree>> subTrees = new ArrayList<>(filteredDerivations.size());

                for(Derivation derivation : filteredDerivations) {
                    SubTree<Tree> result = implodeDerivation(inputString, derivation, startOffset, oldResult);
                    trees.add(result.tree);
                    subTrees.add(result);
                }

                return new SubTree<>(treeFactory.createAmb(production.sort(), trees), subTrees, null, null,
                    subTrees.get(0).width);
            } else
                return implodeDerivation(inputString, filteredDerivations.get(0), startOffset, oldResult);
        } else {
            String substring = inputString.substring(startOffset, startOffset + parseNode.width());

            return new SubTree<>(createLexicalTerm(production, substring), production, substring);
        }
    }

    private SubTree<Tree> implodeDerivation(String inputString, Derivation derivation, int startOffset,
        WeakHashMap<ParseNode, SubTree<Tree>> oldResult) {
        IProduction production = derivation.production();

        if(!production.isContextFree())
            throw new RuntimeException("non context free imploding not supported");

        List<Tree> childASTs = new ArrayList<>();
        List<SubTree<Tree>> subTrees = new ArrayList<>();

        for(ParseForest childParseForest : getChildParseForests(derivation)) {
            if(childParseForest != null) { // Can be null in the case of a layout subtree parse node that is not created
                @SuppressWarnings("unchecked") ParseNode childParseNode = (ParseNode) childParseForest;

                SubTree<Tree> subTree = implodeParseNode(inputString, childParseNode, startOffset, oldResult);
                oldResult.put(childParseNode, subTree);

                if(subTree.tree != null) {
                    childASTs.add(subTree.tree);
                }
                subTrees.add(subTree);
                startOffset += subTree.width;
            }
        }

        return new SubTree<>(createContextFreeTerm(production, childASTs), subTrees, derivation.production());
    }

}
