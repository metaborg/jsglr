package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.inputstack.IInputStack;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.collections.ActiveStacksFactory;
import org.spoofax.jsglr2.stack.collections.ForActorStacksFactory;
import org.spoofax.jsglr2.stack.collections.IActiveStacks;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;

public class ParseState<StackNode extends IStackNode> extends AbstractParseState<IInputStack, StackNode> {

    protected ParseState(IInputStack inputStack, IActiveStacks<StackNode> activeStacks,
        IForActorStacks<StackNode> forActorStacks) {
        super(inputStack, activeStacks, forActorStacks);
    }

    public static
//@formatter:off
   <ParseForest_ extends IParseForest,
    Derivation_  extends IDerivation<ParseForest_>,
    ParseNode_   extends IParseNode<ParseForest_, Derivation_>,
    StackNode_   extends IStackNode,
    ParseState_  extends AbstractParseState<IInputStack, StackNode_>>
//@formatter:on
    ParseStateFactory<ParseForest_, Derivation_, ParseNode_, StackNode_, IInputStack, ParseState_>
        factory(ParserVariant variant) {
        return (inputStack, observing) -> {
            IActiveStacks<StackNode_> activeStacks =
                new ActiveStacksFactory(variant.activeStacksRepresentation).get(observing);
            IForActorStacks<StackNode_> forActorStacks =
                new ForActorStacksFactory(variant.forActorStacksRepresentation).get(observing);

            return (ParseState_) new ParseState<>(inputStack, activeStacks, forActorStacks);
        };
    }

}
