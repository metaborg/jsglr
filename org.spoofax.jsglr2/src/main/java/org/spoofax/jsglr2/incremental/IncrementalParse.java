package org.spoofax.jsglr2.incremental;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.metaborg.parsetable.actions.IGoto;
import org.metaborg.sdf2table.parsetable.query.ActionsForCharacterSeparated;
import org.metaborg.sdf2table.parsetable.query.ActionsPerCharacterClass;
import org.metaborg.sdf2table.parsetable.query.ProductionToGotoForLoop;
import org.spoofax.jsglr2.JSGLR2Variants;
import org.spoofax.jsglr2.incremental.lookaheadstack.EagerLookaheadStack;
import org.spoofax.jsglr2.incremental.lookaheadstack.ILookaheadStack;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForestManager;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseNode;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.ParseFactory;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.collections.ActiveStacksFactory;
import org.spoofax.jsglr2.stack.collections.ForActorStacksFactory;
import org.spoofax.jsglr2.stack.collections.IActiveStacksFactory;
import org.spoofax.jsglr2.stack.collections.IForActorStacksFactory;
import org.spoofax.jsglr2.states.State;

public class IncrementalParse<StackNode extends IStackNode> extends AbstractParse<IncrementalParseForest, StackNode> {

    // TODO this should not be public, but still end up in the IncrementalParseForestManager
    public boolean multipleStates;
    ILookaheadStack lookahead;

    private IncrementalParseForestManager parseForestManager = new IncrementalParseForestManager();
    public static State NO_STATE = new State(-1, new ActionsForCharacterSeparated(new ActionsPerCharacterClass[0]),
        new ProductionToGotoForLoop(new IGoto[0]));

    public IncrementalParse(List<EditorUpdate> editorUpdates, IncrementalParseForest previous, String inputString,
        String filename, IActiveStacksFactory activeStacksFactory, IForActorStacksFactory forActorStacksFactory,
        ParserObserving<IncrementalParseForest, StackNode> observing) {

        super(inputString, filename, activeStacksFactory, forActorStacksFactory, observing);
        initParse(processUpdates(editorUpdates, previous), inputString);
    }

    public IncrementalParse(String inputString, String filename, IActiveStacksFactory activeStacksFactory,
        IForActorStacksFactory forActorStacksFactory, ParserObserving<IncrementalParseForest, StackNode> observing) {

        super(inputString, filename, activeStacksFactory, forActorStacksFactory, observing);
        initParse(getParseNodeFromString(inputString), inputString);
    }

    private void initParse(IncrementalParseForest updatedTree, String inputString) {
        this.lookahead = new EagerLookaheadStack(updatedTree, inputString); // TODO switch types between Lazy and Eager
        this.multipleStates = false;
        this.currentChar = lookahead.actionQueryCharacter();
    }

    public static <StackNode_ extends IStackNode> IncrementalParseFactory<StackNode_, IncrementalParse<StackNode_>>
        incrementalFactory(JSGLR2Variants.ParserVariant variant) {

        ActiveStacksFactory activeStacksFactory = new ActiveStacksFactory(variant.activeStacksRepresentation);
        ForActorStacksFactory forActorStacksFactory = new ForActorStacksFactory(variant.forActorStacksRepresentation);
        return (editorUpdates, previousVersion, inputString, filename, observing) -> new IncrementalParse<>(
            editorUpdates, previousVersion, inputString, filename, activeStacksFactory, forActorStacksFactory,
            observing);
    }

    public static <StackNode_ extends IStackNode>
        ParseFactory<IncrementalParseForest, StackNode_, IncrementalParse<StackNode_>>
        factory(JSGLR2Variants.ParserVariant variant) {

        ActiveStacksFactory activeStacksFactory = new ActiveStacksFactory(variant.activeStacksRepresentation);
        ForActorStacksFactory forActorStacksFactory = new ForActorStacksFactory(variant.forActorStacksRepresentation);
        return (inputString, filename, observing) -> new IncrementalParse<>(inputString, filename, activeStacksFactory,
            forActorStacksFactory, observing);
    }

    @Override public int actionQueryCharacter() {
        return currentChar;
    }

