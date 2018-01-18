package org.spoofax.jsglr2.parser;

import org.metaborg.parsetable.IState;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public final class ForShifterElement<ParseForest, StackNode extends AbstractStackNode<ParseForest>> {

    public final StackNode stack;
    public final IState state;

    public ForShifterElement(StackNode stack, IState state) {
        this.stack = stack;
        this.state = state;
    }

}
