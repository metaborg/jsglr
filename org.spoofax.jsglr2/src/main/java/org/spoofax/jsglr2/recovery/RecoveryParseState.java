package org.spoofax.jsglr2.recovery;

import org.spoofax.jsglr2.inputstack.IInputStack;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.ParseStateFactory;
import org.spoofax.jsglr2.parser.ParserVariant;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.collections.ActiveStacksFactory;
import org.spoofax.jsglr2.stack.collections.ForActorStacksFactory;
import org.spoofax.jsglr2.stack.collections.IActiveStacks;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;

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
        return (inputStack, observing) -> {
            IActiveStacks<StackNode_> activeStacks =
                new ActiveStacksFactory(variant.activeStacksRepresentation).get(observing);
            IForActorStacks<StackNode_> forActorStacks =
                new ForActorStacksFactory(variant.forActorStacksRepresentation).get(observing);

            return new RecoveryParseState<>(inputStack, activeStacks, forActorStacks);
        };
    }

    @Override public BacktrackChoicePoint<InputStack, StackNode> createBacktrackChoicePoint() {
        // TODO this cast is ugly, but there's no way around it
        return new BacktrackChoicePoint<>((InputStack) inputStack.clone(), activeStacks);
    }
}
