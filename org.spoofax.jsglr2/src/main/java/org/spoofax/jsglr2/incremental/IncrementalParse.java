package org.spoofax.jsglr2.incremental;

import java.util.List;

import org.metaborg.parsetable.actions.IGoto;
import org.metaborg.parsetable.query.ActionsForCharacterSeparated;
import org.metaborg.parsetable.query.ActionsPerCharacterClass;
import org.metaborg.parsetable.query.ProductionToGotoForLoop;
import org.metaborg.parsetable.states.State;
import org.spoofax.jsglr2.incremental.diff.ProcessUpdates;
import org.spoofax.jsglr2.incremental.lookaheadstack.EagerLookaheadStack;
import org.spoofax.jsglr2.incremental.lookaheadstack.ILookaheadStack;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.IParseState;
import org.spoofax.jsglr2.parser.ParseFactory;
import org.spoofax.jsglr2.parser.ParserVariant;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;

public class IncrementalParse
//@formatter:off
   <StackNode  extends IStackNode,
    ParseState extends IParseState<IncrementalParseForest, StackNode>>
//@formatter:on
    extends AbstractParse<IncrementalParseForest, StackNode, ParseState> implements IIncrementalParse {

    private boolean multipleStates = false;
    ILookaheadStack lookahead;

    private final ProcessUpdates<StackNode> processUpdates = new ProcessUpdates<>(this);
    public static final State NO_STATE = new State(-1,
        new ActionsForCharacterSeparated(new ActionsPerCharacterClass[0]), new ProductionToGotoForLoop(new IGoto[0]));

    public IncrementalParse(ParserVariant variant, List<EditorUpdate> editorUpdates, IncrementalParseForest previous,
        String inputString, String filename, ParserObserving<IncrementalParseForest, StackNode, ParseState> observing) {
        super(variant, inputString, filename, observing);
        initParse(processUpdates.processUpdates(previous, editorUpdates), inputString);
    }

    public IncrementalParse(ParserVariant variant, String inputString, String filename,
        ParserObserving<IncrementalParseForest, StackNode, ParseState> observing) {
        super(variant, inputString, filename, observing);
        initParse(processUpdates.getParseNodeFromString(inputString), inputString);
    }

    private void initParse(IncrementalParseForest updatedTree, String inputString) {
        this.lookahead = new EagerLookaheadStack(updatedTree, inputString); // TODO switch types between Lazy and Eager
        this.currentChar = lookahead.actionQueryCharacter();
    }

    public static <StackNode_ extends IStackNode, ParseState_ extends IParseState<IncrementalParseForest, StackNode_>>
        IncrementalParseFactory<StackNode_, ParseState_, IncrementalParse<StackNode_, ParseState_>>
        incrementalFactory(ParserVariant variant) {
        return (editorUpdates, previousVersion, inputString, filename, observing) -> new IncrementalParse<>(variant,
            editorUpdates, previousVersion, inputString, filename, observing);
    }

    public static <StackNode_ extends IStackNode, ParseState_ extends IParseState<IncrementalParseForest, StackNode_>>
        ParseFactory<IncrementalParseForest, StackNode_, ParseState_, IncrementalParse<StackNode_, ParseState_>>
        factory(ParserVariant variant) {
        return (inputString, filename, observing) -> (IncrementalParse<StackNode_, ParseState_>) new IncrementalParse<>(
            variant, inputString, filename, observing);
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

    @Override public ILookaheadStack lookahead() {
        return lookahead;
    }

    @Override public boolean isMultipleStates() {
        return multipleStates;
    }

    @Override public void setMultipleStates(boolean multipleStates) {
        this.multipleStates = multipleStates;
    }
}
