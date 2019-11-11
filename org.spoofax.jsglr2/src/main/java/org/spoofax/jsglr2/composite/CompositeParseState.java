package org.spoofax.jsglr2.composite;

import org.spoofax.jsglr2.inputstack.LayoutSensitiveInputStack;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.ParseStateFactory;
import org.spoofax.jsglr2.parser.ParserVariant;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.collections.ActiveStacksFactory;
import org.spoofax.jsglr2.stack.collections.ForActorStacksFactory;
import org.spoofax.jsglr2.stack.collections.IActiveStacks;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;

public class CompositeParseState<StackNode extends IStackNode>
    extends AbstractParseState<LayoutSensitiveInputStack, StackNode> {

    private CompositeParseState(LayoutSensitiveInputStack inputStack, IActiveStacks<StackNode> activeStacks,
        IForActorStacks<StackNode> forActorStacks) {
        super(inputStack, activeStacks, forActorStacks);
    }

    public static
//@formatter:off
   <ParseForest_ extends IParseForest,
    Derivation_  extends IDerivation<ParseForest_>,
    ParseNode_   extends IParseNode<ParseForest_, Derivation_>,
    StackNode_   extends IStackNode,
    ParseState_  extends AbstractParseState<LayoutSensitiveInputStack, StackNode_>>
//@formatter:on
    ParseStateFactory<ParseForest_, Derivation_, ParseNode_, StackNode_, LayoutSensitiveInputStack, ParseState_>
        factory(ParserVariant variant) {
        return (inputStack, observing) -> {
            IActiveStacks<StackNode_> activeStacks =
                new ActiveStacksFactory(variant.activeStacksRepresentation).get(observing);
            IForActorStacks<StackNode_> forActorStacks =
                new ForActorStacksFactory(variant.forActorStacksRepresentation).get(observing);

            return (ParseState_) new CompositeParseState<>(inputStack, activeStacks, forActorStacks);
        };
    }

}
