package org.spoofax.jsglr2.benchmark.jsglr2.datastructures;

import java.util.ArrayList;
import java.util.List;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.ParseTableReadException;
import org.metaborg.parsetable.ParseTableReader;
import org.metaborg.parsetable.actions.ActionsFactory;
import org.metaborg.parsetable.actions.IAction;
import org.metaborg.parsetable.actions.IActionsFactory;
import org.metaborg.parsetable.characterclasses.CharacterClassFactory;
import org.metaborg.parsetable.characterclasses.ICharacterClassFactory;
import org.metaborg.parsetable.query.ActionsForCharacterRepresentation;
import org.metaborg.parsetable.query.IActionQuery;
import org.metaborg.parsetable.query.ParsingMode;
import org.metaborg.parsetable.query.ProductionToGotoRepresentation;
import org.metaborg.parsetable.states.IState;
import org.metaborg.parsetable.states.IStateFactory;
import org.metaborg.parsetable.states.StateFactory;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.infra.Blackhole;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.benchmark.BenchmarkParserObserver;
import org.spoofax.jsglr2.inputstack.IInputStack;
import org.spoofax.jsglr2.parseforest.basic.IBasicDerivation;
import org.spoofax.jsglr2.parseforest.basic.IBasicParseForest;
import org.spoofax.jsglr2.parseforest.basic.IBasicParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.stack.basic.BasicStackNode;

public abstract class JSGLR2StateApplicableActionsBenchmark extends JSGLR2DataStructureBenchmark {

    ActorObserver actorObserver;

    @Param({ "false", "true" }) public boolean optimizeCharacterClasses;

    @Param({ "false", "true" }) public boolean cacheCharacterClasses;

    @Param({ "false", "true" }) public boolean cacheActions;

    @Param ActionsForCharacterRepresentation actionsPerCharacterClassRepresentation;

    @Override public void postParserSetup() {
        actorObserver = new ActorObserver();

        parser.observing().attachObserver(actorObserver);
    }

    @Override protected IParseTable readParseTable(IStrategoTerm parseTableTerm) throws ParseTableReadException {
        ICharacterClassFactory characterClassFactory =
            new CharacterClassFactory(optimizeCharacterClasses, cacheCharacterClasses);
        IActionsFactory actionsFactory = new ActionsFactory(cacheActions);
        IStateFactory stateFactory =
            new StateFactory(actionsPerCharacterClassRepresentation, ProductionToGotoRepresentation.standard());

        return new ParseTableReader(characterClassFactory, actionsFactory, stateFactory).read(parseTableTerm);
    }

    class ActorOnState {

        final IState state;
        final IActionQuery actionQuery;

        public ActorOnState(IState state, int character) {
            this.state = state;
            this.actionQuery = new IActionQuery() {
                @Override public int actionQueryCharacter() {
                    return character;
                }

                @Override public int[] actionQueryLookahead(int length) {
                    return new int[0];
                }
            };
        }

        public void iterateOverApplicableActions(Blackhole bh) {
            for(IAction action : state.getApplicableActions(actionQuery, ParsingMode.Standard))
                bh.consume(action);
        }

    }

    class ActorObserver extends
        BenchmarkParserObserver<IBasicParseForest, IBasicDerivation<IBasicParseForest>, IBasicParseNode<IBasicParseForest, IBasicDerivation<IBasicParseForest>>, BasicStackNode<IBasicParseForest>, AbstractParseState<IInputStack, BasicStackNode<IBasicParseForest>>> {

        public List<ActorOnState> stateApplicableActions = new ArrayList<>();

        @Override public void actor(BasicStackNode<IBasicParseForest> stack,
            AbstractParseState<IInputStack, BasicStackNode<IBasicParseForest>> parseState,
            Iterable<IAction> applicableActions) {
            ActorOnState stateApplicableActionsForActor =
                new ActorOnState(stack.state, parseState.inputStack.getChar());

            stateApplicableActions.add(stateApplicableActionsForActor);
        }

    }

    @Benchmark public void benchmark(Blackhole bh) {
        for(ActorOnState stateApplicableActions : actorObserver.stateApplicableActions)
            stateApplicableActions.iterateOverApplicableActions(bh);
    }

}