    @Override public String actionQueryLookahead(int length) {
        return lookahead.actionQueryLookahead(length);
    }

    @Override public boolean hasNext() {
        return lookahead.get() != null; // null is the lookahead of the EOF node
    }

    @Override public void next() {
        currentOffset += lookahead.get().width();
        lookahead.popLookahead();
        currentChar = lookahead.actionQueryCharacter();
    }

    // Recursively processes the tree until the update site has been found
    private IncrementalParseForest processUpdates(List<EditorUpdate> editorUpdates, IncrementalParseForest previous) {
        // TODO for all editor updates (currently only checking first update)
        EditorUpdate editorUpdate = editorUpdates.get(0);
        // If everything is deleted, then just return the inserted string
        if(editorUpdate.deletedStart == 0 && editorUpdate.deletedEnd == previous.width())
            return getParseNodeFromString(editorUpdate.inserted);
        return processUpdates(previous, 0, editorUpdate.deletedStart, editorUpdate.deletedEnd, editorUpdate.inserted);
    }

    private IncrementalParseForest processUpdates(IncrementalParseForest currentForest, int currentOffset,
        int deletedStartOffset, int deletedEndOffset, String inserted) {
        if(currentForest.isTerminal()) {
            // If there is nothing to delete
            if(deletedStartOffset == deletedEndOffset) {
                // Insert-after strategy (can also be applied in the general case, but gives less subtree re-use)
                // If insert position is begin of string, prepend to first character
                if(deletedStartOffset == 0 && currentOffset == deletedEndOffset)
                    return newParseNodeFromChildren(getParseNodeFromString(inserted), currentForest);
                // If insert position is NOT begin of string, append to previous character
                if(deletedStartOffset != 0 && currentOffset == deletedStartOffset - 1)
                    return newParseNodeFromChildren(currentForest, getParseNodeFromString(inserted));
                // If none of the cases applies, just return original character node
                return currentForest;
            }
            // In-place replace strategy
            // Replace first deleted character with the inserted string
            if(deletedStartOffset == currentOffset)
                return getParseNodeFromString(inserted);
            // Delete all other characters
            if(deletedStartOffset < currentOffset && currentOffset < deletedEndOffset)
                return null;
            // If none of the cases applies, just return original character node
            return currentForest;
        }
        // Use a shallow copy of the current children, else the old children array will be modified
        IncrementalParseForest[] parseForests =
            ((IncrementalParseNode) currentForest).getFirstDerivation().parseForests().clone();
        for(int i = 0; i < parseForests.length; i++) {
            IncrementalParseForest parseForest = parseForests[i];
            int nextOffset = currentOffset + parseForest.width(); // == start offset of right sibling subtree
            // Optimization: if current subtree falls completely within [deletedStart + 1, deletedEnd], delete it
            if(deletedStartOffset < currentOffset && nextOffset <= deletedEndOffset)
                parseForests[i] = null;
            // If current subtree overlaps with the to-be-deleted range, recurse
            else if(deletedStartOffset <= nextOffset && currentOffset <= deletedEndOffset)
                parseForests[i] =
                    processUpdates(parseForest, currentOffset, deletedStartOffset, deletedEndOffset, inserted);
            currentOffset = nextOffset;
        }
        return newParseNodeFromChildren(parseForests);
    }

    private IncrementalParseNode newParseNodeFromChildren(IncrementalParseForest... newChildren) {
        IncrementalParseForest[] filtered =
            Arrays.stream(newChildren).filter(Objects::nonNull).toArray(IncrementalParseForest[]::new);
        if(filtered.length == 0)
            return null;
        return parseForestManager.createChangedParseNode(this, filtered);
    }

    private IncrementalParseNode getParseNodeFromString(String inputString) {
        IncrementalParseForest[] parseForests = parseForestManager.parseForestsArray(inputString.length());

        char[] chars = inputString.toCharArray();
        for(int i = 0; i < chars.length; i++) {
            parseForests[i] = parseForestManager.createCharacterNode(this, chars[i]);
        }
        return parseForestManager.createChangedParseNode(this, parseForests);
    }

}
