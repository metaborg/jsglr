package org.spoofax.jsglr2.imploder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.symbols.IMetaVarSymbol;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr2.JSGLR2Request;
import org.spoofax.jsglr2.imploder.treefactory.ITokenizedTreeFactory;
import org.spoofax.jsglr2.parseforest.ICharacterNode;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.tokens.Tokens;

public abstract class TokenizedTreeImploder
//@formatter:off
   <ParseForest extends IParseForest,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    Derivation  extends IDerivation<ParseForest>,
    Tree>
//@formatter:on
    extends
    AbstractTreeImploder<ParseForest, ParseNode, Derivation, Tokens, Void, Tree, ImplodeResult<Tokens, Void, Tree>> {

    protected final ITokenizedTreeFactory<Tree> treeFactory;

    public TokenizedTreeImploder(ITokenizedTreeFactory<Tree> treeFactory) {
        this.treeFactory = treeFactory;
    }

    @Override public ImplodeResult<Tokens, Void, Tree> implode(JSGLR2Request request, ParseForest parseForest,
        Void resultCache) {
        Tokens tokens = new Tokens(request.input, request.fileName);
        tokens.makeStartToken();

        Position position = Position.START_POSITION;

        SubTree<Tree> tree = implodeParseNode(parseForest, tokens, position);

        tokens.makeEndToken(tree.endPosition);

        tokenTreeBinding(tokens.startToken(), tree.tree);
        tokenTreeBinding(tokens.endToken(), tree.tree);

        return new ImplodeResult<>(tokens, null, tree.tree);
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

    protected SubTree<Tree> implodeParseNode(ParseForest parseForest, Tokens tokens, Position startPosition) {
        if(parseForest instanceof ICharacterNode) {
            int width = parseForest.width();
            Position endPosition = startPosition.step(tokens.getInput(), width);
            IToken token = tokens.makeToken(startPosition, endPosition, null);
            Tree tree = createCharacterTerm(((ICharacterNode) parseForest).character(), token);
            return new SubTree<>(tree, endPosition, token, token);
        }

        @SuppressWarnings("unchecked") ParseNode parseNode = (ParseNode) parseForest;

        parseNode = implodeInjection(parseNode);

        IProduction production = parseNode.production();

        if(production.isContextFree() && !production.isSkippableInParseForest()) {
            List<Derivation> filteredDerivations = applyDisambiguationFilters(parseNode);

            if(filteredDerivations.size() > 1) {
                List<Tree> trees = new ArrayList<>(filteredDerivations.size());
                SubTree<Tree> result = null;

                if(production.isList()) {
                    for(List<ParseForest> derivationParseForests : implodeAmbiguousLists(filteredDerivations)) {
                        if(result == null) {
                            result = implodeListDerivation(tokens, production, derivationParseForests, startPosition);

                            trees.add(result.tree);
                        } else
                            trees.add(
                                implodeListDerivation(tokens, production, derivationParseForests, startPosition).tree);
                    }
                } else {
                    for(Derivation derivation : filteredDerivations) {
                        if(result == null) {
                            result = implodeDerivation(tokens, derivation, startPosition);

                            trees.add(result.tree);
                        } else
                            trees.add(implodeDerivation(tokens, derivation, startPosition).tree);
                    }
                }

                result.tree = treeFactory.createAmb(trees, result.leftToken, result.rightToken);

                return result;
            } else
                return implodeDerivation(tokens, filteredDerivations.get(0), startPosition);
        } else {
            int width = parseNode.width();

            Position endPosition = startPosition.step(tokens.getInput(), width);

            IToken token = width > 0 ? tokens.makeToken(startPosition, endPosition, production) : null;

            Tree tree;

            if(production.isLayout() || production.isLiteral()) {
                tree = null;
            } else if(production.isLexical()) {
                tree = createLexicalTerm(production, tokens.toString(startPosition.offset, endPosition.offset), token);
            } else {
                throw new RuntimeException("invalid term type");
            }

            return new SubTree<>(tree, endPosition, token, token);
        }
    }

    protected SubTree<Tree> implodeDerivation(Tokens tokens, Derivation derivation, Position startPosition) {
        IProduction production = derivation.production();

        if(!production.isContextFree())
            throw new RuntimeException("non context free imploding not supported");

        List<Tree> childASTs = new ArrayList<>();
        List<IToken> unboundTokens = new ArrayList<>();

        SubTree<Tree> subTree = implodeChildParseNodes(tokens, childASTs, Arrays.asList(derivation.parseForests()),
            derivation.production(), unboundTokens, startPosition);

        subTree.tree = createContextFreeTerm(derivation.production(), childASTs, subTree.leftToken, subTree.rightToken);

        for(IToken token : unboundTokens)
            tokenTreeBinding(token, subTree.tree);

        return subTree;
    }

    protected SubTree<Tree> implodeListDerivation(Tokens tokens, IProduction production,
        List<ParseForest> childParseForests, Position startPosition) {
        List<Tree> childASTs = new ArrayList<>();
        List<IToken> unboundTokens = new ArrayList<>();

        SubTree<Tree> subTree =
            implodeChildParseNodes(tokens, childASTs, childParseForests, production, unboundTokens, startPosition);

        subTree.tree = createContextFreeTerm(production, childASTs, subTree.leftToken, subTree.rightToken);

        for(IToken token : unboundTokens)
            tokenTreeBinding(token, subTree.tree);

        return subTree;
    }

    protected SubTree<Tree> implodeChildParseNodes(Tokens tokens, List<Tree> childASTs,
        Iterable<ParseForest> childParseForests, IProduction production, List<IToken> unboundTokens,
        Position startPosition) {
        SubTree<Tree> result = new SubTree<>(null, startPosition, null, null);

        Position pivotPosition = startPosition;

        for(ParseForest childParseForest : childParseForests) {
            @SuppressWarnings("unchecked") ParseNode childParseNode =
                childParseForest instanceof IParseNode ? (ParseNode) childParseForest : null;
            IProduction childProduction = childParseNode != null ? childParseNode.production() : null;

            SubTree<Tree> subTree;

            if(production.isList() && childProduction != null && (
            //@formatter:off
                // Constraints for flattening nested lists productions:
                childProduction.isList() && // The subtree is a list
                childProduction.constructor() == null && // The subtree has no constructor
                childParseNode.getPreferredAvoidedDerivations().size() <= 1 && // The subtree is not ambiguous
                !production.isLexical() // Not in lexical context; otherwise just implode as lexical token
            //@formatter:on
            )) {
                // Make sure lists are flattened
                subTree = implodeChildParseNodes(tokens, childASTs,
                    Arrays.asList(childParseNode.getFirstDerivation().parseForests()), childProduction, unboundTokens,
                    pivotPosition);
            } else {
                subTree = implodeParseNode(childParseForest, tokens, pivotPosition);

                if(subTree.tree != null)
                    childASTs.add(subTree.tree);

                // Collect tokens that are not bound to a tree such that they can later be bound to the resulting
                // parent tree
                if(subTree.tree == null) {
                    if(subTree.leftToken != null)
                        unboundTokens.add(subTree.leftToken);

                    // Make sure that if subTree.leftToken == subTree.rightToken it is not considered twice
                    if(subTree.rightToken != null && subTree.rightToken != subTree.leftToken)
                        unboundTokens.add(subTree.rightToken);
                }
            }

            // Set the parent tree left and right token from the outermost non-layout left and right child tokens
            if(childProduction != null && !childProduction.isLayout()
                // Also do this for character nodes
                || childParseNode == null) {
                if(result.leftToken == null)
                    result.leftToken = subTree.leftToken;

                if(subTree.rightToken != null) {
                    result.rightToken = subTree.rightToken;
                }
            }

            pivotPosition = subTree.endPosition;
        }

        // If is no token, this means that this AST has no characters in the input.
        // In this case, create an empty token to associate with this AST node.
        if(result.leftToken == null) {
            assert result.rightToken == null;
            result.leftToken = result.rightToken = tokens.makeToken(startPosition, pivotPosition, production);
            unboundTokens.add(result.leftToken);
        }

        result.endPosition = pivotPosition;

        return result;
    }

    protected Tree createContextFreeTerm(IProduction production, List<Tree> childASTs, IToken leftToken,
        IToken rightToken) {
        String constructor = production.constructor();

        if(constructor != null)
            return treeFactory.createNonTerminal(production.lhs(), constructor, childASTs, leftToken, rightToken);
        else if(production.isOptional())
            return treeFactory.createOptional(production.lhs(), childASTs, leftToken, rightToken);
        else if(production.isList())
            return treeFactory.createList(childASTs, leftToken, rightToken);
        else if(childASTs.size() == 1)
            return treeFactory.createInjection(production.lhs(), childASTs.get(0), production.isBracket());
        else
            return treeFactory.createTuple(childASTs, leftToken, rightToken);
    }

    protected Tree createLexicalTerm(IProduction production, String lexicalString, IToken lexicalToken) {
        Tree lexicalTerm;

        if(production.lhs() instanceof IMetaVarSymbol)
            lexicalTerm = treeFactory.createMetaVar((IMetaVarSymbol) production.lhs(), lexicalString, lexicalToken);
        else
            lexicalTerm = treeFactory.createStringTerminal(production.lhs(), lexicalString, lexicalToken);

        if(lexicalToken != null) // Can be null, e.g. for empty string lexicals
            tokenTreeBinding(lexicalToken, lexicalTerm);

        return lexicalTerm;
    }

    protected Tree createCharacterTerm(int character, IToken lexicalToken) {
        Tree term = treeFactory.createCharacterTerminal(character, lexicalToken);

        tokenTreeBinding(lexicalToken, term);

        return term;
    }

    protected abstract void tokenTreeBinding(IToken token, Tree tree);

}
