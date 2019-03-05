package org.spoofax.jsglr2.incremental;

import java.util.List;

import org.metaborg.characterclasses.CharacterClassFactory;
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
        initParse(processUpdates(editorUpdates, previous), observing);
    }

    public IncrementalParse(String inputString, String filename, IActiveStacks<StackNode> activeStacks,
        IForActorStacks<StackNode> forActorStacks, ParserObserving<IncrementalParseForest, StackNode> observing) {

        super(inputString, filename, activeStacks, forActorStacks, observing);
        initParse(new IncrementalParseNode(null, getDerivationFromString(inputString)), observing);
    }

    private void initParse(IncrementalParseForest updatedTree,
        ParserObserving<IncrementalParseForest, StackNode> observing) {
        this.shiftLookAhead = updatedTree;
        this.reducerLookAhead = this.shiftLookAhead;
        this.multipleStates = false;
        this.currentChar = actionQueryCharacter();

        observing.notify(
            observer -> observer.createCharacterNode(IncrementalCharacterNode.EOF_NODE, CharacterClassFactory.EOF_INT));
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

    private IncrementalDerivation getDerivationFromString(String inputString) {
        IncrementalParseForest[] parseForests = parseForestManager.parseForestsArray(inputString.length());

        char[] chars = inputString.toCharArray();
        for(int i = 0; i < chars.length; i++) {
            parseForests[i] = new IncrementalCharacterNode(chars[i]);
        }
        return new IncrementalDerivation(null, null, parseForests, NO_STATE);
    }

    private IncrementalParseForest processUpdates(List<EditorUpdate> editorUpdates, IncrementalParseForest previous) {
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
        // `currentParent` now holds the terminal node after which a new string is inserted
        IncrementalDerivation oldParent = currentForest.parent;
        int oldIndex = currentForest.childIndex;
        IncrementalDerivation derivation = getDerivationFromUpdate(currentForest, editorUpdate);
        // Replace the character node with a parse node that contains one character node per inserted character
        oldParent.parseForests[oldIndex] = new IncrementalParseNode(null, derivation);
        oldParent.parseForests[oldIndex].parent = oldParent;
        oldParent.parseForests[oldIndex].childIndex = oldIndex;

        currentOffset += 1;
        currentForest = oldParent.parent.popLookAhead();
        while(currentOffset < deletedEndOffset) {
            while(deletedEndOffset < currentOffset + currentForest.width())
                currentForest = currentForest.leftBreakdown();
            while(deletedEndOffset >= currentOffset + currentForest.width()) {
                oldParent = currentForest.parent;
                oldIndex = currentForest.childIndex;
                oldParent.parseForests[oldIndex] = new IncrementalParseNode(null,
                    new IncrementalDerivation(null, null, new IncrementalParseForest[0], null));
                oldParent.parseForests[oldIndex].parent = oldParent;
                oldParent.parseForests[oldIndex].childIndex = oldIndex;

                currentOffset += currentForest.width();
                currentForest = currentForest.popLookAhead();
            }
        }
        return previous;
    }

    private IncrementalDerivation getDerivationFromUpdate(IncrementalParseForest originalLookahead,
        EditorUpdate editorUpdate) {
        IncrementalParseForest[] parseForests =
            parseForestManager.parseForestsArray(editorUpdate.insterted.length() + 1);
        parseForests[0] = originalLookahead;

        char[] chars = editorUpdate.insterted.toCharArray();
        for(int i = 0; i < chars.length; i++) {
            parseForests[i + 1] = new IncrementalCharacterNode(chars[i]);
        }
        return new IncrementalDerivation(null, null, parseForests, NO_STATE);
    }
}
