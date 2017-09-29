package org.spoofax.jsglr2.imploder;

import java.util.ArrayList;
import java.util.List;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parsetable.IProduction;
import org.spoofax.jsglr2.tokenizer.Tokenizer;
import org.spoofax.jsglr2.tokenizer.Tokens;

public abstract class TokenizedTreeImploder<ParseForest extends AbstractParseForest, ParseNode extends ParseForest, Derivation extends IDerivation<ParseForest>, Tree> implements IImploder<ParseForest, Tree> {
	
    protected final ITreeFactory<Tree> treeFactory;
    protected final Tokenizer<ParseForest, ParseNode, Derivation> tokenizer;
    
    public TokenizedTreeImploder(ITreeFactory<Tree> treeFactory, Tokenizer<ParseForest, ParseNode, Derivation> tokenizer) {
        this.treeFactory = treeFactory;
        this.tokenizer = tokenizer;
    }
	
	public ImplodeResult<Tree> implode(Parse<?, ParseForest> parse, ParseForest parseForest) {
        Tokens tokens = new Tokens(parse.inputString, parse.filename);
        
        tokenizer.tokenize(tokens, parseForest);
        
        @SuppressWarnings("unchecked")
        ParseNode topParseNode = (ParseNode) parseForest;
        
        Tree tree = implodeParseNode(topParseNode, tokens.startToken, tokens.endToken);
		
        tokenTreeBinding(tokens.getTokenAt(0), tree);
        
		return new ImplodeResult<Tree>(parse, tree);
	}
	
	protected Tree implodeParseNode(ParseNode parseNode, IToken leftToken, IToken rightToken) {
	    IProduction production = parseNodeProduction(parseNode);
	    
        if (production.isContextFree()) {
            List<Derivation> preferredAvoidedDerivations = parseNodePreferredAvoidedDerivations(parseNode);
            
            if (preferredAvoidedDerivations.size() > 1) {
                List<Tree> trees = new ArrayList<Tree>(preferredAvoidedDerivations.size());
                
                for (Derivation derivation : preferredAvoidedDerivations)
                    trees.add(implodeDerivation(derivation, leftToken, rightToken));
                
                String sort = production.sort();
                
                return treeFactory.createAmb(sort, trees, leftToken, rightToken);
            } else
                return implodeDerivation(preferredAvoidedDerivations.get(0), leftToken, rightToken);
        } else if (production.isLayout() || production.isLiteral()) {
            return null;
        } else if (production.isLexical() || production.isLexicalRhs()) {
            return createLexicalTerm(production, parseNode.inputPart(), leftToken, parseNode.token);
        } else {
            throw new RuntimeException("invalid term type");
        }
    }

	protected Tree implodeDerivation(Derivation derivation, IToken leftToken, IToken rightToken) {
	    IProduction production = derivation.production();
	    
        if (!production.isContextFree())
            throw new RuntimeException("non context free imploding not supported");
        
        List<Tree> childASTs = new ArrayList<Tree>();
        List<ParseForest> nonAstLexicals = new ArrayList<ParseForest>();
        
        implodeChildParseNodes(childASTs, derivation, derivation.production(), leftToken, rightToken, nonAstLexicals);
        
        Tree resultAst = createContextFreeTerm(derivation.production(), childASTs, leftToken, rightToken);
        
        for (ParseForest nonAstLexical : nonAstLexicals)
            tokenTreeBinding(nonAstLexical.token, resultAst);
        
        return resultAst;
    }
    
	protected void implodeChildParseNodes(List<Tree> childASTs, Derivation derivation, IProduction production, IToken leftToken, IToken rightToken, List<ParseForest> nonAstLexicals) {
        ParseForest[] childParseForests = derivation.parseForests();
        
        IToken[] rightTokenPerChild = getRightTokensPerParseNode(childParseForests, rightToken);
        IToken childLeftToken = leftToken;
        
        for (int i = 0; i < childParseForests.length; i++) {
            @SuppressWarnings("unchecked")
            ParseNode parseNode = (ParseNode) childParseForests[i];
            
            IToken childRightToken = rightTokenPerChild[i];
            
            IProduction parseNodeProduction = parseNodeProduction(parseNode);
            
            if (production.isList() && parseNodeProduction.isList()) {
                // Make sure lists are flattened
                implodeChildParseNodes(childASTs, parseNodeOnlyDerivation(parseNode), parseNodeProduction, childLeftToken, childRightToken, nonAstLexicals);
            } else {
                Tree childAST = implodeParseNode(parseNode, childLeftToken, childRightToken);
                
                if (childAST != null)
                    childASTs.add(childAST);
                
                if (childAST == null && parseNode.token != null)
                    nonAstLexicals.add(parseNode);
            }
            
            if (parseNode.lastToken != null)
                childLeftToken = parseNode.lastToken;
        }
    }
    
    protected IToken[] getRightTokensPerParseNode(ParseForest[] parseNodes, IToken rightToken) {
        IToken[] rightTokenPerParseNode = new IToken[parseNodes.length];
        
        for (int i = parseNodes.length - 1; i >= 0; i--) {
            if (i == parseNodes.length - 1)
                rightTokenPerParseNode[i] = rightToken;
            else if (parseNodes[i + 1].firstToken != null)
                rightTokenPerParseNode[i] = parseNodes[i + 1].firstToken;
            else
                rightTokenPerParseNode[i] = rightTokenPerParseNode[i + 1];
        }
        
        return rightTokenPerParseNode;
    }
    
    protected Tree createContextFreeTerm(IProduction production, List<Tree> childASTs, IToken leftToken, IToken rightToken) {
        String constructor = production.constructor();
        
        if (production.isList())
            return treeFactory.createList(production.sort(), childASTs, leftToken, rightToken);
        else if (production.isOptional())
            return treeFactory.createOptional(production.sort(), childASTs, leftToken, rightToken);
        else if (constructor != null)
            return treeFactory.createNonTerminal(production.sort(), constructor, childASTs, leftToken, rightToken);
        else if (childASTs.size() == 1)
            return childASTs.get(0);
        else
            return treeFactory.createTuple(production.sort(), childASTs, leftToken, rightToken);
    }
    
    protected Tree createLexicalTerm(IProduction production, String lexicalString, IToken leftToken, IToken lexicalToken) {
        Tree lexicalTerm = treeFactory.createStringTerminal(production.sort(), lexicalString, leftToken, lexicalToken);
        
        if (lexicalToken != null) // Can be null, e.g. for empty string lexicals
            tokenTreeBinding(lexicalToken, lexicalTerm);
        
        return lexicalTerm;
    }
    
    protected abstract void tokenTreeBinding(IToken token, Tree tree);
    
    protected abstract IProduction parseNodeProduction(ParseNode parseNode);
    
    protected abstract Derivation parseNodeOnlyDerivation(ParseNode parseNode);
    
    protected abstract List<Derivation> parseNodePreferredAvoidedDerivations(ParseNode parseNode);

}
