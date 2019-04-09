package org.spoofax.jsglr2.imploder;

import static org.spoofax.interpreter.terms.IStrategoTerm.MUTABLE;

import java.util.*;

import org.metaborg.parsetable.IProduction;
import org.metaborg.util.iterators.Iterables2;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.layoutsensitive.LayoutSensitiveParseNode;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.tokens.Tokens;
import org.spoofax.terms.TermFactory;

public class StrategoTermImploder
//@formatter:off
   <ParseForest extends IParseForest,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    Derivation  extends IDerivation<ParseForest>>
//@formatter:on
    implements IImploder<ParseForest, IStrategoTerm> {

    protected final ITreeFactory<IStrategoTerm> treeFactory =
        new StrategoTermTreeFactory(new TermFactory().getFactoryWithStorageType(MUTABLE));
    private StrategoTermTokenizer tokenizer = new StrategoTermTokenizer();

    @Override public ImplodeResult<IStrategoTerm> implode(String input, String filename, ParseForest parseForest) {
        @SuppressWarnings("unchecked") ParseNode topParseNode = (ParseNode) parseForest;

        SubTree tree = implodeParseNode(input, topParseNode, 0);

        Tokens tokens = new Tokens(input, filename);
        return new ImplodeResult<>(tokens, tokenizer.tokenize(tokens, tree));
    }

    static class SubTree {

        final IStrategoTerm tree;
        final List<SubTree> children;
        final IProduction production;
        final String string; // Only set for lexical nodes.
        final int width;

        SubTree(IStrategoTerm tree, List<SubTree> children, IProduction production, String string, int width) {
            this.tree = tree;
            this.children = children;
            this.production = production;
            this.string = string;
            this.width = width;
        }

        /** This constructor infers the width from the sum of widths of its children. */
        SubTree(IStrategoTerm tree, List<SubTree> children, IProduction production) {
            this(tree, children, production, null, sumWidth(children));
        }

        /** This constructor corresponds to a terminal/lexical node without children. */
        SubTree(IStrategoTerm tree, IProduction production, String string) {
            this(tree, Collections.emptyList(), production, string, string.length());
        }

        private static int sumWidth(List<SubTree> children) {
            int result = 0;
            for(SubTree child : children) {
                result += child.width;
            }
            return result;
        }

    }

    protected SubTree implodeParseNode(String inputString, ParseNode parseNode, int startOffset) {
        IProduction production = parseNode.production();

        if(production.isContextFree()) {
            List<Derivation> filteredDerivations = applyDisambiguationFilters(parseNode);

            if(filteredDerivations.size() > 1) {
                List<IStrategoTerm> trees = new ArrayList<>(filteredDerivations.size());
                List<SubTree> subTrees = new ArrayList<>(filteredDerivations.size());

                for(Derivation derivation : filteredDerivations) {
                    SubTree result = implodeDerivation(inputString, derivation, startOffset);
                    trees.add(result.tree);
                    subTrees.add(result);
                }

                return new SubTree(treeFactory.createAmb(production.sort(), trees), subTrees, null, null,
                    subTrees.get(0).width);
            } else
                return implodeDerivation(inputString, filteredDerivations.get(0), startOffset);
        } else {
            String substring = inputString.substring(startOffset, startOffset + parseNode.width());

            return new SubTree(createLexicalTerm(production, substring), production, substring);
        }
    }

    protected SubTree implodeDerivation(String inputString, Derivation derivation, int startOffset) {
        IProduction production = derivation.production();

        if(!production.isContextFree())
            throw new RuntimeException("non context free imploding not supported");

        List<IStrategoTerm> childASTs = new ArrayList<>();
        List<SubTree> subTrees = new ArrayList<>();

        for(ParseForest childParseForest : getChildParseForests(derivation)) {
            if(childParseForest != null) { // Can be null in the case of a layout subtree parse node that is not created
                @SuppressWarnings("unchecked") ParseNode childParseNode = (ParseNode) childParseForest;

                SubTree subTree = implodeParseNode(inputString, childParseNode, startOffset);

                if(subTree.tree != null) {
                    childASTs.add(subTree.tree);
                }
                subTrees.add(subTree);
                startOffset += subTree.width;
            }
        }

        return new SubTree(createContextFreeTerm(production, childASTs), subTrees, derivation.production());
    }

    protected Iterable<ParseForest> getChildParseForests(Derivation derivation) {
        // Make sure lists are flattened
        if(derivation.production().isList()) {
            LinkedList<ParseForest> listQueueTodo = new LinkedList<>();
            LinkedList<ParseForest> listQueueDone = new LinkedList<>();
            Collections.addAll(listQueueTodo, derivation.parseForests());

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
            return Iterables2.from(derivation.parseForests());
        }
    }

    protected List<Derivation> applyDisambiguationFilters(ParseNode parseNode) {
        List<Derivation> result;
        // TODO always filter longest-match?
        if(parseNode instanceof LayoutSensitiveParseNode) {
            ((LayoutSensitiveParseNode) parseNode).filterLongestMatchDerivations();
        }
        // TODO always filter prefer/avoid?
        result = parseNode.getPreferredAvoidedDerivations();

        return result;
    }

    protected IStrategoTerm createLexicalTerm(IProduction production, String substring) {
        if(production.isLayout() || production.isLiteral()) {
            return null;
        } else if(production.isLexical() || production.isLexicalRhs()) {
            return treeFactory.createStringTerminal(production.sort(), substring);
        } else {
            throw new RuntimeException("invalid term type");
        }
    }

    protected IStrategoTerm createContextFreeTerm(IProduction production, List<IStrategoTerm> childASTs) {
        String constructor = production.constructor();

        if(production.isList())
            return treeFactory.createList(production.sort(), childASTs);
        else if(production.isOptional())
            return treeFactory.createOptional(production.sort(), childASTs);
        else if(constructor != null)
            return treeFactory.createNonTerminal(production.sort(), constructor, childASTs);
        else if(childASTs.size() == 1)
            return childASTs.get(0);
        else
            return treeFactory.createTuple(production.sort(), childASTs);
    }

}
