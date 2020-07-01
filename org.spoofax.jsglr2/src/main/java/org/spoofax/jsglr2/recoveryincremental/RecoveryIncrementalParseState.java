package org.spoofax.jsglr2.recoveryincremental;

import org.spoofax.jsglr2.JSGLR2Request;
import org.spoofax.jsglr2.incremental.IIncrementalParseState;
import org.spoofax.jsglr2.inputstack.incremental.IIncrementalInputStack;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.ParseStateFactory;
import org.spoofax.jsglr2.parser.ParserVariant;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.recovery.AbstractRecoveryParseState;
import org.spoofax.jsglr2.recovery.BacktrackChoicePoint;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.collections.ActiveStacksFactory;
import org.spoofax.jsglr2.stack.collections.ForActorStacksFactory;
import org.spoofax.jsglr2.stack.collections.IActiveStacks;
import org.spoofax.jsglr2.stack.collections.IActiveStacksFactory;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;
import org.spoofax.jsglr2.stack.collections.IForActorStacksFactory;

public class RecoveryIncrementalParseState<InputStack extends IIncrementalInputStack, StackNode extends IStackNode>
    extends AbstractRecoveryParseState<InputStack, StackNode, BacktrackChoicePoint<InputStack, StackNode>>
    implements IIncrementalParseState {

    private boolean multipleStates = false;

    RecoveryIncrementalParseState(JSGLR2Request request, InputStack inputStack, IActiveStacks<StackNode> activeStacks,
        IForActorStacks<StackNode> forActorStacks) {
        super(request, inputStack, activeStacks, forActorStacks);
    }

    public static
//@formatter:off
   <ParseForest_ extends IParseForest,
    Derivation_  extends IDerivation<ParseForest_>,
    ParseNode_   extends IParseNode<ParseForest_, Derivation_>,
    InputStack_  extends IIncrementalInputStack,
    StackNode_   extends IStackNode>
//@formatter:on
    ParseStateFactory<ParseForest_, Derivation_, ParseNode_, InputStack_, StackNode_, RecoveryIncrementalParseState<InputStack_, StackNode_>>
        factory(ParserVariant variant) {
       //@formatter:off
        return factory(
            new ActiveStacksFactory(variant.activeStacksRepresentation),
            new ForActorStacksFactory(variant.forActorStacksRepresentation)
        );
       //@formatter:on
    }

    public static
//@formatter:off
   <ParseForest_ extends IParseForest,
    Derivation_  extends IDerivation<ParseForest_>,
    ParseNode_   extends IParseNode<ParseForest_, Derivation_>,
    InputStack_  extends IIncrementalInputStack,
    StackNode_   extends IStackNode>
//@formatter:on
    ParseStateFactory<ParseForest_, Derivation_, ParseNode_, InputStack_, StackNode_, RecoveryIncrementalParseState<InputStack_, StackNode_>>
        factory(IActiveStacksFactory activeStacksFactory, IForActorStacksFactory forActorStacksFactory) {
        return (request, inputStack, observing) -> {
            IActiveStacks<StackNode_> activeStacks = activeStacksFactory.get(observing);
            IForActorStacks<StackNode_> forActorStacks = forActorStacksFactory.get(observing);

            return new RecoveryIncrementalParseState<>(request, inputStack, activeStacks, forActorStacks);
        };
    }

    @Override public void nextParseRound(ParserObserving observing) {
        super.nextParseRound(observing);

        setMultipleStates(activeStacks.isMultiple());
    }

    @Override public boolean newParseNodesAreReusable() {
        return !multipleStates && !isRecovering();
    }

    @Override public void setMultipleStates(boolean multipleStates) {
        this.multipleStates = multipleStates;
    }

    @SuppressWarnings("unchecked") @Override public BacktrackChoicePoint<InputStack, StackNode>
        createBacktrackChoicePoint() {
        // This cast is ugly, but there's no way around it (see AbstractRecoveryParseState)
        return new BacktrackChoicePoint<>((InputStack) inputStack.clone(), activeStacks);
    }
}
