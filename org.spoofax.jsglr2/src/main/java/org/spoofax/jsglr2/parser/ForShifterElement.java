package org.spoofax.jsglr2.parser;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.stack.IStackNode;

public final class ForShifterElement<StackNode extends IStackNode> {

    public final StackNode stack;
    public final IState state;

    public ForShifterElement(StackNode stack, IState state) {
        this.stack = stack;
        this.state = state;
    }

}
