package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.collections.ActiveStacksFactory;
import org.spoofax.jsglr2.stack.collections.ForActorStacksFactory;
import org.spoofax.jsglr2.stack.collections.IActiveStacks;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;

public final class Parse
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<ParseForest, StackNode>>
//@formatter:on
{

    public final ParserObserving<ParseForest, StackNode, ParseState> observing;
    public final ParseState state;

    public Parse(ParserObserving<ParseForest, StackNode, ParseState> observing, ParseState state) {
        this.observing = observing;
        this.state = state;
    }

    public static
//@formatter:off
   <ParseForest_ extends IParseForest,
    StackNode_   extends IStackNode,
    ParseState_  extends AbstractParseState<ParseForest_, StackNode_>>
//@formatter:on
    ParseFactory<ParseForest_, StackNode_, ParseState_> factory(ParserVariant variant,
        ParseStateFactory<ParseForest_, StackNode_, ParseState_> parseStateFactory) {
        return (inputString, filename, observing) -> {
            IActiveStacks<StackNode_> activeStacks =
                new ActiveStacksFactory(variant.activeStacksRepresentation).get(observing);
            IForActorStacks<StackNode_> forActorStacks =
                new ForActorStacksFactory(variant.forActorStacksRepresentation).get(observing);

            return new Parse<>(observing, parseStateFactory.get(inputString, filename, activeStacks, forActorStacks));
        };
    }

    public boolean hasNext() {
        return state.hasNext();
    }

    public void next() {
        state.next();

        observing.notify(observer -> observer.parseNext(this));
    }

}
