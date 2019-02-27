package org.spoofax.jsglr2.stack;

import org.metaborg.parsetable.IState;
import org.spoofax.jsglr2.parser.Position;

public abstract class AbstractStackNode<ParseForest> {

    public final IState state;
    public final Position position;

    public AbstractStackNode(IState state, Position position) {
        this.state = state;
        this.position = position;
    }

    // True if non-empty and all links are rejected
    public abstract boolean allLinksRejected();

}
