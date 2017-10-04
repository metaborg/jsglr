package org.spoofax.jsglr2.parser;

import java.util.List;

import org.spoofax.jsglr2.actions.IGoto;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parsetable.IParseTable;
import org.spoofax.jsglr2.parsetable.IState;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.StackManager;
import org.spoofax.jsglr2.stack.StackPath;
import org.spoofax.jsglr2.stack.elkhound.AbstractElkhoundStackNode;

public class ReducerElkhound<ParseForest extends AbstractParseForest, ParseNode extends ParseForest, Derivation> extends Reducer<AbstractElkhoundStackNode<ParseForest>, ParseForest, ParseNode, Derivation> {

    public ReducerElkhound(IParseTable parseTable, StackManager<AbstractElkhoundStackNode<ParseForest>, ParseForest> stackManager, ParseForestManager<ParseForest, ParseNode, Derivation> parseForestManager) {
        super(parseTable, stackManager, parseForestManager);
    }
    
    @Override
    public void doReductions(Parse<AbstractElkhoundStackNode<ParseForest>, ParseForest> parse, AbstractElkhoundStackNode<ParseForest> stack, IReduce reduce) {
        if (reduce.production().isCompletionOrRecovery())
            return;
        
        if (stack.deterministicDepth >= reduce.arity()) {
            StackPath<AbstractElkhoundStackNode<ParseForest>, ParseForest> deterministicPath = stack.findDeterministicPathOfLength(reduce.arity());
            
            if (parse.activeStacks.size() == 1)
                reduceElkhoundPath(parse, deterministicPath, reduce); // Do standard LR if there is only 1 active stack
            else
                reducePath(parse, deterministicPath, reduce); // Benefit from faster path retrieval, but still do extra checks since there are other active stacks
        } else {
            // Fall back to regular GLR
            for (StackPath<AbstractElkhoundStackNode<ParseForest>, ParseForest> path : stackManager.findAllPathsOfLength(stack, reduce.arity()))
                reducePath(parse, path, reduce);
        }
    }
    
    private void reduceElkhoundPath(Parse<AbstractElkhoundStackNode<ParseForest>, ParseForest> parse, StackPath<AbstractElkhoundStackNode<ParseForest>, ParseForest> path, IReduce reduce) {
        AbstractElkhoundStackNode<ParseForest> pathEnd = path.lastStackNode(); 
        
        List<ParseForest> parseNodes = path.getParseForests();
    
        IGoto gotoAction = pathEnd.state.getGoto(reduce.production().productionNumber());
        IState gotoState = parseTable.getState(gotoAction.gotoState());
        
        reducerElkhound(parse, pathEnd, gotoState, reduce, parseNodes);
    }
    
    private void reducerElkhound(Parse<AbstractElkhoundStackNode<ParseForest>, ParseForest> parse, AbstractElkhoundStackNode<ParseForest> stack, IState gotoState, IReduce reduce, List<ParseForest> parseForests) {
        Derivation derivation = parseForestManager.createDerivation(parse, reduce.production(), reduce.productionType(), parseForests);
        ParseForest parseNode = parseForestManager.createParseNode(parse, reduce.production(), derivation);
        
        AbstractElkhoundStackNode<ParseForest> newStack = stackManager.createStackNode(parse, gotoState);
        StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> link = stackManager.createStackLink(parse, newStack, stack, parseNode);
        
        parse.activeStacks.add(newStack);
        parse.forActor.add(newStack);
        
        if (reduce.isRejectProduction())
            stackManager.rejectStackLink(parse, link);
    }

}
