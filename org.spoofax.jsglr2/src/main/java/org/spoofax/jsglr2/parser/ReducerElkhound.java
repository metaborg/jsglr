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
import org.spoofax.jsglr2.stack.elkhound.ElkhoundStackNode;

public class ReducerElkhound<ParseForest extends AbstractParseForest, ParseNode extends ParseForest, Derivation> extends Reducer<ElkhoundStackNode<ParseForest>, ParseForest, ParseNode, Derivation> {

    public ReducerElkhound(IParseTable parseTable, StackManager<ElkhoundStackNode<ParseForest>, ParseForest> stackManager, ParseForestManager<ParseForest, ParseNode, Derivation> parseForestManager) {
        super(parseTable, stackManager, parseForestManager);
    }
    
    @Override
    public void doReductions(Parse<ElkhoundStackNode<ParseForest>, ParseForest> parse, ElkhoundStackNode<ParseForest> stack, IReduce reduce) {
        if (reduce.production().isCompletionOrRecovery())
            return;
        
        if (stack.deterministicDepth >= reduce.arity()) {
            StackPath<ElkhoundStackNode<ParseForest>, ParseForest> deterministicPath = stack.findDeterministicPathOfLength(reduce.arity());
            
            if (parse.activeStacks.size() == 1)
                reduceElkhoundPath(parse, deterministicPath, reduce); // Do standard LR if there is only 1 active stack
            else
                reducePath(parse, deterministicPath, reduce); // Benefit from faster path retrieval, but still do extra checks since there are other active stacks
        } else {
            // Fall back to regular GLR
            for (StackPath<ElkhoundStackNode<ParseForest>, ParseForest> path : stackManager.findAllPathsOfLength(stack, reduce.arity()))
                reducePath(parse, path, reduce);
        }
    }
    
    private void reduceElkhoundPath(Parse<ElkhoundStackNode<ParseForest>, ParseForest> parse, StackPath<ElkhoundStackNode<ParseForest>, ParseForest> path, IReduce reduce) {
        ElkhoundStackNode<ParseForest> pathEnd = path.lastStackNode(); 
        
        List<ParseForest> parseNodes = path.getParseForests();
    
        IGoto gotoAction = pathEnd.state.getGoto(reduce.production().productionNumber());
        IState gotoState = parseTable.getState(gotoAction.gotoState());
        
        reducerElkhound(parse, pathEnd, gotoState, reduce, parseNodes);
    }
    
    private void reducerElkhound(Parse<ElkhoundStackNode<ParseForest>, ParseForest> parse, ElkhoundStackNode<ParseForest> stack, IState gotoState, IReduce reduce, List<ParseForest> parseForests) {
        Derivation derivation = parseForestManager.createDerivation(parse, reduce.production(), reduce.productionType(), parseForests);
        ParseForest parseNode = parseForestManager.createParseNode(parse, reduce.production(), derivation);
        
        ElkhoundStackNode<ParseForest> newStack = stackManager.createStackNode(parse, gotoState);
        StackLink<ElkhoundStackNode<ParseForest>, ParseForest> link = stackManager.createStackLink(parse, newStack, stack, parseNode);
        
        parse.activeStacks.add(newStack);
        parse.forActor.add(newStack); // TODO: check if this is required, it might cause unrequired overhead
        
        if (reduce.isRejectProduction())
            stackManager.rejectStackLink(parse, link);
    }

}
