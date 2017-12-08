package org.spoofax.jsglr2.benchmark.jsglr2.datastructures;

import java.util.ArrayList;
import java.util.List;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.infra.Blackhole;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.actions.ActionsFactory;
import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IActionsFactory;
import org.spoofax.jsglr2.benchmark.BenchmarkParserObserver;
import org.spoofax.jsglr2.characterclasses.CharacterClassFactory;
import org.spoofax.jsglr2.characterclasses.ICharacterClassFactory;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForest;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.parsetable.IParseTable;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.parsetable.ParseTableReader;
import org.spoofax.jsglr2.stack.basic.BasicStackNode;
import org.spoofax.jsglr2.states.ActionsForCharacterRepresentation;
import org.spoofax.jsglr2.states.IState;
import org.spoofax.jsglr2.states.IStateFactory;
import org.spoofax.jsglr2.states.StateFactory;
import org.spoofax.jsglr2.testset.Input;
import org.spoofax.jsglr2.testset.TestSet;

public abstract class JSGLR2StateApplicableActionsBenchmark extends JSGLR2DataStructureBenchmark {

    ActorObserver actorObserver;

    protected JSGLR2StateApplicableActionsBenchmark(TestSet testSet) {
        super(testSet);
    }

    @Param({ "false", "true" }) public boolean optimizeCharacterClasses;

    @Param({ "false", "true" }) public boolean cacheCharacterClasses;

    @Param({ "false", "true" }) public boolean cacheActions;

    @Param ActionsForCharacterRepresentation actionsPerCharacterClassRepresentation;

    @Override
    public void postParserSetup() {
        actorObserver = new ActorObserver();

        parser.observing().attachObserver(actorObserver);

        try {
            for(Input input : inputs)
                parser.parseUnsafe(input.content, input.filename, null);
        } catch(ParseException e) {
            throw new IllegalStateException("setup of benchmark should not fail");
        }
    }

    @Override
    protected IParseTable readParseTable(IStrategoTerm parseTableTerm) throws ParseTableReadException {
        ICharacterClassFactory characterClassFactory =
            new CharacterClassFactory(optimizeCharacterClasses, cacheCharacterClasses);
        IActionsFactory actionsFactory = new ActionsFactory(cacheActions);
        IStateFactory stateFactory = new StateFactory(actionsPerCharacterClassRepresentation,
            StateFactory.defaultProductionToGotoRepresentation);

        return new ParseTableReader(characterClassFactory, actionsFactory, stateFactory).read(parseTableTerm);
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

    class ActorObserver extends BenchmarkParserObserver<BasicParseForest, BasicStackNode<BasicParseForest>> {

        public List<ActorOnState> stateApplicableActions = new ArrayList<ActorOnState>();

        @Override
        public void actor(BasicStackNode<BasicParseForest> stack,
            Parse<BasicParseForest, BasicStackNode<BasicParseForest>> parse, Iterable<IAction> applicableActions) {
            ActorOnState stateApplicableActionsForActor = new ActorOnState(stack.state, parse.currentChar);

            stateApplicableActions.add(stateApplicableActionsForActor);
        }

    }

    @Benchmark
    public void benchmark(Blackhole bh) throws ParseException {
        for(ActorOnState stateApplicableActions : actorObserver.stateApplicableActions)
            stateApplicableActions.iterateOverApplicableActions(bh);
    }

}
