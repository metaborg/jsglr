package org.spoofax.jsglr2.incremental;

import java.util.Collections;
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
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.parser.PositionInterval;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.collections.IActiveStacks;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;
import org.spoofax.jsglr2.states.State;

public class IncrementalParse<StackNode extends IStackNode> extends AbstractParse<IncrementalParseForest, StackNode> {

    private final List<EditorUpdate> editorUpdates;
    private final IncrementalParseForest previous;
    public IState state;
    boolean multipleStates;
    IncrementalParseForest shiftLookAhead;
    IncrementalParseForest reducerLookAhead;
    Position currentPosition = new Position(0, 1, 1);

    private IncrementalParseForestManager parseForestManager = new IncrementalParseForestManager();
    public static State NO_STATE = new State(-1, new ActionsForCharacterSeparated(new ActionsPerCharacterClass[0]),
        new ProductionToGotoForLoop(new IGoto[0]));

    public IncrementalParse(List<EditorUpdate> editorUpdates, IncrementalParseForest previous, String filename,
        IActiveStacks<StackNode> activeStacks, IForActorStacks<StackNode> forActorStacks,
        ParserObserving<IncrementalParseForest, StackNode> observing) {

        super("<<<<<<<<<<<<<<<<<<<", filename, activeStacks, forActorStacks, observing);
        this.editorUpdates = editorUpdates;
        if(previous == null) {
            // TODO this only works when starting with a clean slate
            previous = new IncrementalParseNode(null, getDerivationFromUpdate(editorUpdates.get(0)));
        }
        this.previous = previous;
        this.shiftLookAhead = previous;
        this.reducerLookAhead = previous;
        this.multipleStates = false;
        this.currentChar = actionQueryCharacter();

        observing.notify(
            observer -> observer.createCharacterNode(IncrementalCharacterNode.EOF_NODE, CharacterClassFactory.EOF_INT));
    }

    public IncrementalParse(String inputString, String filename, IActiveStacks<StackNode> activeStacks,
        IForActorStacks<StackNode> forActorStacks, ParserObserving<IncrementalParseForest, StackNode> observing) {

        this(
            Collections.singletonList(
                new EditorUpdate(new PositionInterval(new Position(0, 1, 1), new Position(0, 1, 1)), inputString)),
            null, filename, activeStacks, forActorStacks, observing);
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
        return ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"; // TODO

        // return CharacterClassFactory.intToString(character);

        // StringBuilder sb = new StringBuilder();
        // for(IncrementalDerivation derivation : getDerivations()) {
        // for(IncrementalParseForest incrementalParseForest : derivation.parseForests()) {
        // sb.append(incrementalParseForest.inputPart());
        // }
        // }
        // return sb.toString();
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
        int width = shiftLookAhead.width();
        currentOffset += width;
        // TODO for all editor updates, and correctly
        // EditorUpdate editorUpdate = editorUpdates.get(0);
        // if(editorUpdate.deleted.getStart().offset == currentOffset) {
        // IncrementalDerivation derivation = getDerivationFromUpdate(editorUpdate);
        // shiftLookAhead.parent.parent.addDerivation(derivation);
        // shiftLookAhead = derivation.parseForests()[0];
        // } else
        shiftLookAhead = shiftLookAhead.popLookAhead();
        reducerLookAhead = shiftLookAhead;
        currentChar = actionQueryCharacter();
    }

    private IncrementalDerivation getDerivationFromUpdate(EditorUpdate editorUpdate) {
        IncrementalParseForest[] parseForests = parseForestManager.parseForestsArray(editorUpdate.insterted.length());
        char[] chars = editorUpdate.insterted.toCharArray();
        for(int i = 0; i < chars.length; i++) {
            parseForests[i] = new IncrementalCharacterNode(chars[i]);
        }
        return new IncrementalDerivation(null, null, parseForests, NO_STATE);
    }
}
