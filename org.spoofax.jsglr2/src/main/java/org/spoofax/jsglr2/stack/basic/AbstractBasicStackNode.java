package org.spoofax.jsglr2.stack.basic;

import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.states.IState;

public abstract class AbstractBasicStackNode<ParseForest> extends AbstractStackNode<ParseForest> {

    public AbstractBasicStackNode(int stackNumber, IState state, Position position) {
        super(stackNumber, state, position);
    }

    public abstract Iterable<StackLink<AbstractBasicStackNode<ParseForest>, ParseForest>> getLinksOut();

    public abstract StackLink<AbstractBasicStackNode<ParseForest>, ParseForest>
        addOutLink(StackLink<AbstractBasicStackNode<ParseForest>, ParseForest> link);

    public StackLink<AbstractBasicStackNode<ParseForest>, ParseForest> addOutLink(int linkNumber,
        AbstractBasicStackNode<ParseForest> parent, ParseForest parseNode) {
        StackLink<AbstractBasicStackNode<ParseForest>, ParseForest> link =
            new StackLink<AbstractBasicStackNode<ParseForest>, ParseForest>(linkNumber, this, parent, parseNode);

        return addOutLink(link);
    }

}
