package org.spoofax.jsglr2.reducing;

import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parsetable.IState;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.StackManager;

public class ReducerSkipLayoutAndLexical<StackNode extends AbstractStackNode<ParseForest>, ParseForest extends AbstractParseForest, ParseNode extends ParseForest, Derivation> extends Reducer<StackNode, ParseForest, ParseNode, Derivation> {

    public ReducerSkipLayoutAndLexical(StackManager<StackNode, ParseForest> stackManager, ParseForestManager<ParseForest, ParseNode, Derivation> parseForestManager) {
    		super(stackManager, parseForestManager);
    }
    
    @Override
    public void reducerExistingStackWithDirectLink(Parse<StackNode, ParseForest> parse, IReduce reduce, StackLink<StackNode, ParseForest> existingDirectLinkToActiveStateWithGoto, ParseForest[] parseForests) {
		@SuppressWarnings("unchecked")
        ParseNode parseNode = (ParseNode) existingDirectLinkToActiveStateWithGoto.parseForest;
    		
    		if (parseNode != null) {
    			Derivation derivation = parseForestManager.createDerivation(parse, existingDirectLinkToActiveStateWithGoto.to.position, reduce.production(), reduce.productionType(), parseForests);
        		parseForestManager.addDerivation(parse, parseNode, derivation);
    		}
        
        if (reduce.isRejectProduction())
            stackManager.rejectStackLink(parse, existingDirectLinkToActiveStateWithGoto);
    }
    
    @Override
    public StackLink<StackNode, ParseForest> reducerExistingStackWithoutDirectLink(Parse<StackNode, ParseForest> parse, IReduce reduce, StackNode existingActiveStackWithGotoState, StackNode stack, ParseForest[] parseForests) {
    		ParseNode parseNode;
    	
    		if (reduce.production().isSkippableInParseForest())
    			parseNode = null;
    		else {
        		Derivation derivation = parseForestManager.createDerivation(parse, stack.position, reduce.production(), reduce.productionType(), parseForests);
            parseNode = parseForestManager.createParseNode(parse, stack.position, reduce.production(), derivation);		
    		}
        
        StackLink<StackNode, ParseForest> newDirectLinkToActiveStateWithGoto = stackManager.createStackLink(parse, existingActiveStackWithGotoState, stack, parseNode);
        
        if (reduce.isRejectProduction())
            stackManager.rejectStackLink(parse, newDirectLinkToActiveStateWithGoto);
        
        return newDirectLinkToActiveStateWithGoto;
    }
    
    @Override
    public StackNode reducerNoExistingStack(Parse<StackNode, ParseForest> parse, IReduce reduce, StackNode stack, IState gotoState, ParseForest[] parseForests) {
    		ParseNode parseNode;
    	
		if (reduce.production().isSkippableInParseForest())
			parseNode = null;
		else {
	    		Derivation derivation = parseForestManager.createDerivation(parse, stack.position, reduce.production(), reduce.productionType(), parseForests);
	        parseNode = parseForestManager.createParseNode(parse, stack.position, reduce.production(), derivation);		
		}
        
        StackNode newStackWithGotoState = stackManager.createStackNode(parse, gotoState);
		StackLink<StackNode, ParseForest> link = stackManager.createStackLink(parse, newStackWithGotoState, stack, parseNode);
        
        if (reduce.isRejectProduction())
            stackManager.rejectStackLink(parse, link);
        
        return newStackWithGotoState;
    }
    
}
