package org.spoofax.jsglr2.parser;

import java.util.ArrayDeque;
import java.util.Queue;

import org.spoofax.jsglr2.inputstack.IInputStack;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.collections.IActiveStacks;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;

public abstract class AbstractParseState<InputStack extends IInputStack, StackNode extends IStackNode> {

    // TODO would be nice if this is final, but resetting a recovery point requires overwriting it...
    public InputStack inputStack;

    public final IActiveStacks<StackNode> activeStacks;
    public final IForActorStacks<StackNode> forActorStacks;
    public final Queue<ForShifterElement<StackNode>> forShifter = new ArrayDeque<>();

    public StackNode acceptingStack;

    protected AbstractParseState(InputStack inputStack, IActiveStacks<StackNode> activeStacks,
        IForActorStacks<StackNode> forActorStacks) {
        this.inputStack = inputStack;

        this.activeStacks = activeStacks;
        this.forActorStacks = forActorStacks;
    }

    public void nextParseRound(ParserObserving observing) {
        observing.notify(observer -> observer.parseRound(this, activeStacks));
    }

}
