package org.spoofax.jsglr2.stack;

import org.metaborg.parsetable.IState;
import org.spoofax.jsglr2.parser.Position;

public interface IStackNode {

    IState state();

    Position position();

    // True if non-empty and all links are rejected
    boolean allLinksRejected();

}
