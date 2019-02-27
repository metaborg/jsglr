package org.spoofax.jsglr2.stack;

import org.metaborg.parsetable.IState;
import org.spoofax.jsglr2.parser.Position;

public abstract class StackNode<ParseForest> extends AbstractStackNode<ParseForest> {

    public StackNode(IState state, Position position) {
        super(state, position);
    }

    public abstract Iterable<StackLink<ParseForest, StackNode<ParseForest>>> getLinks();

    public abstract StackLink<ParseForest, StackNode<ParseForest>>
        addLink(StackLink<ParseForest, StackNode<ParseForest>> link);

    public StackLink<ParseForest, StackNode<ParseForest>> addLink(
            StackNode<ParseForest> parent, ParseForest parseNode) {
        StackLink<ParseForest, StackNode<ParseForest>> link =
            new StackLink<>(this, parent, parseNode);

        return addLink(link);
    }

}
