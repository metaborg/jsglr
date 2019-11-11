package org.spoofax.jsglr2.incremental.diff;

import static org.spoofax.jsglr2.incremental.EditorUpdate.Type.INSERTION;
import static org.spoofax.jsglr2.incremental.EditorUpdate.Type.REPLACEMENT;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.spoofax.jsglr2.incremental.EditorUpdate;
import org.spoofax.jsglr2.incremental.IIncrementalParseState;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalDerivation;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForestManager;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseNode;
import org.spoofax.jsglr2.inputstack.incremental.IIncrementalInputStack;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;

public class ProcessUpdates
//@formatter:off
   <StackNode extends IStackNode,
    ParseState extends AbstractParseState<IIncrementalInputStack, StackNode> & IIncrementalParseState>
//@formatter:on
{

    private final ParserObserving<IncrementalParseForest, IncrementalDerivation, IncrementalParseNode, StackNode, ParseState> observing;
    private final IncrementalParseForestManager<StackNode, ParseState> parseForestManager;

    public ProcessUpdates(IncrementalParseForestManager<StackNode, ParseState> parseForestManager) {
        this.observing = new ParserObserving<>();
        this.parseForestManager = parseForestManager;
    }

    public IncrementalParseForest processUpdates(IncrementalParseForest previous, EditorUpdate... editorUpdates) {
        return processUpdates(previous, Arrays.asList(editorUpdates));
    }

    /**
     * Recursively processes the tree until the update site has been found. General strategy:
     *
     * <ul>
     * <li>If the update is a replacement (a, b, new) with a != b: replace character a with the new string and delete
     * characters [a+1, b>
     * <li>If the update is an insertion (a, a, new), and
     * <ul>
     * <li>the insertion is at the first character: replace first character with (new, first)
     * <li>the insertion is anywhere else: replace character a with (a, new)
     * </ul>
     * <li>If the update is a deletion (a, b, ""): just delete everything in range [a, b>
     * </ul>
     * 
     * The slightly reasonable assumption has been made that any two consecutive updates never "touch" each other
     * (meaning that first.end == second.start). This method will still work for two consecutive replacements, but no
     * guarantees are made for a consecutive insertion/deletion.
     */
    public IncrementalParseForest processUpdates(IncrementalParseForest previous, List<EditorUpdate> editorUpdates) {
        // Optimization: if everything is deleted/replaced: then return a tree created from the inserted string
        if(editorUpdates.size() == 1) {
            EditorUpdate editorUpdate = editorUpdates.get(0);
            if(editorUpdate.deletedStart == 0 && editorUpdate.deletedEnd == previous.width()) {
                return getParseNodeFromString(editorUpdate.inserted);
            }
        }

        LinkedList<EditorUpdate> linkedUpdates = new LinkedList<>(editorUpdates);
        return processUpdates(previous, 0, linkedUpdates);
    }

    private IncrementalParseForest processUpdates(IncrementalParseForest currentForest, int currentOffset,
        LinkedList<EditorUpdate> updates) {
        if(currentForest.isTerminal()) {
            EditorUpdate update = updates.getFirst();
            int deletedStartOffset = update.deletedStart;
            int deletedEndOffset = update.deletedEnd;
            String inserted = update.inserted;
            EditorUpdate.Type type = update.type;

            // If it is an insertion (there is nothing to delete, deletedStart == deletedEnd)
            if(type == INSERTION) {
                // If insert position is begin of string: prepend to first character
                if(deletedStartOffset == 0 && currentOffset == deletedEndOffset) {
                    updates.removeFirst();
                    return newParseNodeFromChildren(getParseNodeFromString(inserted), currentForest);
                }
                // If insert position is NOT begin of string: append to current character
                if(deletedStartOffset != 0 && currentOffset == deletedStartOffset - 1) {
                    updates.removeFirst();
                    return newParseNodeFromChildren(currentForest, getParseNodeFromString(inserted));
                }
                // If none of the cases applies: just return original character node
                return currentForest;
            }
            // Replace first deleted character with the inserted string (if any)
            if(type == REPLACEMENT && currentOffset == deletedStartOffset) {
                if(currentOffset == deletedEndOffset - 1)
                    updates.removeFirst();
                return getParseNodeFromString(inserted);
            }
            // Else: delete all characters within deletion range
            if(deletedStartOffset <= currentOffset && currentOffset < deletedEndOffset) {
                if(currentOffset == deletedEndOffset - 1)
                    updates.removeFirst();
                return null;
            }
            // If none of the cases applies: just return original character node
            return currentForest;
        }
        // Use a shallow copy of the current children, else the old children array will be modified
        IncrementalParseForest[] parseForests =
            ((IncrementalParseNode) currentForest).getFirstDerivation().parseForests().clone();
        for(int i = 0; i < parseForests.length; i++) {
            if(updates.isEmpty())
                break;
            // If the current subtree is after the previous to-be-deleted range: move to next update
            if(currentOffset >= updates.getFirst().deletedEnd && currentOffset > 0)
                updates.removeFirst();
            if(updates.isEmpty())
                break;

            EditorUpdate update = updates.getFirst();
            int deletedStartOffset = update.deletedStart;
            int deletedEndOffset = update.deletedEnd;
            String inserted = update.inserted;
            EditorUpdate.Type type = update.type;

            IncrementalParseForest parseForest = parseForests[i];
            int nextOffset = currentOffset + parseForest.width(); // == start offset of right sibling subtree

            // Optimization: if current subtree starts exactly at deletedStart and it spans the subtree: replace it
            if(type == REPLACEMENT && deletedStartOffset == currentOffset && nextOffset <= deletedEndOffset
            // (also, it must be at least one character wide, else empty subtrees at the same position get replaced)
                && currentOffset < nextOffset)
                parseForests[i] = getParseNodeFromString(inserted);
            // Optimization: if current subtree is a subrange within [deletedStart, deletedEnd]: delete it
            else if(type == REPLACEMENT && deletedStartOffset <= currentOffset && nextOffset <= deletedEndOffset)
                parseForests[i] = null;
            // If current subtree (partially) overlaps with the to-be-deleted range: recurse
            else if(deletedStartOffset <= nextOffset && currentOffset <= deletedEndOffset)
                parseForests[i] = processUpdates(parseForest, currentOffset, updates);

            currentOffset = nextOffset;
        }
        return newParseNodeFromChildren(parseForests);
    }

    private IncrementalParseNode newParseNodeFromChildren(IncrementalParseForest... newChildren) {
        IncrementalParseForest[] filtered =
            Arrays.stream(newChildren).filter(Objects::nonNull).toArray(IncrementalParseForest[]::new);
        if(filtered.length == 0)
            return null;
        return parseForestManager.createChangedParseNode(filtered);
    }

    public IncrementalParseNode getParseNodeFromString(String inputString) {
        IncrementalParseForest[] parseForests = parseForestManager.parseForestsArray(inputString.length());

        char[] chars = inputString.toCharArray();
        for(int i = 0; i < chars.length; i++) {
            parseForests[i] = parseForestManager.createCharacterNode(chars[i]);
        }
        return parseForestManager.createChangedParseNode(parseForests);
    }
}
