package org.spoofax.jsglr2.incremental;

import java.util.List;

import org.metaborg.parsetable.actions.IGoto;
import org.metaborg.parsetable.query.ActionsForCharacterSeparated;
import org.metaborg.parsetable.query.ActionsPerCharacterClass;
import org.metaborg.parsetable.query.ProductionToGotoForLoop;
import org.metaborg.parsetable.states.State;
import org.spoofax.jsglr2.JSGLR2Variants;
import org.spoofax.jsglr2.incremental.diff.ProcessUpdates;
import org.spoofax.jsglr2.incremental.lookaheadstack.EagerLookaheadStack;
import org.spoofax.jsglr2.incremental.lookaheadstack.ILookaheadStack;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.ParseFactory;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.collections.ActiveStacksFactory;
import org.spoofax.jsglr2.stack.collections.ForActorStacksFactory;
import org.spoofax.jsglr2.stack.collections.IActiveStacksFactory;
import org.spoofax.jsglr2.stack.collections.IForActorStacksFactory;

public class IncrementalParse<StackNode extends IStackNode> extends AbstractParse<IncrementalParseForest, StackNode> {

    // TODO this should not be public but still end up in the IncrementalParseForestManager and IncrementalReduceManager
    public boolean multipleStates = false;
    ILookaheadStack lookahead;

    private final ProcessUpdates<StackNode> processUpdates = new ProcessUpdates<>(this);
    public static final State NO_STATE = new State(-1,
        new ActionsForCharacterSeparated(new ActionsPerCharacterClass[0]), new ProductionToGotoForLoop(new IGoto[0]));

    public IncrementalParse(List<EditorUpdate> editorUpdates, IncrementalParseForest previous, String inputString,
        String filename, IActiveStacksFactory activeStacksFactory, IForActorStacksFactory forActorStacksFactory,
        ParserObserving<IncrementalParseForest, StackNode> observing) {

        super(inputString, filename, activeStacksFactory, forActorStacksFactory, observing);
        initParse(processUpdates.processUpdates(previous, editorUpdates), inputString);
    }

    public IncrementalParse(String inputString, String filename, IActiveStacksFactory activeStacksFactory,
        IForActorStacksFactory forActorStacksFactory, ParserObserving<IncrementalParseForest, StackNode> observing) {

        super(inputString, filename, activeStacksFactory, forActorStacksFactory, observing);
        initParse(processUpdates.getParseNodeFromString(inputString), inputString);
    }

    private void initParse(IncrementalParseForest updatedTree, String inputString) {
        this.lookahead = new EagerLookaheadStack(updatedTree, inputString); // TODO switch types between Lazy and Eager
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

}
