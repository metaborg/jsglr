package org.spoofax.jsglr2.inputstack.incremental;

import java.util.List;

import org.metaborg.util.functions.Function4;
import org.spoofax.jsglr2.incremental.EditorUpdate;
import org.spoofax.jsglr2.incremental.diff.IStringDiff;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseNode;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalSkippedNode;

/**
 * This type of incremental input stack processes the list of updates during parsing, instead of having a preprocessing
 * step. Any time when the node at the top of the stack includes an update (including at construction time), this parse
 * node is broken down until this is no longer the case. Once the parser arrives at the offset that matches the start of
 * the update, this update is applied (i.e., nodes within the deletion range are deleted, and new character nodes
 * representing the inserted text are pushed onto the stack). After an update is applied, we move to the next update in
 * the list.
 */
public abstract class AbstractIncrementalInputStack extends AbstractPreprocessingIncrementalInputStack
    implements IIncrementalInputStack {

    private final String previousInput;
    private final List<EditorUpdate> editorUpdates;
    private int currentUpdateIndex = 0;
    private EditorUpdate currentUpdate = null;
    private boolean updateIsExposed = false;

    private int currentOffsetInPrevious = 0;

    AbstractIncrementalInputStack(AbstractIncrementalInputStack original) {
        super(original);

        this.previousInput = original.previousInput;
        this.editorUpdates = original.editorUpdates;
        this.currentUpdateIndex = original.currentUpdateIndex;
        this.currentUpdate = original.currentUpdate;
        this.updateIsExposed = original.updateIsExposed;
        this.currentOffsetInPrevious = original.currentOffsetInPrevious;
    }

    public AbstractIncrementalInputStack(String input, String previousInput, IncrementalParseForest previousResult,
        List<EditorUpdate> editorUpdates) {
        super(previousResult, input);

        this.previousInput = previousInput;
        this.editorUpdates = editorUpdates;

        if(editorUpdates.isEmpty())
            return;

        this.currentUpdate = editorUpdates.get(0);

        checkUpdate();
    }

    static IncrementalInputStackFactory<IIncrementalInputStack> factoryBuilder(IStringDiff diff,
        Function4<String, String, IncrementalParseForest, List<EditorUpdate>, IIncrementalInputStack> constructor) {
        return (inputString, previousInput, previousResult) -> {
            if(previousInput == null || previousResult == null)
                return new StringIncrementalInputStack(inputString);

            List<EditorUpdate> editorUpdates = diff.diff(previousInput, inputString);

            // Optimization: if everything is deleted/replaced, then start a batch parse
            if(editorUpdates.size() == 1 && editorUpdates.get(0).deletedStart == 0
                && editorUpdates.get(0).deletedEnd == previousResult.width())
                return new StringIncrementalInputStack(inputString);

            return constructor.apply(inputString, previousInput, previousResult, editorUpdates);
        };
    }

    @Override public void breakDown() {
        do {
            if(stack.isEmpty())
                return;

            IncrementalParseForest current = stack.peek();
            if(current.isTerminal())
                return;

            stack.pop(); // always pop last lookahead, whether it has children or not

            if(current instanceof IncrementalSkippedNode) {
                // Break down a skipped node by explicitly instantiating character nodes for the skipped part
                pushCharactersToStack(
                    previousInput.substring(currentOffsetInPrevious, currentOffsetInPrevious + current.width()));
            } else {
                IncrementalParseForest[] children =
                    ((IncrementalParseNode) current).getFirstDerivation().parseForests();
                // Push all children to stack in reverse order
                for(int i = children.length - 1; i >= 0; i--) {
                    stack.push(children[i]);
                }
            }

            if(updateIsAtStartOfNextNode())
                updateIsExposed = true;
        } while(currentNodeHasChange());
    }

    @Override public void next() {
        IncrementalParseForest popped = stack.pop();
        int increase = popped.width();

        assert isCorrectYield(popped, increase) : "Yield of popped node must be equal to the substring in the input";

        currentOffset += increase;
        currentOffsetInPrevious += increase;

        checkUpdate();
    }

    // This method is only used when assertions are enabled
    private boolean isCorrectYield(IncrementalParseForest popped, int increase) {
        try {
            return popped.getYield().equals(inputString.substring(Integer.min(currentOffset, inputLength),
                Integer.min(currentOffset + increase, inputLength)));
        } catch(UnsupportedOperationException e) {
            if(e.getMessage().equals("Cannot get yield of skipped parse node"))
                return true; // Ignore this exception
            else
                throw e;
        }
    }

    private void checkUpdate() {
        if(currentUpdate != null && currentOffsetInPrevious == currentUpdate.deletedStart) {
            while(currentOffsetInPrevious < currentUpdate.deletedEnd)
                if(currentOffsetInPrevious + stack.peek().width() > currentUpdate.deletedEnd)
                    breakDown();
                else
                    currentOffsetInPrevious += stack.pop().width();
            // Also delete any null-yield trees at position `currentUpdate.deletedEnd`
            while(stack.peek().width() == 0)
                stack.pop();
            currentOffsetInPrevious -= currentUpdate.insertedLength();
            pushCharactersToStack(currentUpdate.inserted);
            currentUpdate = ++currentUpdateIndex >= editorUpdates.size() ? null : editorUpdates.get(currentUpdateIndex);
            updateIsExposed = false;
        }

        if(currentNodeHasChange())
            breakDown();
    }

    private boolean currentNodeHasChange() {
        IncrementalParseForest node = getNode();
        if(node == null || currentUpdate == null)
            return false;

        // Examples: (current node width indicated with [])
        // 0 [1 2 ]3 D 5 => 4 < 1 + 2 + 1 => false
        // 0 [1 2 3 ]D 5 => 4 < 1 + 3 + 1 => true
        // 0 [1 2 D ]4 5 => 3 < 1 + 3 + 1 => true
        // TODO Instead of +1, it would be cleaner to check the follow-restriction length of the production
        return currentUpdate.deletedStart < currentOffsetInPrevious + node.width() + 1;
    }

    private boolean updateIsAtStartOfNextNode() {
        IncrementalParseForest node = getNode();
        if(node == null || currentUpdate == null)
            return false;

        // Examples: (current node width indicated with [])
        // 0 [1 2 ]3 D 5 => 4 == 1 + 2 => false
        // 0 [1 2 3 ]D 5 => 4 == 1 + 3 => true
        // 0 [1 2 D ]4 5 => 3 == 1 + 3 => false
        // TODO If current node has follow-restriction length > 1, the first example should ALSO return true
        return currentUpdate.deletedStart == currentOffsetInPrevious + node.width();
    }

    @Override public boolean lookaheadIsUnchanged() {
        return !updateIsExposed;
    }
}
