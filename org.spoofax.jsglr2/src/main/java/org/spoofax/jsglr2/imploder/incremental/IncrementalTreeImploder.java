package org.spoofax.jsglr2.imploder.incremental;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private Map<String, ParseNode> inputCache = new HashMap<>();
    private Map<String, SubTree<Tree>> outputCache = new HashMap<>();

    public IncrementalTreeImploder(ITreeFactory<Tree> treeFactory) {
        super(treeFactory);
    }

    @Override public SubTree<Tree> implode(String inputString, String filename, ParseForest parseForest) {
        @SuppressWarnings("unchecked") ParseNode topParseNode = (ParseNode) parseForest;

        final SubTree<Tree> tree;
        if(!filename.equals("") && inputCache.containsKey(filename) && outputCache.containsKey(filename)) {
            tree = implodeParseNode(inputString, topParseNode, inputCache.get(filename), outputCache.get(filename), 0);
        } else
            tree = implodeParseNode(inputString, topParseNode, 0);

        if(!filename.equals("")) {
            inputCache.put(filename, topParseNode);
            outputCache.put(filename, tree);
        }

        return tree;
    }

    private SubTree<Tree> implodeParseNode(String inputString, ParseNode parseNode, ParseNode oldNode,
        SubTree<Tree> oldResult, int startOffset) {
        if(parseNode == oldNode)
            return oldResult;

        IProduction production = parseNode.production();
        IProduction oldProduction = oldNode.production();
        if(production != oldProduction)
            return implodeParseNode(inputString, parseNode, startOffset);

        if(production.isContextFree()) {
            List<Derivation> filteredDerivations = applyDisambiguationFilters(parseNode);
            List<Derivation> oldDerivations = applyDisambiguationFilters(oldNode);
            if(filteredDerivations.size() != oldDerivations.size())
                return implodeParseNode(inputString, parseNode, startOffset);

            if(filteredDerivations.size() > 1) {
                List<Tree> trees = new ArrayList<>(filteredDerivations.size());
                List<SubTree<Tree>> subTrees = new ArrayList<>(filteredDerivations.size());

                for(int i = 0; i < filteredDerivations.size(); i++) {
                    Derivation derivation = filteredDerivations.get(i);
                    Derivation oldDerivation = oldDerivations.get(i);
                    SubTree<Tree> result = implodeDerivation(inputString, derivation, oldDerivation,
                        oldResult.children.get(i), startOffset);
                    trees.add(result.tree);
                    subTrees.add(result);
                }

                return new SubTree<>(treeFactory.createAmb(production.sort(), trees), subTrees, null, null,
                    subTrees.get(0).width);
            } else
                return implodeDerivation(inputString, filteredDerivations.get(0), oldDerivations.get(0), oldResult,
                    startOffset);
        } else {
            String substring = inputString.substring(startOffset, startOffset + parseNode.width());

            return new SubTree<>(createLexicalTerm(production, substring), production, substring);
        }
    }

    private SubTree<Tree> implodeDerivation(String inputString, Derivation derivation, Derivation oldDerivation,
        SubTree<Tree> oldResult, int startOffset) {
        IProduction production = derivation.production();
        IProduction oldProduction = oldDerivation.production();
        if(production != oldProduction)
            return implodeDerivation(inputString, derivation, startOffset);

        if(!production.isContextFree())
            throw new RuntimeException("non context free imploding not supported");

        List<Tree> childASTs = new ArrayList<>();
        List<SubTree<Tree>> subTrees = new ArrayList<>();

        List<ParseForest> childParseForests = getChildParseForests(derivation);
        List<ParseForest> oldChildParseForests = getChildParseForests(oldDerivation);
        if(childParseForests.size() != oldChildParseForests.size())
            return implodeDerivation(inputString, derivation, startOffset);

        for(int i = 0; i < childParseForests.size(); i++) {
            ParseForest childParseForest = childParseForests.get(i);
            if(childParseForest != null) { // Can be null in the case of a layout subtree parse node that is not created
                @SuppressWarnings("unchecked") ParseNode childParseNode = (ParseNode) childParseForest;
                @SuppressWarnings("unchecked") ParseNode oldChildParseForest = (ParseNode) oldChildParseForests.get(i);

                SubTree<Tree> subTree = implodeParseNode(inputString, childParseNode, oldChildParseForest,
                    oldResult.children.get(i), startOffset);

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
