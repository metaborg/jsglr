package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.actions.IGoto;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parsetable.IParseTable;
import org.spoofax.jsglr2.parsetable.IState;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.StackPath;
import org.spoofax.jsglr2.stack.elkhound.AbstractElkhoundStackManager;
import org.spoofax.jsglr2.stack.elkhound.AbstractElkhoundStackNode;
import org.spoofax.jsglr2.stack.elkhound.DeterministicStackPath;

public class ReducerElkhound<ParseForest extends AbstractParseForest, ParseNode extends ParseForest, Derivation> extends Reducer<AbstractElkhoundStackNode<ParseForest>, ParseForest, ParseNode, Derivation> {

    protected final AbstractElkhoundStackManager<AbstractElkhoundStackNode<ParseForest>, ParseForest> stackManager;
    
    public ReducerElkhound(IParseTable parseTable, AbstractElkhoundStackManager<AbstractElkhoundStackNode<ParseForest>, ParseForest> stackManager, ParseForestManager<ParseForest, ParseNode, Derivation> parseForestManager) {
        super(parseTable, stackManager, parseForestManager);
        
        this.stackManager = stackManager;
    }
    
    @Override
    protected void doReductionsHelper(Parse<AbstractElkhoundStackNode<ParseForest>, ParseForest> parse, AbstractElkhoundStackNode<ParseForest> stack, IReduce reduce, StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> throughLink) {
	    	if (stack.deterministicDepth >= reduce.arity()) {
	    		DeterministicStackPath<AbstractElkhoundStackNode<ParseForest>, ParseForest> deterministicPath = stackManager.findDeterministicPathOfLength(parseForestManager, stack, reduce.arity());
	        
	    		if (throughLink == null || deterministicPath.contains(throughLink)) {
		        if (parse.activeStacks.size() == 1)
		            reduceElkhoundPath(parse, deterministicPath.parseForests, deterministicPath.head(), reduce); // Do standard LR if there is only 1 active stack
		        else
		            reducePath(parse, deterministicPath.parseForests, deterministicPath.head(), reduce); // Benefit from faster path retrieval, but still do extra checks since there are other active stacks
	    		}
	    } else {
	        // Fall back to regular GLR
	        for (StackPath<AbstractElkhoundStackNode<ParseForest>, ParseForest> path : stackManager.findAllPathsOfLength(stack, reduce.arity()))
		        	if (throughLink == null || path.contains(throughLink))
		            reducePath(parse, path, reduce);
	    }
    }
    
    private void reduceElkhoundPath(Parse<AbstractElkhoundStackNode<ParseForest>, ParseForest> parse, ParseForest[] parseNodes, AbstractElkhoundStackNode<ParseForest> pathBegin, IReduce reduce) {
        IGoto gotoAction = pathBegin.state.getGoto(reduce.production().productionNumber());
        IState gotoState = parseTable.getState(gotoAction.gotoState());
        
        reducerElkhound(parse, pathBegin, gotoState, reduce, parseNodes);
    }
    
    private void reducerElkhound(Parse<AbstractElkhoundStackNode<ParseForest>, ParseForest> parse, AbstractElkhoundStackNode<ParseForest> stack, IState gotoState, IReduce reduce, ParseForest[] parseForests) {
    		parse.notify(observer -> observer.reducerElkhound(reduce, parseForests));
        
    		AbstractElkhoundStackNode<ParseForest> newStack = reducerNoExistingStack(parse, reduce, stack, gotoState, parseForests);
        
        parse.activeStacks.add(newStack);
        parse.forActor.add(newStack);
    }

}
