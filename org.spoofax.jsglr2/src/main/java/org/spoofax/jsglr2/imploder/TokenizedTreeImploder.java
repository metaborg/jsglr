package org.spoofax.jsglr2.imploder;

import java.util.ArrayList;
import java.util.List;

import org.metaborg.parsetable.IProduction;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr2.layoutsensitive.LayoutSensitiveParseNode;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.Position;

public abstract class TokenizedTreeImploder
//@formatter:off
   <ParseForest extends IParseForest,
    ParseNode   extends ParseForest,
    Derivation  extends IDerivation<ParseForest>,
    Tree>
//@formatter:on
    implements IImploder<ParseForest, Tree> {

    protected final ITreeFactory<Tree> treeFactory;

    public TokenizedTreeImploder(ITreeFactory<Tree> treeFactory) {
        this.treeFactory = treeFactory;
    }

    @Override public ImplodeResult<ParseForest, Tree> implode(AbstractParse<ParseForest, ?> parse,
        ParseForest parseForest) {
        @SuppressWarnings("unchecked") ParseNode topParseNode = (ParseNode) parseForest;

        parse.tokens.makeStartToken();

        Position position = new Position(0, 1, 1);

        SubTree<Tree> tree = implodeParseNode(topParseNode, parse, position, parse.tokens.startToken());

        parse.tokens.makeEndToken(tree.endPosition);

        tokenTreeBinding(parse.tokens.startToken(), tree.tree);
        tokenTreeBinding(parse.tokens.endToken(), tree.tree);

        return new ImplodeResult<>(parse, tree.tree);
    }

    static class SubTree<Tree> {

        Tree tree;
        Position endPosition;
        IToken leftToken, rightToken;

        SubTree(Tree tree, Position endPosition, IToken leftToken, IToken rightToken) {
            this.tree = tree;
            this.endPosition = endPosition;
            this.leftToken = leftToken;
            this.rightToken = rightToken;
        }

    }

    protected SubTree<Tree> implodeParseNode(ParseNode parseNode, AbstractParse<ParseForest, ?> parse,
        Position startPosition, IToken parentLeftToken) {
        IProduction production = parseNodeProduction(parseNode);

        if(production.isContextFree()) {
            List<Derivation> filteredDerivations = applyDisambiguationFilters(parseNode);

            if(filteredDerivations.size() > 1) {
                List<Tree> trees = new ArrayList<>(filteredDerivations.size());
                SubTree<Tree> result = null;

                for(Derivation derivation : filteredDerivations) {
                    if(result == null) {
                        result = implodeDerivation(parse, derivation, startPosition, parentLeftToken);

                        trees.add(result.tree);
                    } else
                        trees.add(implodeDerivation(parse, derivation, startPosition, parentLeftToken).tree);
                }

                String sort = production.sort();

                result.tree = treeFactory.createAmb(sort, trees, result.leftToken, result.rightToken);

                return result;
            } else
                return implodeDerivation(parse, filteredDerivations.get(0), startPosition, parentLeftToken);
        } else {
            Position endPosition = startPosition.step(parse.inputString, parseNode.width());

            IToken token =
                parseNode.width() > 0 ? parse.tokens.makeToken(startPosition, endPosition, production) : null;

            Tree tree;

            if(production.isLayout() || production.isLiteral()) {
                tree = null;
            } else if(production.isLexical() || production.isLexicalRhs()) {
                tree = createLexicalTerm(production, parse.tokens.toString(startPosition.offset, endPosition.offset),
                    token);
            } else {
                throw new RuntimeException("invalid term type");
            }

            return new SubTree<>(tree, endPosition, token, token);
        }
    }

    protected List<Derivation> applyDisambiguationFilters(ParseNode parseNode) {
        List<Derivation> result;
        // TODO always filter longest-match?
        if(parseNode instanceof LayoutSensitiveParseNode) {
            ((LayoutSensitiveParseNode) parseNode).filterLongestMatchDerivations();
        }
        // TODO always filter prefer/avoid?
        result = parseNodePreferredAvoidedDerivations(parseNode);

        return result;
    }

    protected SubTree<Tree> implodeDerivation(AbstractParse<ParseForest, ?> parse, Derivation derivation,
        Position startPosition, IToken parentLeftToken) {
        IProduction production = derivation.production();

        if(!production.isContextFree())
            throw new RuntimeException("non context free imploding not supported");

        List<Tree> childASTs = new ArrayList<>();
        List<IToken> unboundTokens = new ArrayList<>();

        SubTree<Tree> subTree = implodeChildParseNodes(parse, childASTs, derivation, derivation.production(),
            unboundTokens, startPosition, parentLeftToken);

        subTree.tree = createContextFreeTerm(derivation.production(), childASTs, subTree.leftToken, subTree.rightToken);

        for(IToken token : unboundTokens)
            tokenTreeBinding(token, subTree.tree);

        return subTree;
    }

    protected SubTree<Tree> implodeChildParseNodes(AbstractParse<ParseForest, ?> parse, List<Tree> childASTs,
        Derivation derivation, IProduction production, List<IToken> unboundTokens, Position startPosition,
        IToken parentLeftToken) {
        SubTree<Tree> result = new SubTree<>(null, startPosition, parentLeftToken, null);

        Position pivotPosition = startPosition;
        IToken pivotToken = parentLeftToken;

        for(ParseForest parseForest : derivation.parseForests()) {
            @SuppressWarnings("unchecked") ParseNode parseNode = (ParseNode) parseForest;

            if(parseNode != null) { // Can be null in the case of a layout subtree parse node that is not created
                IProduction parseNodeProduction = parseNodeProduction(parseNode);

                SubTree<Tree> subTree;

                if(production.isList() && (parseNodeProduction.isList() && parseNodeProduction.constructor() == null
                    && parseNodePreferredAvoidedDerivations(parseNode).size() <= 1)) {
                    // Make sure lists are flattened
                    subTree = implodeChildParseNodes(parse, childASTs, parseNodeOnlyDerivation(parseNode),
                        parseNodeProduction, unboundTokens, pivotPosition, pivotToken);
                } else {
                    subTree = implodeParseNode(parseNode, parse, pivotPosition, pivotToken);

                    if(subTree.tree != null)
                        childASTs.add(subTree.tree);

                    // Collect tokens that are not bound to a tree such that they can later be bound to the resulting
                    // parent tree
                    if(subTree.tree == null) {
                        if(subTree.leftToken != null)
                            unboundTokens.add(subTree.leftToken);

                        if(subTree.rightToken != null)
                            unboundTokens.add(subTree.rightToken);
                    }
                }

                // Set the parent tree left and right token from the outermost non-layout left and right child tokens
                if(!parseNodeProduction.isLayout()) {
                    if(result.leftToken == null)
                        result.leftToken = subTree.leftToken;

                    if(subTree.rightToken != null) {
                        result.rightToken = subTree.rightToken;
                        pivotToken = subTree.rightToken;
                    }
                }

                pivotPosition = subTree.endPosition;
            }
        }

        result.endPosition = pivotPosition;

        return result;
    }

    protected Tree createContextFreeTerm(IProduction production, List<Tree> childASTs, IToken leftToken,
        IToken rightToken) {
        String constructor = production.constructor();

        if(production.isList())
            return treeFactory.createList(production.sort(), childASTs, leftToken, rightToken);
        else if(production.isOptional())
            return treeFactory.createOptional(production.sort(), childASTs, leftToken, rightToken);
        else if(constructor != null)
            return treeFactory.createNonTerminal(production.sort(), constructor, childASTs, leftToken, rightToken);
        else if(childASTs.size() == 1)
            return childASTs.get(0);
        else
            return treeFactory.createTuple(production.sort(), childASTs, leftToken, rightToken);
    }

    protected Tree createLexicalTerm(IProduction production, String lexicalString, IToken lexicalToken) {
        Tree lexicalTerm = treeFactory.createStringTerminal(production.sort(), lexicalString, lexicalToken);

        if(lexicalToken != null) // Can be null, e.g. for empty string lexicals
            tokenTreeBinding(lexicalToken, lexicalTerm);

        return lexicalTerm;
    }

    protected abstract void tokenTreeBinding(IToken token, Tree tree);

    protected abstract IProduction parseNodeProduction(ParseNode parseNode);

    protected abstract Derivation parseNodeOnlyDerivation(ParseNode parseNode);

    protected abstract List<Derivation> parseNodePreferredAvoidedDerivations(ParseNode parseNode);

    protected abstract List<Derivation> longestMatchedDerivations(List<Derivation> derivations);

}
