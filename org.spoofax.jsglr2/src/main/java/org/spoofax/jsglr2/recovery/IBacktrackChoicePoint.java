package org.spoofax.jsglr2.recovery;

import java.util.List;

import org.spoofax.jsglr2.inputstack.IInputStack;
import org.spoofax.jsglr2.stack.IStackNode;

public interface IBacktrackChoicePoint<InputStack extends IInputStack, StackNode extends IStackNode> {

    int offset();

    InputStack inputStack();

    List<StackNode> activeStacks();

}
