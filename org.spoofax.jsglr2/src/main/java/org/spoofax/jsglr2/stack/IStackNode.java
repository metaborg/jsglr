package org.spoofax.jsglr2.stack;

import org.metaborg.parsetable.states.IState;

public interface IStackNode {

    IState state();

    // True if non-empty and all links are rejected
    boolean allLinksRejected();

}
