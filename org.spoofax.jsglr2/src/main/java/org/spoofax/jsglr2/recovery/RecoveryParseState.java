package org.spoofax.jsglr2.recovery;

import org.spoofax.jsglr2.inputstack.IInputStack;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.ParseStateFactory;
import org.spoofax.jsglr2.parser.ParserVariant;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.collections.*;

public class RecoveryParseState<InputStack extends IInputStack, StackNode extends IStackNode>
    extends AbstractRecoveryParseState<InputStack, StackNode, BacktrackChoicePoint<InputStack, StackNode>> {

    RecoveryParseState(InputStack inputStack, IActiveStacks<StackNode> activeStacks,
        IForActorStacks<StackNode> forActorStacks) {
        super(inputStack, activeStacks, forActorStacks);
    }

    public static
//@formatter:off
   <ParseForest_ extends IParseForest,
    Derivation_  extends IDerivation<ParseForest_>,
    ParseNode_   extends IParseNode<ParseForest_, Derivation_>,
    InputStack_  extends IInputStack,
    StackNode_   extends IStackNode>
//@formatter:on
    ParseStateFactory<ParseForest_, Derivation_, ParseNode_, InputStack_, StackNode_, RecoveryParseState<InputStack_, StackNode_>>
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
    InputStack_  extends IInputStack,
    StackNode_   extends IStackNode>
//@formatter:on
    ParseStateFactory<ParseForest_, Derivation_, ParseNode_, InputStack_, StackNode_, RecoveryParseState<InputStack_, StackNode_>>
        factory(IActiveStacksFactory activeStacksFactory, IForActorStacksFactory forActorStacksFactory) {
        return (inputStack, observing) -> {
            IActiveStacks<StackNode_> activeStacks = activeStacksFactory.get(observing);
            IForActorStacks<StackNode_> forActorStacks = forActorStacksFactory.get(observing);

            return new RecoveryParseState<>(inputStack, activeStacks, forActorStacks);
        };
    }

    @SuppressWarnings("unchecked") @Override public BacktrackChoicePoint<InputStack, StackNode>
        createBacktrackChoicePoint() {
        // This cast is ugly, but there's no way around it (see AbstractRecoveryParseState)
        return new BacktrackChoicePoint<>((InputStack) inputStack.clone(), activeStacks);
    }
}
