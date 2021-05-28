package org.spoofax.jsglr2.inputstack.incremental;

import static org.spoofax.jsglr2.incremental.parseforest.IncrementalCharacterNode.EOF_NODE;

import org.spoofax.jsglr2.incremental.IIncrementalParseState;
import org.spoofax.jsglr2.incremental.diff.IStringDiff;
import org.spoofax.jsglr2.incremental.diff.ProcessUpdates;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalCharacterNode;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseNode;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalSkippedNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.stack.IStackNode;

/**
 * This type of incremental input stack preprocesses the previous parse forest using the list of updates.
 * {@link LazyPreprocessingIncrementalInputStack} also uses this strategy, but does not inherit this class because of
 * its completely different way of handling the stack.
 */
public abstract class AbstractPreprocessingIncrementalInputStack extends AbstractInputStack
    implements IIncrementalInputStack {
    /**
     * The stack contains all subtrees that are yet to be popped. The top of the stack also contains the subtree that
     * has been returned last time. The stack initially only contains EOF and the root.
     */
    protected final IStack<IncrementalParseForest> stack;

    protected abstract IStack<IncrementalParseForest> createStack();

    /** Copy constructor. Only used in {@link #clone()}. */
    AbstractPreprocessingIncrementalInputStack(AbstractPreprocessingIncrementalInputStack original) {
        super(original.inputString);

        this.currentOffset = original.currentOffset;
        this.stack = original.stack.clone();
    }

    /**
     * @param inputString
     *            should be equal to the yield of the root.
     */
    public AbstractPreprocessingIncrementalInputStack(IncrementalParseForest root, String inputString) {
        super(inputString);
        stack = createStack();
        stack.push(EOF_NODE);
        stack.push(root);
    }

    static <StackNode extends IStackNode, ParseState extends AbstractParseState<IIncrementalInputStack, StackNode> & IIncrementalParseState>
        IncrementalParseForest preProcessParseForest(ProcessUpdates<StackNode, ParseState> processUpdates,
            IStringDiff diff, String inputString, String previousInput, IncrementalParseForest previousResult) {
        return previousInput != null && previousResult != null
            ? processUpdates.processUpdates(previousInput, previousResult, diff.diff(previousInput, inputString))
            : processUpdates.getParseNodeFromString(inputString);
    }

    @Override public abstract AbstractPreprocessingIncrementalInputStack clone();

    @Override public void breakDown() {
        if(stack.isEmpty())
            return;

        IncrementalParseForest current = stack.peek();
        if(current.isTerminal())
            return;

        if(current instanceof IncrementalSkippedNode) {
            // Break down a skipped node by explicitly instantiating character nodes for the skipped part
            stack.pop();
            pushCharactersToStack(inputString.substring(currentOffset, currentOffset + current.width()));
            return;
        }

        stack.pop(); // always pop last lookahead, whether it has children or not
        IncrementalParseForest[] children = ((IncrementalParseNode) current).getFirstDerivation().parseForests();
        // Push all children to stack in reverse order
        for(int i = children.length - 1; i >= 0; i--) {
            stack.push(children[i]);
        }
    }

    @Override public void next() {
        currentOffset += stack.pop().width();
    }

    @Override public IncrementalParseForest getNode() {
        return stack.isEmpty() ? null : stack.peek();
    }

    protected void pushCharactersToStack(String inputString) {
        int[] chars = inputString.codePoints().toArray();
        for(int i = chars.length - 1; i >= 0; i--) {
            stack.push(new IncrementalCharacterNode(chars[i]));
        }
    }
}
