package org.spoofax.jsglr2.stack;

import org.metaborg.parsetable.IState;

public abstract class AbstractStackNode<ParseForest> {

    public final IState state;

    public AbstractStackNode(IState state) {
        this.state = state;
    }

    // True if non-empty and all links are rejected
    public abstract boolean allLinksRejected();

}
