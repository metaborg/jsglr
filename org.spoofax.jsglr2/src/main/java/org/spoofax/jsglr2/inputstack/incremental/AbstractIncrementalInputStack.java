package org.spoofax.jsglr2.inputstack.incremental;

import static org.spoofax.jsglr2.parser.observing.IParserObserver.BreakdownReason;

import java.util.List;

import org.metaborg.util.functions.Function5;
import org.spoofax.jsglr2.incremental.EditorUpdate;
import org.spoofax.jsglr2.incremental.diff.IStringDiff;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseNode;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalSkippedNode;
import org.spoofax.jsglr2.parser.observing.ParserObserving;

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

    // TODO move this field to parent class
    private final ParserObserving<?, ?, ?, ?, ?> observing;

    private final String previousInput;
    private final List<EditorUpdate> editorUpdates;
    private int currentUpdateIndex = 0;
    private EditorUpdate currentUpdate = null;
    private boolean updateIsExposed = false;

    private int currentOffsetInPrevious = 0;

    AbstractIncrementalInputStack(AbstractIncrementalInputStack original) {
        super(original);

        this.observing = original.observing;
        this.previousInput = original.previousInput;
        this.editorUpdates = original.editorUpdates;
        this.currentUpdateIndex = original.currentUpdateIndex;
        this.currentUpdate = original.currentUpdate;
        this.updateIsExposed = original.updateIsExposed;
        this.currentOffsetInPrevious = original.currentOffsetInPrevious;
    }

    public AbstractIncrementalInputStack(String input, String previousInput, IncrementalParseForest previousResult,
        List<EditorUpdate> editorUpdates, ParserObserving<?, ?, ?, ?, ?> observing) {
        super(previousResult, input);

        this.observing = observing;

        this.previousInput = previousInput;
        this.editorUpdates = editorUpdates;

        if(editorUpdates.isEmpty())
            return;

        this.currentUpdate = editorUpdates.get(0);

        checkUpdate();
    }

    static IncrementalInputStackFactory<IIncrementalInputStack> factoryBuilder(IStringDiff diff,
        Function5<String, String, IncrementalParseForest, List<EditorUpdate>, ParserObserving<?, ?, ?, ?, ?>, IIncrementalInputStack> constructor) {
        return (inputString, previousInput, previousResult, observing) -> {
            if(previousInput == null || previousResult == null)
                return new StringIncrementalInputStack(inputString);

            List<EditorUpdate> editorUpdates = diff.diff(previousInput, inputString);

            // Optimization: if everything is deleted/replaced, then start a batch parse
            if(editorUpdates.size() == 1 && editorUpdates.get(0).deletedStart == 0
                && editorUpdates.get(0).deletedEnd == previousResult.width())
                return new StringIncrementalInputStack(inputString);

            return constructor.apply(inputString, previousInput, previousResult, editorUpdates, observing);
        };
    }

    @Override public void breakDown(BreakdownReason breakdownReason) {
        do {
            if(stack.isEmpty())
                return;

            IncrementalParseForest current = stack.peek();
            if(current.isTerminal())
                return;

            BreakdownReason finalBreakdownReason = breakdownReason;
            observing.notify(observer -> observer.breakDown(this, finalBreakdownReason));

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

            breakdownReason = BreakdownReason.HAS_CHANGE; // In case we need to loop

            // The call to breakDown() from `IncrementalParser` usually only needs to break down a single parse node,
            // because parse nodes can only get smaller and the current offset will not change,
            // so the children of the broken-down parse node don't suddenly accidentally expose an update.
            // HOWEVER, when the broken-down node has NO children, the next parse node on the stack CAN contain changes.
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
                    breakDown(BreakdownReason.HAS_CHANGE);
                else {
                    // By default, the observer does not count the children of the popped node.
                    observing.notify(observer -> observer.breakDown(this, BreakdownReason.HAS_CHANGE));
                    // The observer call below DOES count them, but that's disabled because its calculation is slow.
                    // observing.notify(observer -> {
                    // Stack<IncrementalParseForest> toCheck = new Stack<>();
                    // toCheck.add(stack.peek());
                    // while(!toCheck.isEmpty()) {
                    // IncrementalParseForest current = toCheck.pop();
                    // if(!current.isTerminal()) {
                    // observer.breakDown(this, BreakdownReason.HAS_CHANGE);
                    // toCheck.addAll(Arrays
                    // .asList(((IncrementalParseNode) current).getFirstDerivation().parseForests()));
                    // }
                    // }
                    // });
                    currentOffsetInPrevious += stack.pop().width();
                }
            // Also delete any null-yield trees at position `currentUpdate.deletedEnd`
            while(stack.peek().width() == 0) {
                observing.notify(observer -> observer.breakDown(this, BreakdownReason.HAS_CHANGE));
                stack.pop();
            }
            currentOffsetInPrevious -= currentUpdate.insertedLength();
            pushCharactersToStack(currentUpdate.inserted);
            currentUpdate = ++currentUpdateIndex >= editorUpdates.size() ? null : editorUpdates.get(currentUpdateIndex);
            updateIsExposed = false;
        }

        // while(currentNodeHasChange() && !getNode().isTerminal()) // This already loops inside breakDown()
        if(currentNodeHasChange())
            breakDown(BreakdownReason.HAS_CHANGE);
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
