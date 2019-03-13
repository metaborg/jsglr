package org.spoofax.jsglr2.stack.collections;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;

public class ActiveStacksFactory implements IActiveStacksFactory {

    private final ActiveStacksRepresentation activeStacksRepresentation;

    public static ActiveStacksRepresentation defaultActiveStacksRepresentation = ActiveStacksRepresentation.ArrayList;

    public ActiveStacksFactory() {
        this(defaultActiveStacksRepresentation);
    }

    public ActiveStacksFactory(ActiveStacksRepresentation activeStacksRepresentation) {
        this.activeStacksRepresentation = activeStacksRepresentation;
    }

    @Override public <ParseForest extends IParseForest, StackNode extends IStackNode> IActiveStacks<StackNode>
        get(ParserObserving<ParseForest, StackNode> observing) {
        IActiveStacks<StackNode> activeStacks;

        switch(activeStacksRepresentation) {
            case ArrayList:
                activeStacks = new ActiveStacksArrayList<>(observing);
                break;
            case ArrayListHashMap:
                activeStacks = new ActiveStacksArrayListHashMap<>(observing);
                break;
            case LinkedHashMap:
                activeStacks = new ActiveStacksLinkedHashMap<>(observing);
                break;
            default:
                activeStacks = null;
                break;
        }

        return activeStacks;
    }

}
