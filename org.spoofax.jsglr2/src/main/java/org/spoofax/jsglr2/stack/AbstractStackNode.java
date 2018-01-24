package org.spoofax.jsglr2.stack;

import org.metaborg.parsetable.IState;
import org.spoofax.jsglr2.parser.Position;

public abstract class AbstractStackNode<ParseForest> {

    public final int stackNumber;
    public final IState state;
    public final Position position;

    public AbstractStackNode(int stackNumber, IState state, Position position) {
        this.stackNumber = stackNumber;
        this.state = state;
        this.position = position;
    }

    // True if non-empty and all links are rejected
    public abstract boolean allLinksRejected();

}
