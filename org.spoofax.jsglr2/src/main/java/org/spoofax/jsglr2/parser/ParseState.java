package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.JSGLR2Request;
import org.spoofax.jsglr2.inputstack.IInputStack;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.collections.ActiveStacksFactory;
import org.spoofax.jsglr2.stack.collections.ForActorStacksFactory;
import org.spoofax.jsglr2.stack.collections.IActiveStacks;
import org.spoofax.jsglr2.stack.collections.IActiveStacksFactory;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;
import org.spoofax.jsglr2.stack.collections.IForActorStacksFactory;

public class ParseState<InputStack extends IInputStack, StackNode extends IStackNode>
    extends AbstractParseState<InputStack, StackNode> {

    protected ParseState(JSGLR2Request request, InputStack inputStack, IActiveStacks<StackNode> activeStacks,
        IForActorStacks<StackNode> forActorStacks) {
        super(request, inputStack, activeStacks, forActorStacks);
    }

    public static
//@formatter:off
   <ParseForest_ extends IParseForest,
    Derivation_  extends IDerivation<ParseForest_>,
    ParseNode_   extends IParseNode<ParseForest_, Derivation_>,
    StackNode_   extends IStackNode,
    InputStack_  extends IInputStack>
//@formatter:on
    ParseStateFactory<ParseForest_, Derivation_, ParseNode_, InputStack_, StackNode_, ParseState<InputStack_, StackNode_>>
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
    StackNode_   extends IStackNode,
    InputStack_  extends IInputStack>
//@formatter:on
    ParseStateFactory<ParseForest_, Derivation_, ParseNode_, InputStack_, StackNode_, ParseState<InputStack_, StackNode_>>
        factory(IActiveStacksFactory activeStacksFactory, IForActorStacksFactory forActorStacksFactory) {
        return (request, inputStack, observing) -> {
            IActiveStacks<StackNode_> activeStacks = activeStacksFactory.get(observing);
            IForActorStacks<StackNode_> forActorStacks = forActorStacksFactory.get(observing);

            return new ParseState<>(request, inputStack, activeStacks, forActorStacks);
        };
    }

}
