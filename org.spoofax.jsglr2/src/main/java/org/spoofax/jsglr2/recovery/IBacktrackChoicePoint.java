package org.spoofax.jsglr2.recovery;

import org.spoofax.jsglr2.stack.IStackNode;

import java.util.List;

public interface IBacktrackChoicePoint<StackNode extends IStackNode> {

    int offset();

    List<StackNode> activeStacks();

}
