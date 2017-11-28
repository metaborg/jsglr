package org.spoofax.jsglr2.benchmark.jsglr2.datastructures;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.infra.Blackhole;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.JSGLR2Variants;
import org.spoofax.jsglr2.JSGLR2Variants.ParseForestConstruction;
import org.spoofax.jsglr2.JSGLR2Variants.ParseForestRepresentation;
import org.spoofax.jsglr2.JSGLR2Variants.Reducing;
import org.spoofax.jsglr2.JSGLR2Variants.StackRepresentation;
import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IActionsFactory;
import org.spoofax.jsglr2.benchmark.BaseBenchmark;
import org.spoofax.jsglr2.benchmark.BenchmarkParserObserver;
import org.spoofax.jsglr2.characters.CharacterClassFactory;
import org.spoofax.jsglr2.characters.ICharacterClassFactory;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.IParser;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.parsetable.IParseTable;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.parsetable.ParseTableReader;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.states.ActionsPerCharacterClassRepresentation;
import org.spoofax.jsglr2.states.IState;
import org.spoofax.jsglr2.states.IStateFactory;
import org.spoofax.jsglr2.states.StateFactory;
import org.spoofax.jsglr2.testset.Input;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.terms.ParseError;

public abstract class JSGLR2StateApplicableActionsBenchmark extends BaseBenchmark {

    IParser<?, ?> parser;
    ActorObserver actorObserver;

    protected JSGLR2StateApplicableActionsBenchmark(TestSet testSet) {
        super(testSet);
    }

    @Param ActionsPerCharacterClassRepresentation actionsPerCharacterClassRepresentation;

    @Param({ "false", "true" }) public boolean optimizeCharacterClasses;

    @Param({ "false", "true" }) public boolean cacheCharacterClasses;

    @Setup public void parserSetup() throws ParseError, ParseTableReadException, IOException,
        InvalidParseTableException, InterruptedException, URISyntaxException {
        ICharacterClassFactory characterClassFactory = new CharacterClassFactory(optimizeCharacterClasses, cacheCharacterClasses);
        IActionsFactory actionsFactory = IAction.factory();
        IStateFactory stateFactory = new StateFactory(actionsPerCharacterClassRepresentation,
            StateFactory.defaultProductionToGotoRepresentation);

        IParseTable parseTable =
            new ParseTableReader(characterClassFactory, actionsFactory, stateFactory).read(testSetReader.getParseTableTerm());

        parser = JSGLR2Variants.getParser(parseTable, ParseForestRepresentation.Basic, ParseForestConstruction.Full,
            StackRepresentation.Basic, Reducing.Basic);

        actorObserver = new ActorObserver();

        parser.attachObserver(actorObserver);

        try {
            for(Input input : inputs)
                parser.parseUnsafe(input.content, input.filename, null);
        } catch(ParseException e) {
            throw new IllegalStateException("setup of benchmark should not fail");
        }
    }

    class ActorOnState {

        final IState state;
        final int character;

        public ActorOnState(IState state, int character) {
            this.state = state;
            this.character = character;
        }

        public void iterateOverApplicableActions(Blackhole bh) {
            for(IAction action : state.getActions(character))
                bh.consume(action);
        }

    }

    class ActorObserver<StackNode extends AbstractStackNode<ParseForest>, ParseForest extends AbstractParseForest>
        extends BenchmarkParserObserver<StackNode, ParseForest> {

        public List<ActorOnState> stateApplicableActions = new ArrayList<ActorOnState>();

        @Override public void actor(StackNode stack, Parse<StackNode, ParseForest> parse,
            Iterable<IAction> applicableActions) {
            ActorOnState stateApplicableActionsForActor = new ActorOnState(stack.state, parse.currentChar);

            stateApplicableActions.add(stateApplicableActionsForActor);
        }

    }

    @Benchmark public void benchmark(Blackhole bh) throws ParseException {
        for(ActorOnState stateApplicableActions : ((ActorObserver<?, ?>) actorObserver).stateApplicableActions)
            stateApplicableActions.iterateOverApplicableActions(bh);
    }

}
