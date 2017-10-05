package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.actions.IGoto;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parsetable.IParseTable;
import org.spoofax.jsglr2.parsetable.IState;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.StackManager;
import org.spoofax.jsglr2.stack.StackPath;

public class Reducer<StackNode extends AbstractStackNode<ParseForest>, ParseForest extends AbstractParseForest, ParseNode extends ParseForest, Derivation> {

    protected final IParseTable parseTable;
    protected final StackManager<StackNode, ParseForest> stackManager;
    protected final ParseForestManager<ParseForest, ParseNode, Derivation> parseForestManager;
    
    public Reducer(IParseTable parseTable, StackManager<StackNode, ParseForest> stackManager, ParseForestManager<ParseForest, ParseNode, Derivation> parseForestManager) {
        this.parseTable = parseTable;
        this.stackManager = stackManager;
        this.parseForestManager = parseForestManager;
    }
    
    public void doReductions(Parse<StackNode, ParseForest> parse, StackNode stack, IReduce reduce) {
        if (reduce.production().isCompletionOrRecovery())
            return;
        
        for (StackPath<StackNode, ParseForest> path : stackManager.findAllPathsOfLength(stack, reduce.arity()))
            reducePath(parse, path, reduce);
    }
    
    private void doLimitedRedutions(Parse<StackNode, ParseForest> parse, StackNode stack, IReduce reduce, StackLink<StackNode, ParseForest> link) {
        if (reduce.production().isCompletionOrRecovery())
            return;
        
        for (StackPath<StackNode, ParseForest> path : stackManager.findAllPathsOfLength(stack, reduce.arity())) {
            if (path.contains(link))
                reducePath(parse, path, reduce);
        }
    }
    
    protected void reducePath(Parse<StackNode, ParseForest> parse, StackPath<StackNode, ParseForest> path, IReduce reduce) {
    		ParseForest[] parseNodes = stackManager.getParseForests(parseForestManager, path);
    		StackNode pathBegin = path.head();
    		
    		reducePath(parse, parseNodes, pathBegin, reduce);    		
    }
    
    protected void reducePath(Parse<StackNode, ParseForest> parse, ParseForest[] parseNodes, StackNode pathBegin, IReduce reduce) {
        IGoto gotoAction = pathBegin.state.getGoto(reduce.production().productionNumber());
        IState gotoState = parseTable.getState(gotoAction.gotoState());
        
        reducer(parse, pathBegin, gotoState, reduce, parseNodes);
    }
    
    private void reducer(Parse<StackNode, ParseForest> parse, StackNode stack, IState gotoState, IReduce reduce, ParseForest[] parseForests) {
        StackNode activeStackWithGotoState = parse.activeStacks.findWithState(gotoState);
        
        parse.notify(observer -> observer.reduce(reduce, parseForests, activeStackWithGotoState));
        
        Derivation derivation = parseForestManager.createDerivation(parse, reduce.production(), reduce.productionType(), parseForests);
        
        if (activeStackWithGotoState != null) {
            StackLink<StackNode, ParseForest> directLink = stackManager.findDirectLink(activeStackWithGotoState, stack);
            
            parse.notify(observer -> observer.directLinkFound(directLink));
            
            if (directLink != null) {
                @SuppressWarnings("unchecked")
                ParseNode parseNode = (ParseNode) directLink.parseForest;
                
                parseForestManager.addDerivation(parse, parseNode, derivation);
                
                if (reduce.isRejectProduction())
                    stackManager.rejectStackLink(parse, directLink);
            } else {
                ParseForest parseNode = parseForestManager.createParseNode(parse, reduce.production(), derivation);
                
                StackLink<StackNode, ParseForest> link = stackManager.createStackLink(parse, activeStackWithGotoState, stack, parseNode);
                
                if (reduce.isRejectProduction())
                    stackManager.rejectStackLink(parse, link);
                
                // Save the number of active stacks to prevent the for loop from processing active stacks that are added by doLimitedReductions.
                // We can safely limit the loop by the current number of stacks since new stack are added at the end.
                int size = parse.activeStacks.size();
                
                for (int i = 0; i < size; i++) {
                		StackNode activeStack = parse.activeStacks.get(i);
                	
                    if (!activeStack.allOutLinksRejected() && !parse.forActor.contains(activeStack) && !parse.forActorDelayed.contains(activeStack))
                        for (IReduce reduceAction : activeStack.state.applicableReduceActions(parse.currentChar))
                            doLimitedRedutions(parse, activeStack, reduceAction, link);
                }
            }
        } else {
            StackNode newStack = stackManager.createStackNode(parse, gotoState);
            ParseForest parseNode = parseForestManager.createParseNode(parse, reduce.production(), derivation);
            
            StackLink<StackNode, ParseForest> link = stackManager.createStackLink(parse, newStack, stack, parseNode);
            
            parse.activeStacks.add(newStack);
            
            if (newStack.state.isRejectable())
                parse.forActorDelayed.add(newStack);
            else
                parse.forActor.add(newStack);
            
            if (reduce.isRejectProduction())
                stackManager.rejectStackLink(parse, link);
        }
    }

}
