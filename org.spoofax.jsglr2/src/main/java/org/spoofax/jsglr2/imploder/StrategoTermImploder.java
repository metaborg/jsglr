package org.spoofax.jsglr2.imploder;

import static org.spoofax.interpreter.terms.IStrategoTerm.MUTABLE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.metaborg.parsetable.IProduction;
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

    protected SubTree implodeDerivation(String inputString, Derivation derivation, int startOffset) {
        IProduction production = derivation.production();

        if(!production.isContextFree())
            throw new RuntimeException("non context free imploding not supported");

        List<IStrategoTerm> childASTs = new ArrayList<>();
        List<SubTree> subTrees = new ArrayList<>();

        implodeChildParseNodes(inputString, childASTs, subTrees, derivation, derivation.production(), startOffset);

        return new SubTree(createContextFreeTerm(derivation.production(), childASTs), subTrees, production);
    }

    // TODO make this thing iterative
    protected int implodeChildParseNodes(String inputString, List<IStrategoTerm> childASTs, List<SubTree> subTrees,
        Derivation derivation, IProduction production, int startOffset) {

        int pivotOffset = startOffset;

        for(ParseForest childParseForest : derivation.parseForests()) {
            if(childParseForest != null) { // Can be null in the case of a layout subtree parse node that is not created
                @SuppressWarnings("unchecked") ParseNode childParseNode = (ParseNode) childParseForest;

                IProduction childProduction = childParseNode.production();

                if(production.isList() && childProduction.isList() && childProduction.constructor() == null
                    && childParseNode.getPreferredAvoidedDerivations().size() <= 1) {
                    // Make sure lists are flattened
                    pivotOffset = implodeChildParseNodes(inputString, childASTs, subTrees,
                        childParseNode.getFirstDerivation(), childProduction, pivotOffset);
                } else {
                    SubTree subTree = implodeParseNode(inputString, childParseNode, pivotOffset);

                    if(subTree.tree != null) {
                        childASTs.add(subTree.tree);
                    }
                    subTrees.add(subTree);
                    pivotOffset += subTree.width;
                }
            }
        }
        return pivotOffset;
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
