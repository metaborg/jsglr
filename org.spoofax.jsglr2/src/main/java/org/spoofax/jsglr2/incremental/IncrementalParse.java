package org.spoofax.jsglr2.incremental;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.metaborg.parsetable.IState;
import org.metaborg.parsetable.actions.IGoto;
import org.metaborg.sdf2table.parsetable.query.ActionsForCharacterSeparated;
import org.metaborg.sdf2table.parsetable.query.ActionsPerCharacterClass;
import org.metaborg.sdf2table.parsetable.query.ProductionToGotoForLoop;
import org.spoofax.jsglr2.incremental.lookaheadstack.EagerLookaheadStack;
import org.spoofax.jsglr2.incremental.lookaheadstack.ILookaheadStack;
import org.spoofax.jsglr2.incremental.parseforest.*;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.ParseFactory;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.collections.IActiveStacks;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;
import org.spoofax.jsglr2.states.State;

public class IncrementalParse<StackNode extends IStackNode> extends AbstractParse<IncrementalParseForest, StackNode> {

    public IState state;
    boolean multipleStates;
    ILookaheadStack shiftLookahead;
    ILookaheadStack reducerLookahead;

    private IncrementalParseForestManager parseForestManager = new IncrementalParseForestManager();
    public static State NO_STATE = new State(-1, new ActionsForCharacterSeparated(new ActionsPerCharacterClass[0]),
        new ProductionToGotoForLoop(new IGoto[0]));

    public IncrementalParse(List<EditorUpdate> editorUpdates, IncrementalParseForest previous, String filename,
        IActiveStacks<StackNode> activeStacks, IForActorStacks<StackNode> forActorStacks,
        ParserObserving<IncrementalParseForest, StackNode> observing) {

        super("<no input string available for incremental parsing>", filename, activeStacks, forActorStacks, observing);
        initParse(processUpdates(editorUpdates, previous));
    }

    public IncrementalParse(String inputString, String filename, IActiveStacks<StackNode> activeStacks,
        IForActorStacks<StackNode> forActorStacks, ParserObserving<IncrementalParseForest, StackNode> observing) {

        super(inputString, filename, activeStacks, forActorStacks, observing);
        initParse(getParseNodeFromString(inputString));
    }

    private void initParse(IncrementalParseForest updatedTree) {
        this.shiftLookahead = new EagerLookaheadStack(updatedTree); // TODO switch types between Lazy and Eager
        this.reducerLookahead = new EagerLookaheadStack(updatedTree);
        this.multipleStates = false;
        this.currentChar = actionQueryCharacter();
    }

    // @formatter:off
    public static <StackNode_ extends IStackNode>
        IncrementalParseFactory<StackNode_, IncrementalParse<StackNode_>> incrementalFactory() {
        return IncrementalParse::new;
    }

    public static <StackNode_ extends IStackNode>
        ParseFactory<IncrementalParseForest, StackNode_, IncrementalParse<StackNode_>> factory() {
        return IncrementalParse::new;
    }
    // @formatter:on

    @Override public String getPart(int begin, int end) {
        return null; // TODO remove getPart from AbstractParse
    }

    @Override public int actionQueryCharacter() {
        return reducerLookahead.actionQueryCharacter();
    }

    @Override public String actionQueryLookahead(int length) {
        return reducerLookahead.actionQueryLookahead(length);
    }

    @Override public boolean hasNext() {
        return shiftLookahead.get() != null; // null is the lookahead of the EOF node
    }

    @Override public void next() {
        currentOffset += shiftLookahead.get().width();
        shiftLookahead.popLookahead();
        reducerLookahead.popLookahead();
        assert shiftLookahead.get() == reducerLookahead.get() : "Lock-step property is broken\nReduce lookahead:\n"
            + reducerLookahead.get() + "\nShift lookahead:\n" + shiftLookahead.get();
        currentChar = actionQueryCharacter();
    }

    private IncrementalParseNode getParseNodeFromString(String inputString) {
        IncrementalParseForest[] parseForests = parseForestManager.parseForestsArray(inputString.length());

        char[] chars = inputString.toCharArray();
        for(int i = 0; i < chars.length; i++) {
            // TODO should we use the IncrementalParseForestManager for this?
            parseForests[i] = new IncrementalCharacterNode(chars[i]);
        }
        return newParseNodeFromChildren(parseForests);
    }

    // Recursively processes the tree until the update site has been found
    private IncrementalParseForest processUpdates(List<EditorUpdate> editorUpdates, IncrementalParseForest previous) {
        // TODO for all editor updates (currently only checking first update)
        EditorUpdate editorUpdate = editorUpdates.get(0);
        return processUpdates(previous, 0, editorUpdate.deletedStart, editorUpdate.deletedEnd, editorUpdate.inserted);
    }

    private IncrementalParseForest processUpdates(IncrementalParseForest currentForest, int currentOffset,
        int deletedStartOffset, int deletedEndOffset, String inserted) {
        if(currentForest instanceof IncrementalCharacterNode) {
            // TODO this breaks when deletedStartOffset == 0
            if(currentOffset == deletedStartOffset - 1)
                return newParseNodeFromChildren(
                    convertToCharacterNodes((IncrementalCharacterNode) currentForest, inserted));
            if(deletedStartOffset <= currentOffset && currentOffset < deletedEndOffset)
                return null;
            return currentForest;
        }
        IncrementalParseForest[] parseForests =
            ((IncrementalParseNode) currentForest).getFirstDerivation().parseForests();
        for(int i = 0; i < parseForests.length; i++) {
            IncrementalParseForest parseForest = parseForests[i];
            if(deletedStartOffset <= currentOffset + parseForest.width() && currentOffset < deletedEndOffset) {
                parseForests[i] =
                    processUpdates(parseForest, currentOffset, deletedStartOffset, deletedEndOffset, inserted);
            }
            currentOffset += parseForest.width();
        }
        return newParseNodeFromChildren(parseForests);
    }

    private IncrementalParseNode newParseNodeFromChildren(IncrementalParseForest[] newChildren) {
        // TODO should we use the IncrementalParseForestManager for this?
        IncrementalParseForest[] filtered =
            Arrays.stream(newChildren).filter(Objects::nonNull).toArray(IncrementalParseForest[]::new);
        if(filtered.length == 0 && newChildren.length != 0) // Only return null if length _changed_
            return null;
        return new IncrementalParseNode(null, new IncrementalDerivation(null, null, filtered, NO_STATE));
    }

    private IncrementalParseForest[] convertToCharacterNodes(IncrementalCharacterNode originalLookahead,
        String inserted) {
        IncrementalParseForest[] children = parseForestManager.parseForestsArray(inserted.length() + 1);
        children[0] = originalLookahead;

        char[] chars = inserted.toCharArray();
        for(int i = 0; i < chars.length; i++) {
            // TODO should we use the IncrementalParseForestManager for this?
            children[i + 1] = new IncrementalCharacterNode(chars[i]);
        }
        return children;
    }
}
