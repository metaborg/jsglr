package org.spoofax.jsglr2.stack.collections;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public class ForActorStacksFactory implements IForActorStacksFactory {

    private final ForActorStacksRepresentation forActorStacksRepresentation;

    public static ForActorStacksRepresentation defaultForActorStacksRepresentation =
        ForActorStacksRepresentation.ArrayDeque;

    public ForActorStacksFactory() {
        this(defaultForActorStacksRepresentation);
    }

    public ForActorStacksFactory(ForActorStacksRepresentation forActorStacksRepresentation) {
        this.forActorStacksRepresentation = forActorStacksRepresentation;
    }

    @Override
    public <ParseForest extends AbstractParseForest, StackNode extends AbstractStackNode<ParseForest>>
        IForActorStacks<StackNode> get(ParserObserving<ParseForest, StackNode> observing) {
        IForActorStacks<StackNode> forActorStacks;

        switch(forActorStacksRepresentation) {
            case ArrayDeque:
                forActorStacks = new ForActorStacksArrayDeque<>(observing);
                break;
            case LinkedHashMap:
                forActorStacks = new ForActorStacksLinkedHashMap<>(observing);
                break;
            default:
                forActorStacks = null;
                break;
        }

        return forActorStacks;
    }

}
