package org.spoofax.jsglr2.benchmark.jsglr2;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

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
import org.spoofax.jsglr2.benchmark.BaseBenchmark;
import org.spoofax.jsglr2.benchmark.BenchmarkParserObserver;
import org.spoofax.jsglr2.characters.ICharacterClassFactory;
import org.spoofax.jsglr2.characters.IntegerRangeSetCharacterClassFactory;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.IParser;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.parsetable.IParseTable;
import org.spoofax.jsglr2.parsetable.IState;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.parsetable.ParseTableReader;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.testset.Input;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.terms.ParseError;

public abstract class JSGLR2StateApplicableActionsBenchmark extends BaseBenchmark {

    IParser<?, ?> parser;
    ActorObserver actorObserver;

    protected JSGLR2StateApplicableActionsBenchmark(TestSet testSet) {
        super(testSet);
    }

    public enum ActionsIteration {
        List, FilterIterable, ForLoop, Lambda
    }

    @Param({ "false", "true" }) public boolean optimizedCharacterClasses;

    @Param public ActionsIteration actionsIteration;

    @Setup public void parserSetup() throws ParseError, ParseTableReadException, IOException,
        InvalidParseTableException, InterruptedException, URISyntaxException {
        ICharacterClassFactory characterClassFactory =
            new IntegerRangeSetCharacterClassFactory(optimizedCharacterClasses);
        IParseTable parseTable = new ParseTableReader(characterClassFactory).read(testSetReader.getParseTableTerm());

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

    abstract class ActorOnStateActions {

        final IState state;
        final int character;

        protected ActorOnStateActions(IState state, int character) {
            this.state = state;
            this.character = character;
        }

        abstract public void iterateOverActions(Blackhole bh);

    }

    class ActorOnStateActionsList extends ActorOnStateActions {

        public ActorOnStateActionsList(IState state, int character) {
            super(state, character);
        }

        private Iterable<IAction> list() {
            List<IAction> res = new ArrayList<IAction>();

            for(IAction action : state.actions()) {
                if(action.appliesTo(character))
                    res.add(action);
            }

            return res;
        }

        @Override public void iterateOverActions(Blackhole bh) {
            for(IAction action : list())
                bh.consume(action);
        }

    }

    class ActorOnStateActionsFilterIterable extends ActorOnStateActions {

        public ActorOnStateActionsFilterIterable(IState state, int character) {
            super(state, character);
        }

        public Iterable<IAction> iterable() {
            return () -> {
                return new Iterator<IAction>() {
                    int index = 0;

                    @Override public boolean hasNext() {
                        while(index < state.actions().length && !state.actions()[index].appliesTo(character)) {
                            index++;
                        }
                        return index < state.actions().length;
                    }

                    @Override public IAction next() {
                        if(!hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return state.actions()[index++];
                    }
                };
            };
        }

        @Override public void iterateOverActions(Blackhole bh) {
            for(IAction action : iterable())
                bh.consume(action);
        }

    }

    class ActorOnStateActionsForLoop extends ActorOnStateActions {

        public ActorOnStateActionsForLoop(IState state, int character) {
            super(state, character);
        }

        @Override public void iterateOverActions(Blackhole bh) {
            for(IAction action : state.actions()) {
                if(action.appliesTo(character))
                    bh.consume(action);
            }
        }

    }

    class StateApplicableActionsLambda extends ActorOnStateActions {

        public StateApplicableActionsLambda(IState state, int character) {
            super(state, character);
        }

        private void forEachAction(Consumer<IAction> consumer) {
            for(IAction action : state.actions()) {
                if(action.appliesTo(character))
                    consumer.accept(action);
            }
        }

        @Override public void iterateOverActions(Blackhole bh) {
            forEachAction(characterClass -> bh.consume(characterClass));
        }

    }

    class ActorObserver<StackNode extends AbstractStackNode<ParseForest>, ParseForest extends AbstractParseForest>
        extends BenchmarkParserObserver<StackNode, ParseForest> {

        public List<ActorOnStateActions> stateApplicableActions = new ArrayList<ActorOnStateActions>();

        @Override public void actor(StackNode stack, Parse<StackNode, ParseForest> parse,
            Iterable<IAction> applicableActions) {
            ActorOnStateActions stateApplicableActionsForActor;

            switch(actionsIteration) {
                case List:
                    stateApplicableActionsForActor = new ActorOnStateActionsList(stack.state, parse.currentChar);
                    break;
                case FilterIterable:
                    stateApplicableActionsForActor =
                        new ActorOnStateActionsFilterIterable(stack.state, parse.currentChar);
                    break;
                case ForLoop:
                    stateApplicableActionsForActor = new ActorOnStateActionsForLoop(stack.state, parse.currentChar);
                    break;
                case Lambda:
                    stateApplicableActionsForActor = new StateApplicableActionsLambda(stack.state, parse.currentChar);
                    break;
                default:
                    stateApplicableActionsForActor = null;
                    break;
            }

            stateApplicableActions.add(stateApplicableActionsForActor);
        }

    }

    @Benchmark public void benchmark(Blackhole bh) throws ParseException {
        for(ActorOnStateActions stateApplicableActions : ((ActorObserver<?, ?>) actorObserver).stateApplicableActions)
            stateApplicableActions.iterateOverActions(bh);
    }

}
