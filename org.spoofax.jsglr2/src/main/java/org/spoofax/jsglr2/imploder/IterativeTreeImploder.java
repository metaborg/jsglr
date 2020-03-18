package org.spoofax.jsglr2.imploder;

import static java.util.Collections.singletonList;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.util.iterators.Iterables2;
import org.spoofax.jsglr2.imploder.input.IImplodeInputFactory;
import org.spoofax.jsglr2.imploder.input.ImplodeInput;
import org.spoofax.jsglr2.imploder.treefactory.ITreeFactory;
import org.spoofax.jsglr2.parseforest.ICharacterNode;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;

import com.google.common.collect.Lists;

public class IterativeTreeImploder
//@formatter:off
   <ParseForest extends IParseForest,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    Derivation  extends IDerivation<ParseForest>,
    Cache,
    Tree,
    Input       extends ImplodeInput>
//@formatter:on
    extends TreeImploder<ParseForest, ParseNode, Derivation, Cache, Tree, Input> {

    public IterativeTreeImploder(IImplodeInputFactory<Input> inputFactory, ITreeFactory<Tree> treeFactory) {
        super(inputFactory, treeFactory);
    }

    private class PseudoNode {
        final ParseNode parseNode;
        final List<Derivation> derivations; // Regular node
        final IProduction production; // List node
        final boolean isListNode;
        final int beginOffset;
        int pivotOffset;

        PseudoNode(ParseNode parseNode, List<Derivation> derivations, int beginOffset) {
            this.parseNode = parseNode;
            this.derivations = derivations;
            this.production = null;
            this.beginOffset = beginOffset;
            this.pivotOffset = beginOffset;
            this.isListNode = false;
        }

        PseudoNode(ParseNode parseNode, IProduction production, int beginOffset) {
            this.parseNode = parseNode;
            this.derivations = null;
            this.production = production;
            this.beginOffset = beginOffset;
            this.pivotOffset = beginOffset;
            this.isListNode = true;
        }
    }

    @Override protected SubTree<Tree> implodeParseNode(Input input, ParseForest rootForest, int startOffset) {
        @SuppressWarnings("unchecked") ParseNode rootNode = (ParseNode) rootForest;

        // This stack contains the parse nodes that we still need to process
        Stack<PseudoNode> parseNodeStack = new Stack<>();
        // These stack elements contain: one list for each derivation, and per derivation: one list for all subtrees.
        // The elements on the input stack are processed from the front,
        // after which they are pushed to the back of the elements on the output stack.
        Stack<LinkedList<LinkedList<ParseForest>>> inputStack = new Stack<>();
        Stack<LinkedList<LinkedList<SubTree<Tree>>>> outputStack = new Stack<>();

        parseNodeStack.add(new PseudoNode(rootNode, applyDisambiguationFilters(rootNode), startOffset));
        inputStack.add(newNestedList(rootForest));
        outputStack.add(newNestedList());

        while(true) {
            PseudoNode pseudoNode = parseNodeStack.peek();
            LinkedList<LinkedList<ParseForest>> currentIn = inputStack.peek();
            LinkedList<LinkedList<SubTree<Tree>>> currentOut = outputStack.peek();

            if(currentIn.getFirst().isEmpty()) { // If we're finished with the current derivation
                currentIn.removeFirst(); // Remove the current derivation
                if(currentIn.isEmpty()) { // If the stack entry is now empty
                    inputStack.pop(); // That means it's done, so remove it from the stack
                    if(inputStack.isEmpty()) // If it was the last stack node, we're done
                        break;
                    parseNodeStack.pop(); // Also remove the current pseudo node
                    outputStack.pop(); // Also remove `currentOut` from stack

                    // Merge resulting SubTrees from `currentOut` into one SubTree
                    SubTree<Tree> possiblyAmbiguousSubTree = pseudoNode.isListNode
                        // In the case of a list node, use the production of the pseudoNode
                        ? createPossiblyAmbiguousSubTree(pseudoNode.parseNode,
                            currentOut.stream()
                                .map(subTrees -> createNonTerminalSubTree(pseudoNode.production, subTrees))
                                .collect(Collectors.toList()))
                        // In the case of a regular node, use the productions of the derivations
                        : createPossiblyAmbiguousSubTree(pseudoNode.parseNode, Lists.newArrayList(Iterables2.zip(
                            pseudoNode.derivations, currentOut,
                            (derivation, subTrees) -> createNonTerminalSubTree(derivation.production(), subTrees))));
                    outputStack.peek().getLast().add(possiblyAmbiguousSubTree); // And add it to the output
                    parseNodeStack.peek().pivotOffset += possiblyAmbiguousSubTree.width;
                } else { // If the stack entry is not yet empty, that means we're processing an alternate derivation
                    currentOut.add(new LinkedList<>()); // Initialize a new derivation list in the output
                    pseudoNode.pivotOffset = pseudoNode.beginOffset; // And reset the pivotOffset
                }
            } else {
                ParseForest parseForest = currentIn.getFirst().removeFirst(); // Process the next parse node
                if(parseForest instanceof ICharacterNode) {
                    SubTree<Tree> characterSubTree =
                        new SubTree<>(treeFactory.createCharacterTerminal(((ICharacterNode) parseForest).character()),
                            null, parseForest.width());
                    currentOut.getLast().add(characterSubTree);
                    pseudoNode.pivotOffset += characterSubTree.width;
                } else {
                    @SuppressWarnings("unchecked") ParseNode parseNode = implodeInjection(((ParseNode) parseForest));

                    IProduction production = parseNode.production();
                    if(!production.isContextFree() || production.isSkippableInParseForest()) {
                        // If the current parse node is a lexical node or a skipped node
                        SubTree<Tree> lexicalSubTree =
                            createLexicalSubTree(input.inputString, parseNode, pseudoNode.pivotOffset, production);
                        currentOut.getLast().add(lexicalSubTree); // Add a new SubTree to the output
                        pseudoNode.pivotOffset += lexicalSubTree.width;
                    } else {
                        // If the current parse node is a context-free node
                        // Then push it on top of the stacks
                        List<Derivation> derivations = applyDisambiguationFilters(parseNode);
                        if(derivations.size() > 1 && production.isList()) {
                            inputStack.add(getDerivationListsForListNode(production, derivations));
                            parseNodeStack.add(new PseudoNode(parseNode, production, pseudoNode.pivotOffset));
                        } else {
                            inputStack.add(getDerivationLists(derivations));
                            parseNodeStack.add(new PseudoNode(parseNode, derivations, pseudoNode.pivotOffset));
                        }
                        outputStack.add(newNestedList());
                    }
                }
            }
        }
        return outputStack.peek().getFirst().getFirst();
    }

    private LinkedList<LinkedList<ParseForest>> getDerivationLists(List<Derivation> derivations) {
        LinkedList<LinkedList<ParseForest>> derivationLists = new LinkedList<>();
        for(Derivation derivation : derivations) {
            if(!derivation.production().isContextFree())
                throw new RuntimeException("non context free imploding of Derivations not supported");

            derivationLists.add(new LinkedList<>(getChildParseForests(derivation)));
        }
        return derivationLists;
    }

    private LinkedList<LinkedList<ParseForest>> getDerivationListsForListNode(IProduction production,
        List<Derivation> derivations) {
        LinkedList<LinkedList<ParseForest>> derivationLists = new LinkedList<>();
        for(List<ParseForest> derivationParseForests : implodeAmbiguousLists(derivations)) {
            derivationLists.add(new LinkedList<>(getChildParseForests(production, derivationParseForests)));
        }
        return derivationLists;
    }

    private SubTree<Tree> createPossiblyAmbiguousSubTree(ParseNode parseNode, List<SubTree<Tree>> subTrees) {
        if(subTrees.size() > 1) {
            return createAmbiguousSubTree(parseNode, subTrees);
        } else
            return subTrees.get(0);
    }

    private SubTree<Tree> createAmbiguousSubTree(ParseNode parseNode, List<SubTree<Tree>> subTrees) {
        return new SubTree<>(treeFactory.createAmb(subTrees.stream().map(t -> t.tree)::iterator), subTrees, null,
            subTrees.get(0).width, false);
    }

    private SubTree<Tree> createNonTerminalSubTree(IProduction production, List<SubTree<Tree>> subTrees) {
        List<Tree> childASTs =
            subTrees.stream().filter(t -> t.tree != null).map(t -> t.tree).collect(Collectors.toList());
        Tree contextFreeTerm = createContextFreeTerm(production, childASTs);
        return new SubTree<>(contextFreeTerm, subTrees, production,
            childASTs.size() == 1 && contextFreeTerm == childASTs.get(0));
    }

    private SubTree<Tree> createLexicalSubTree(String inputString, ParseNode parseNode, int startOffset,
        IProduction production) {
        int width = parseNode.width();

        return new SubTree<>(createLexicalTerm(production, inputString, startOffset, width), production, width);
    }

    @SafeVarargs private static <E> LinkedList<LinkedList<E>> newNestedList(E... elements) {
        return new LinkedList<>(singletonList(new LinkedList<>(Arrays.asList(elements))));
    }
}
