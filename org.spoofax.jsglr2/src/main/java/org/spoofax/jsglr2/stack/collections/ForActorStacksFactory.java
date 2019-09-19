package org.spoofax.jsglr2.stack.collections;

import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;

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

    @Override public
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<ParseForest, StackNode>>
//@formatter:on
    IForActorStacks<StackNode>
        get(ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing) {
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
