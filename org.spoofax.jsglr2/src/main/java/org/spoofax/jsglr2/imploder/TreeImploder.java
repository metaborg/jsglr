package org.spoofax.jsglr2.imploder;

import java.util.*;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.symbols.IMetaVarSymbol;
import org.spoofax.jsglr2.imploder.input.IImplodeInputFactory;
import org.spoofax.jsglr2.imploder.input.ImplodeInput;
import org.spoofax.jsglr2.imploder.treefactory.ITreeFactory;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;

public class TreeImploder
//@formatter:off
   <ParseForest extends IParseForest,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    Derivation  extends IDerivation<ParseForest>,
    Tree,
    Input       extends ImplodeInput>
//@formatter:on
    extends AbstractTreeImploder<ParseForest, ParseNode, Derivation, TreeImploder.SubTree<Tree>> {

    protected final IImplodeInputFactory<Input> inputFactory;
    protected final ITreeFactory<Tree> treeFactory;

    public TreeImploder(IImplodeInputFactory<Input> inputFactory, ITreeFactory<Tree> treeFactory) {
        this.inputFactory = inputFactory;
        this.treeFactory = treeFactory;
    }

    @Override public SubTree<Tree> implode(String input, String filename, ParseForest parseForest) {
        @SuppressWarnings("unchecked") ParseNode topParseNode = (ParseNode) parseForest;

        return implodeParseNode(inputFactory.get(input), topParseNode, 0);
    }

    protected SubTree<Tree> implodeParseNode(Input input, ParseNode parseNode, int startOffset) {
        parseNode = implodeInjection(parseNode);

        IProduction production = parseNode.production();

        if(production.isContextFree()) {
            List<Derivation> filteredDerivations = applyDisambiguationFilters(parseNode);

            if(filteredDerivations.size() > 1) {
                List<Tree> trees = new ArrayList<>(filteredDerivations.size());
                List<SubTree<Tree>> subTrees = new ArrayList<>(filteredDerivations.size());

                if(production.isList()) {
                    for(List<ParseForest> derivationParseForests : implodeAmbiguousLists(filteredDerivations)) {
                        SubTree<Tree> result = implodeDerivationChildren(input, production,
                            getChildParseForests(production, derivationParseForests), startOffset);
                        trees.add(result.tree);
                        subTrees.add(result);
                    }
                } else {
                    for(Derivation derivation : filteredDerivations) {
                        SubTree<Tree> result = implodeDerivation(input, derivation, startOffset);
                        trees.add(result.tree);
                        subTrees.add(result);
                    }
                }

                return new SubTree<>(treeFactory.createAmb(trees), subTrees, null, null, subTrees.get(0).width);
            } else
                return implodeDerivation(input, filteredDerivations.get(0), startOffset);
        } else {
            String substring = input.inputString.substring(startOffset, startOffset + parseNode.width());

            return new SubTree<>(createLexicalTerm(production, substring), production, substring);
        }
    }

    protected SubTree<Tree> implodeDerivation(Input input, Derivation derivation, int startOffset) {
        IProduction production = derivation.production();

        if(!production.isContextFree())
            throw new RuntimeException("non context free imploding not supported");

        return implodeDerivationChildren(input, production, getChildParseForests(derivation), startOffset);
    }

    protected SubTree<Tree> implodeDerivationChildren(Input input, IProduction production,
        List<ParseForest> childParseForests, int startOffset) {

        List<Tree> childASTs = new ArrayList<>();
        List<SubTree<Tree>> subTrees = new ArrayList<>();

        for(ParseForest childParseForest : childParseForests) {
            if(childParseForest != null) { // Can be null in the case of a layout subtree parse node that is not created
                @SuppressWarnings("unchecked") ParseNode childParseNode = (ParseNode) childParseForest;

                SubTree<Tree> subTree = this.implodeParseNode(input, childParseNode, startOffset);

                if(subTree.tree != null) {
                    childASTs.add(subTree.tree);
                }
                subTrees.add(subTree);
                startOffset += subTree.width;
            }
        }

        return new SubTree<>(createContextFreeTerm(production, childASTs), subTrees, production);
    }

    protected List<ParseForest> getChildParseForests(Derivation derivation) {
        return getChildParseForests(derivation.production(), Arrays.asList(derivation.parseForests()));
    }

    protected List<ParseForest> getChildParseForests(IProduction production, List<ParseForest> parseForests) {
        // Make sure lists are flattened
        if(production.isList()) {
            LinkedList<ParseForest> listQueueDone = new LinkedList<>();
            LinkedList<ParseForest> listQueueTodo = new LinkedList<>(parseForests);

            // Check child parse forest from front to back
            while(!listQueueTodo.isEmpty()) {
                ParseForest childParseForest = listQueueTodo.removeFirst();

                @SuppressWarnings("unchecked") ParseNode childParseNode = (ParseNode) childParseForest;

                IProduction childProduction = childParseNode.production();

                // If child is also a list, add all its children to the front of the unprocessed list
                if(childProduction.isList() && childProduction.constructor() == null) {
                    List<Derivation> filteredDerivations = applyDisambiguationFilters(childParseNode);
                    if(filteredDerivations.size() <= 1) {
                        listQueueTodo.addAll(0, Arrays.asList(filteredDerivations.get(0).parseForests()));
                        continue;
                    }
                }

                // Else, add child to processed list
                listQueueDone.add(childParseForest);
            }
            return listQueueDone;
        } else {
            return parseForests;
        }
    }

    protected Tree createLexicalTerm(IProduction production, String substring) {
        if(production.isLayout() || production.isLiteral()) {
            return null;
        } else if(production.isLexical()) {
            if(production.lhs() instanceof IMetaVarSymbol)
                return treeFactory.createMetaVar((IMetaVarSymbol) production.lhs(), substring);
            else
                return treeFactory.createStringTerminal(production.lhs(), substring);
        } else {
            throw new RuntimeException("invalid term type");
        }
    }

    protected Tree createContextFreeTerm(IProduction production, List<Tree> childASTs) {
        String constructor = production.constructor();

        if(constructor != null)
            return treeFactory.createNonTerminal(production.lhs(), constructor, childASTs);
        else if(production.isOptional())
            return treeFactory.createOptional(production.lhs(), childASTs);
        else if(production.isList())
            return treeFactory.createList(childASTs);
        else if(childASTs.size() == 1)
            return childASTs.get(0);
        else
            return treeFactory.createTuple(childASTs);
    }

    public static class SubTree<Tree> {

        public final Tree tree;
        public final List<SubTree<Tree>> children;
        public final IProduction production;
        public final String string; // Only set for lexical nodes.
        public final int width;

        public SubTree(Tree tree, List<SubTree<Tree>> children, IProduction production, String string, int width) {
            this.tree = tree;
            this.children = children;
            this.production = production;
            this.string = string;
            this.width = width;
        }

        /** This constructor infers the width from the sum of widths of its children. */
        public SubTree(Tree tree, List<SubTree<Tree>> children, IProduction production) {
            this(tree, children, production, null, sumWidth(children));
        }

        /** This constructor corresponds to a terminal/lexical node without children. */
        public SubTree(Tree tree, IProduction production, String string) {
            this(tree, Collections.emptyList(), production, string, string.length());
        }

        private static <Tree> int sumWidth(List<SubTree<Tree>> children) {
            int result = 0;
            for(SubTree child : children) {
                result += child.width;
            }
            return result;
        }

    }
}
