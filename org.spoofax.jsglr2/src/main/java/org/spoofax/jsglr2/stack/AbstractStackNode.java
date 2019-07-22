package org.spoofax.jsglr2.stack;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parser.Position;

public abstract class AbstractStackNode<ParseForest, StackNode extends IStackNode> implements IStackNode {

    public final IState state;
    public final Position position;

    public AbstractStackNode(IState state, Position position) {
        this.state = state;
        this.position = position;
    }

    public IState state() {
        return state;
    }

    public Position position() {
        return position;
    }

    public abstract Iterable<StackLink<ParseForest, StackNode>> getLinks();

    public abstract StackLink<ParseForest, StackNode> addLink(StackLink<ParseForest, StackNode> link);

    public StackLink<ParseForest, StackNode> addLink(StackNode parent, ParseForest parseNode) {
        StackLink<ParseForest, StackNode> link = new StackLink<>((StackNode) this, parent, parseNode);

        return addLink(link);
    }

}
