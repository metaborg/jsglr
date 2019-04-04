package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.JSGLR2Variants;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.collections.ActiveStacksFactory;
import org.spoofax.jsglr2.stack.collections.ForActorStacksFactory;
import org.spoofax.jsglr2.stack.collections.IActiveStacksFactory;
import org.spoofax.jsglr2.stack.collections.IForActorStacksFactory;

public class Parse
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode>
//@formatter:on
    extends AbstractParse<ParseForest, StackNode> {

    public static <ParseForest_ extends IParseForest, StackNode_ extends IStackNode>
        ParseFactory<ParseForest_, StackNode_, AbstractParse<ParseForest_, StackNode_>>
        factory(JSGLR2Variants.ParserVariant variant) {

        ActiveStacksFactory activeStacksFactory = new ActiveStacksFactory(variant.activeStacksRepresentation);
        ForActorStacksFactory forActorStacksFactory = new ForActorStacksFactory(variant.forActorStacksRepresentation);
        return (inputString, filename, observing) -> new Parse<>(inputString, filename, activeStacksFactory,
            forActorStacksFactory, observing);
    }

    public Parse(String inputString, String filename, IActiveStacksFactory activeStacksFactory,
        IForActorStacksFactory forActorStacksFactory, ParserObserving<ParseForest, StackNode> observing) {
        super(inputString, filename, activeStacksFactory, forActorStacksFactory, observing);
    }
}
