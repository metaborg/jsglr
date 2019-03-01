package org.spoofax.jsglr2.imploder;

import java.util.ArrayList;
import java.util.List;

import org.metaborg.parsetable.IProduction;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr2.layoutsensitive.LayoutSensitiveParseNode;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parser.AbstractParse;

public abstract class TokenizedTreeImploder
//@formatter:off
   <ParseForest extends AbstractParseForest,
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

        parse.tokens.makeStartToken(topParseNode);

        TokenizedSubTree<Tree> tree = implodeParseNode(parse, topParseNode, parse.tokens.startToken());

        parse.tokens.makeEndToken(topParseNode);

        tokenTreeBinding(parse.tokens.startToken(), tree.tree);
        tokenTreeBinding(parse.tokens.endToken(), tree.tree);

        return new ImplodeResult<>(parse, tree.tree);
    }

    static class TokenizedSubTree<Tree> {

        final Tree tree;
        final IToken leftToken, rightToken;

        TokenizedSubTree(Tree tree, IToken leftToken, IToken rightToken) {
            this.tree = tree;
            this.leftToken = leftToken;
            this.rightToken = rightToken;
        }

    }

    static class SubTreeTokens {

        IToken leftToken, rightToken;

        SubTreeTokens(IToken leftToken) {
            this.leftToken = leftToken;
            this.rightToken = null;
        }

    }

    protected TokenizedSubTree<Tree> implodeParseNode(AbstractParse<ParseForest, ?> parse, ParseNode parseNode,
        IToken parentLeftToken) {
        IProduction production = parseNodeProduction(parseNode);

        if(production.isContextFree()) {
            List<Derivation> filteredDerivations = applyDisambiguationFilters(parseNode);

            if(filteredDerivations.size() > 1) {
                List<Tree> trees = new ArrayList<>(filteredDerivations.size());
                TokenizedSubTree<Tree> tokenizedSubTree = null;

                for(Derivation derivation : filteredDerivations) {
                    if(tokenizedSubTree == null) {
                        tokenizedSubTree = implodeDerivation(parse, derivation, parentLeftToken);

                        trees.add(tokenizedSubTree.tree);
                    } else
                        trees.add(implodeDerivation(parse, derivation, parentLeftToken).tree);
                }

                String sort = production.sort();

                Tree tree = treeFactory.createAmb(sort, trees, tokenizedSubTree.leftToken, tokenizedSubTree.rightToken);

                return new TokenizedSubTree<>(tree, tokenizedSubTree.leftToken, tokenizedSubTree.rightToken);
            } else
                return implodeDerivation(parse, filteredDerivations.get(0), parentLeftToken);
        } else if(production.isLayout() || production.isLiteral()) {
            IToken token = parseNode.getStartPosition().offset < parseNode.getEndPosition().offset
                ? parse.tokens.makeToken(parseNode, production) : null;

            return new TokenizedSubTree<>(null, token, token);
        } else if(production.isLexical() || production.isLexicalRhs()) {
            IToken token = parseNode.getStartPosition().offset < parseNode.getEndPosition().offset
                ? parse.tokens.makeToken(parseNode, production) : null;

            Tree tree = createLexicalTerm(production,
                parse.getPart(parseNode.getStartPosition().offset, parseNode.getEndPosition().offset), token);

            return new TokenizedSubTree<>(tree, token, token);
        } else {
            throw new RuntimeException("invalid term type");
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

    protected TokenizedSubTree<Tree> implodeDerivation(AbstractParse<ParseForest, ?> parse, Derivation derivation,
        IToken parentLeftToken) {
        IProduction production = derivation.production();

        if(!production.isContextFree())
            throw new RuntimeException("non context free imploding not supported");

        List<Tree> childASTs = new ArrayList<>();
        List<IToken> unboundTokens = new ArrayList<>();

        SubTreeTokens subTreeTokens = implodeChildParseNodes(parse, childASTs, derivation, derivation.production(),
            unboundTokens, parentLeftToken);

        Tree tree = createContextFreeTerm(derivation.production(), childASTs, subTreeTokens.leftToken,
            subTreeTokens.rightToken);

        for(IToken token : unboundTokens)
            tokenTreeBinding(token, tree);

        return new TokenizedSubTree<>(tree, subTreeTokens.leftToken, subTreeTokens.rightToken);
    }

    protected SubTreeTokens implodeChildParseNodes(AbstractParse<ParseForest, ?> parse, List<Tree> childASTs,
        Derivation derivation, IProduction production, List<IToken> unboundTokens, IToken parentLeftToken) {
        ParseForest[] childParseForests = derivation.parseForests();
        SubTreeTokens subTreeTokens = new SubTreeTokens(parentLeftToken);
        IToken pivotToken = parentLeftToken;

        for(ParseForest parseForest : childParseForests) {
            @SuppressWarnings("unchecked") ParseNode parseNode = (ParseNode) parseForest;

            if(parseNode != null) { // Can be null in the case of a layout subtree parse node that is not created
                IProduction parseNodeProduction = parseNodeProduction(parseNode);

                if(production.isList() && (parseNodeProduction.isList() && parseNodeProduction.constructor() == null
                    && parseNodePreferredAvoidedDerivations(parseNode).size() <= 1)) {
                    // Make sure lists are flattened
                    SubTreeTokens listSubTreeTokens = implodeChildParseNodes(parse, childASTs,
                        parseNodeOnlyDerivation(parseNode), parseNodeProduction, unboundTokens, pivotToken);

                    if(subTreeTokens.leftToken == null)
                        subTreeTokens.leftToken = listSubTreeTokens.leftToken;
                    if(listSubTreeTokens.rightToken != null)
                        subTreeTokens.rightToken = listSubTreeTokens.rightToken;
                } else {
                    TokenizedSubTree<Tree> tokenizedSubTree = implodeParseNode(parse, parseNode, pivotToken);

                    if(tokenizedSubTree.tree != null)
                        childASTs.add(tokenizedSubTree.tree);

                    if(tokenizedSubTree.tree == null) {
                        if(tokenizedSubTree.leftToken != null)
                            unboundTokens.add(tokenizedSubTree.leftToken);

                        if(tokenizedSubTree.rightToken != null)
                            unboundTokens.add(tokenizedSubTree.rightToken);
                    }

                    if(!parseNodeProduction.isLayout()) {
                        if(subTreeTokens.leftToken == null)
                            subTreeTokens.leftToken = tokenizedSubTree.leftToken;
                        if(tokenizedSubTree.rightToken != null) {
                            subTreeTokens.rightToken = tokenizedSubTree.rightToken;
                            pivotToken = tokenizedSubTree.rightToken;
                        }
                    }
                }
            }
        }

        return subTreeTokens;
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
