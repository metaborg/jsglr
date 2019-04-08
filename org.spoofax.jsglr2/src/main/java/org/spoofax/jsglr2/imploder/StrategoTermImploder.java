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
import org.spoofax.jsglr2.parser.Position;
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

        Position position = new Position(0, 1, 1);

        SubTree tree = implodeParseNode(input, topParseNode, position);

        Tokens tokens = new Tokens(input, filename);
        return new ImplodeResult<>(tokens, tokenizer.tokenize(tokens, tree));
    }

    static class SubTree {

        final IStrategoTerm tree;
        final List<SubTree> children;
        final IProduction production;
        final Position startPosition;
        final Position endPosition;

        SubTree(IStrategoTerm tree, List<SubTree> children, IProduction production, Position startPosition,
            Position endPosition) {
            this.tree = tree;
            this.children = children;
            this.production = production;
            this.startPosition = startPosition;
            this.endPosition = endPosition;
        }

        SubTree(IStrategoTerm tree, List<SubTree> children, IProduction production) {
            this(tree, children, production, children.get(0).startPosition,
                children.get(children.size() - 1).endPosition);
        }

        SubTree(IStrategoTerm tree, IProduction production, Position startPosition, Position endPosition) {
            this(tree, Collections.emptyList(), production, startPosition, endPosition);
        }

    }

    protected SubTree implodeParseNode(String inputString, ParseNode parseNode, Position startPosition) {
        IProduction production = parseNode.production();

        if(production.isContextFree()) {
            List<Derivation> filteredDerivations = applyDisambiguationFilters(parseNode);

            if(filteredDerivations.size() > 1) {
                List<IStrategoTerm> trees = new ArrayList<>(filteredDerivations.size());
                List<SubTree> subTrees = new ArrayList<>(filteredDerivations.size());

                for(Derivation derivation : filteredDerivations) {
                    SubTree result = implodeDerivation(inputString, derivation, startPosition);
                    trees.add(result.tree);
                    subTrees.add(result);
                }

                return new SubTree(treeFactory.createAmb(production.sort(), trees), subTrees, production);
            } else
                return implodeDerivation(inputString, filteredDerivations.get(0), startPosition);
        } else {
            Position endPosition = startPosition.step(inputString, parseNode.width());

            IStrategoTerm tree =
                createLexicalTerm(production, inputString.substring(startPosition.offset, endPosition.offset));

            return new SubTree(tree, production, startPosition, endPosition);
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

    protected SubTree implodeDerivation(String inputString, Derivation derivation, Position startPosition) {
        IProduction production = derivation.production();

        if(!production.isContextFree())
            throw new RuntimeException("non context free imploding not supported");

        List<IStrategoTerm> childASTs = new ArrayList<>();
        List<SubTree> subTrees = new ArrayList<>();

        Position endPosition = implodeChildParseNodes(inputString, childASTs, subTrees, derivation,
            derivation.production(), startPosition);

        return new SubTree(createContextFreeTerm(derivation.production(), childASTs), subTrees, production,
            startPosition, endPosition);
    }

    // TODO make this thing iterative
    protected Position implodeChildParseNodes(String inputString, List<IStrategoTerm> childASTs, List<SubTree> subTrees,
        Derivation derivation, IProduction production, Position startPosition) {

        Position pivotPosition = startPosition;

        for(ParseForest childParseForest : derivation.parseForests()) {
            if(childParseForest != null) { // Can be null in the case of a layout subtree parse node that is not created
                @SuppressWarnings("unchecked") ParseNode childParseNode = (ParseNode) childParseForest;

                IProduction childProduction = childParseNode.production();

                if(production.isList() && childProduction.isList() && childProduction.constructor() == null
                    && childParseNode.getPreferredAvoidedDerivations().size() <= 1) {
                    // Make sure lists are flattened
                    pivotPosition = implodeChildParseNodes(inputString, childASTs, subTrees,
                        childParseNode.getFirstDerivation(), childProduction, pivotPosition);
                } else {
                    SubTree subTree = implodeParseNode(inputString, childParseNode, pivotPosition);

                    if(subTree.tree != null) {
                        childASTs.add(subTree.tree);
                    }
                    subTrees.add(subTree);
                    pivotPosition = subTree.endPosition;
                }
            }
        }
        return pivotPosition;
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
