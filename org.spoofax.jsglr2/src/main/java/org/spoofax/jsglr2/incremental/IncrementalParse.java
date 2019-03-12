package org.spoofax.jsglr2.incremental;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.metaborg.parsetable.IState;
import org.metaborg.parsetable.actions.IGoto;
import org.metaborg.sdf2table.parsetable.query.ActionsForCharacterSeparated;
import org.metaborg.sdf2table.parsetable.query.ActionsPerCharacterClass;
import org.metaborg.sdf2table.parsetable.query.ProductionToGotoForLoop;
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
    IncrementalParseForest shiftLookAhead;
    IncrementalParseForest reducerLookAhead;

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
        this.shiftLookAhead = updatedTree;
        this.reducerLookAhead = this.shiftLookAhead;
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
        if(reducerLookAhead instanceof IncrementalCharacterNode) {
            return ((IncrementalCharacterNode) reducerLookAhead).character;
        } else {
            return -1; // TODO not implemented if lookahead is a non-terminal
        }
    }

    @Override public String actionQueryLookahead(int length) {
        char[] chars = new char[length];
        IncrementalParseForest lookAhead = this.reducerLookAhead;
        while(!lookAhead.isTerminal())
            lookAhead = lookAhead.leftBreakdown();
        for(int i = 0; i < length; i++) {
            lookAhead = lookAhead.popLookAhead();
            while(!lookAhead.isTerminal())
                lookAhead = lookAhead.leftBreakdown();
            chars[i] = (char) ((IncrementalCharacterNode) lookAhead).character;
        }
        return new String(chars);
    }

    @Override public boolean hasNext() {
        return shiftLookAhead != null; // null is the lookahead of the EOF node
    }

    @Override public void next() {
        currentOffset += shiftLookAhead.width();
        shiftLookAhead = shiftLookAhead.popLookAhead();
        reducerLookAhead = shiftLookAhead;
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

    // Recursive, "functional" version
    private IncrementalParseForest processUpdates(List<EditorUpdate> editorUpdates, IncrementalParseForest previous) {
        // TODO for all editor updates (currently only checking first update)
        EditorUpdate editorUpdate = editorUpdates.get(0);
        return processUpdates(previous, 0, editorUpdate.deletedStart, editorUpdate.deletedEnd, editorUpdate.inserted);
    }

    private IncrementalParseForest processUpdates(IncrementalParseForest currentForest, int currentOffset,
        int deletedStartOffset, int deletedEndOffset, String inserted) {
        if(currentForest instanceof IncrementalCharacterNode) {
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

    // Imperative version
    private IncrementalParseForest processUpdatesI(List<EditorUpdate> editorUpdates, IncrementalParseForest previous) {
        // TODO for all editor updates (currently only checking first update)
        EditorUpdate editorUpdate = editorUpdates.get(0);
        IncrementalParseForest currentForest = previous;
        int currentOffset = 0;
        int deletedStartOffset = editorUpdate.deletedStart;
        int deletedEndOffset = editorUpdate.deletedEnd;

        while(!currentForest.isTerminal()) {
            while(!currentForest.isTerminal() && deletedStartOffset <= currentOffset + currentForest.width()) {
                ((IncrementalParseNode) currentForest).getDerivations().forEach(IncrementalDerivation::markChanged);
                currentForest = currentForest.leftBreakdown();
            }
            while(deletedStartOffset > currentOffset + currentForest.width()) {
                currentOffset += currentForest.width();
                currentForest = currentForest.popLookAhead();
            }
        }

        // `currentForest` now holds the terminal node after which a new string is inserted
        // Remember the node after this, so we can start deleting from there
        IncrementalParseForest nextNode = currentForest.popLookAhead();

        replaceForestWithNewChildrenImp(currentForest,
            convertToCharacterNodes((IncrementalCharacterNode) currentForest, editorUpdate.inserted));

        currentOffset += 1;
        currentForest = nextNode;
        while(currentOffset < deletedEndOffset) {
            while(deletedEndOffset < currentOffset + currentForest.width())
                currentForest = currentForest.leftBreakdown();
            while(deletedEndOffset >= currentOffset + currentForest.width()) {
                replaceForestWithNewChildrenImp(currentForest, parseForestManager.parseForestsArray(0));
                currentOffset += currentForest.width();
                currentForest = currentForest.popLookAhead();
            }
        }
        return previous;
    }

    private void replaceForestWithNewChildrenImp(IncrementalParseForest currentForest,
        IncrementalParseForest[] newChildren) {
        IncrementalDerivation oldParent = currentForest.parent;
        int oldIndex = currentForest.childIndex;
        oldParent.parseForests[oldIndex] = newParseNodeFromChildren(newChildren);
        oldParent.parseForests[oldIndex].parent = oldParent;
        oldParent.parseForests[oldIndex].childIndex = oldIndex;
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
