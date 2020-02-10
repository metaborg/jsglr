package org.spoofax.jsglr2.incremental;

import org.spoofax.jsglr2.incremental.parseforest.IncrementalDerivation;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseNode;
import org.spoofax.jsglr2.inputstack.incremental.IIncrementalInputStack;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.ParseStateFactory;
import org.spoofax.jsglr2.parser.ParserVariant;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.collections.*;

public class IncrementalParseState<StackNode extends IStackNode>
    extends AbstractParseState<IIncrementalInputStack, StackNode> implements IIncrementalParseState {

    private boolean multipleStates = false;

    public IncrementalParseState(IIncrementalInputStack inputStack, IActiveStacks<StackNode> activeStacks,
        IForActorStacks<StackNode> forActorStacks) {
        super(inputStack, activeStacks, forActorStacks);
    }

    public static <StackNode_ extends IStackNode>
        ParseStateFactory<IncrementalParseForest, IncrementalDerivation, IncrementalParseNode, IIncrementalInputStack, StackNode_, IncrementalParseState<StackNode_>>
        factory(ParserVariant variant) {
        //@formatter:off
        return factory(
            new ActiveStacksFactory(variant.activeStacksRepresentation),
            new ForActorStacksFactory(variant.forActorStacksRepresentation)
        );
        //@formatter:on
    }

    public static <StackNode_ extends IStackNode>
        ParseStateFactory<IncrementalParseForest, IncrementalDerivation, IncrementalParseNode, IIncrementalInputStack, StackNode_, IncrementalParseState<StackNode_>>
        factory(IActiveStacksFactory activeStacksFactory, IForActorStacksFactory forActorStacksFactory) {
        return (inputStack, observing) -> {
            IActiveStacks<StackNode_> activeStacks = activeStacksFactory.get(observing);
            IForActorStacks<StackNode_> forActorStacks = forActorStacksFactory.get(observing);

            return new IncrementalParseState<>(inputStack, activeStacks, forActorStacks);
        };
    }

    @Override public void nextParseRound(ParserObserving observing) {
        super.nextParseRound(observing);

        setMultipleStates(activeStacks.isMultiple());
    }

    @Override public boolean newParseNodesAreReusable() {
        return !multipleStates;
    }

    @Override public void setMultipleStates(boolean multipleStates) {
        this.multipleStates = multipleStates;
    }
}
