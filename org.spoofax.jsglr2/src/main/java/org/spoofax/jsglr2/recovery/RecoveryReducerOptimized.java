package org.spoofax.jsglr2.recovery;

import org.metaborg.parsetable.actions.IReduce;
import org.spoofax.jsglr2.inputstack.IInputStack;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.reducing.ReducerFactory;
import org.spoofax.jsglr2.reducing.ReducerOptimized;
import org.spoofax.jsglr2.stack.AbstractStackManager;
import org.spoofax.jsglr2.stack.IStackNode;

public class RecoveryReducerOptimized
//@formatter:off
   <ParseForest          extends IParseForest,
    Derivation           extends IDerivation<ParseForest>,
    ParseNode            extends IParseNode<ParseForest, Derivation>,
    StackNode            extends IStackNode,
    InputStack           extends IInputStack,
    BacktrackChoicePoint extends IBacktrackChoicePoint<InputStack, StackNode>,
    ParseState           extends AbstractParseState<InputStack, StackNode> & IRecoveryParseState<InputStack, StackNode, BacktrackChoicePoint>>
//@formatter:on
    extends ReducerOptimized<ParseForest, Derivation, ParseNode, StackNode, InputStack, ParseState> {

    public RecoveryReducerOptimized(
        AbstractStackManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> stackManager,
        ParseForestManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> parseForestManager) {
        super(stackManager, parseForestManager);
    }

    public static
//@formatter:off
   <ParseForest_          extends IParseForest,
    Derivation_           extends IDerivation<ParseForest_>,
    ParseNode_            extends IParseNode<ParseForest_, Derivation_>,
    StackNode_            extends IStackNode,
    InputStack_           extends IInputStack,
    BacktrackChoicePoint_ extends IBacktrackChoicePoint<InputStack_, StackNode_>,
    ParseState_           extends AbstractParseState<InputStack_, StackNode_> & IRecoveryParseState<InputStack_, StackNode_, BacktrackChoicePoint_>>
//@formatter:on
    ReducerFactory<ParseForest_, Derivation_, ParseNode_, StackNode_, InputStack_, ParseState_>
        factoryRecoveryOptimized() {
        return RecoveryReducerOptimized::new;
    }

    protected boolean skipParseNodeCreation(ParseState parseState, IReduce reduce) {
        return reduce.production().isSkippableInParseForest() && !parseState.isRecovering();
    }

}
