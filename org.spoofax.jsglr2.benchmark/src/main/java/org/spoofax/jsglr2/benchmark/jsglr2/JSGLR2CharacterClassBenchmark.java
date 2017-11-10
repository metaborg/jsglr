package org.spoofax.jsglr2.benchmark.jsglr2;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
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
import org.spoofax.jsglr2.benchmark.BaseBenchmark;
import org.spoofax.jsglr2.benchmark.BenchmarkParserObserver;
import org.spoofax.jsglr2.characters.ICharacters;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.IParser;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.parsetable.IParseTable;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.parsetable.ParseTableReader;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.testset.Input;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.terms.ParseError;

public abstract class JSGLR2CharacterClassBenchmark extends BaseBenchmark {

    IParser<?, ?> parser;
    ActorObserver actorObserver;

    protected JSGLR2CharacterClassBenchmark(TestSet testSet) {
        super(testSet);
    }

    public enum CharacterClassRepresentation {
        Bitset
    }

    public enum ApplicableActionsRepresentation {
        List, Iterable, ForLoop
    }

    @Param public CharacterClassRepresentation characterClassRepresentation;

    @Param public ApplicableActionsRepresentation applicableActionsRepresentation;

    @Setup public void parserSetup() throws ParseError, ParseTableReadException, IOException,
        InvalidParseTableException, InterruptedException, URISyntaxException {
        IParseTable parseTable = ParseTableReader.read(testSetReader.getParseTableTerm());

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

    abstract class StateApplicableActions {

        final ICharacters[] characterClasses; // Represent the character classes of the actions in the state
        final byte character;

        protected StateApplicableActions(ICharacters[] characterClasses, byte character) {
            this.characterClasses = characterClasses;
            this.character = character;
        }

        abstract public void execute(Blackhole bh);

    }

    class StateApplicableActionsList extends StateApplicableActions {

        public StateApplicableActionsList(ICharacters[] characterClasses, byte character) {
            super(characterClasses, character);
        }

        private Iterable<ICharacters> list() {
            List<ICharacters> res = new ArrayList<ICharacters>();

            for(ICharacters characterClass : characterClasses) {
                if(characterClass.containsCharacter(character))
                    res.add(characterClass);
            }

            return res;
        }

        @Override public void execute(Blackhole bh) {
            for(ICharacters characterClass : list())
                bh.consume(characterClass);
        }

    }

    class StateApplicableActionsIterable extends StateApplicableActions {

        public StateApplicableActionsIterable(ICharacters[] characterClasses, byte character) {
            super(characterClasses, character);
        }

        public Iterable<ICharacters> iterable() {
            return new Iterable<ICharacters>() {
                @Override public Iterator<ICharacters> iterator() {
                    return new Iterator<ICharacters>() {
                        int i = 0;

                        @Override public boolean hasNext() {
                            if(i < characterClasses.length) {
                                if(characterClasses[i].containsCharacter(character))
                                    return true;
                                else {
                                    i++;

                                    return hasNext();
                                }
                            }

                            return false;
                        }

                        @Override public ICharacters next() {
                            return characterClasses[i++];
                        }
                    };
                }
            };
        }

        @Override public void execute(Blackhole bh) {
            for(ICharacters characterClass : iterable())
                bh.consume(characterClass);
        }

    }

    class StateApplicableActionsForLoop extends StateApplicableActions {

        public StateApplicableActionsForLoop(ICharacters[] characterClasses, byte character) {
            super(characterClasses, character);
        }

        @Override public void execute(Blackhole bh) {
            for(ICharacters characterClass : characterClasses) {
                if(characterClass.containsCharacter(character))
                    bh.consume(characterClass);
            }
        }

    }

    class ActorObserver<StackNode extends AbstractStackNode<ParseForest>, ParseForest extends AbstractParseForest>
        extends BenchmarkParserObserver<StackNode, ParseForest> {

        public List<StateApplicableActions> stateApplicableActions = new ArrayList<StateApplicableActions>();

        @Override public void actor(StackNode stack, Parse<StackNode, ParseForest> parse,
            Iterable<IAction> applicableActions) {
            ICharacters[] characterClasses = new ICharacters[stack.state.actions().length];

            for(int i = 0; i < stack.state.actions().length; i++) {
                ICharacters characterClass = stack.state.actions()[i].characters();
                characterClasses[i] = characterClass;
            }

            StateApplicableActions stateApplicableActionsForActor;

            switch(applicableActionsRepresentation) {
                case List:
                    stateApplicableActionsForActor =
                        new StateApplicableActionsList(characterClasses, parse.currentChar);
                    break;
                case Iterable:
                    stateApplicableActionsForActor =
                        new StateApplicableActionsIterable(characterClasses, parse.currentChar);
                    break;
                case ForLoop:
                    stateApplicableActionsForActor =
                        new StateApplicableActionsForLoop(characterClasses, parse.currentChar);
                    break;
                default:
                    stateApplicableActionsForActor = null;
                    break;
            }

            stateApplicableActions.add(stateApplicableActionsForActor);
        }

    }

    @Benchmark public void benchmark(Blackhole bh) throws ParseException {
        for(StateApplicableActions stateApplicableActions : ((ActorObserver<?, ?>) actorObserver).stateApplicableActions)
            stateApplicableActions.execute(bh);
    }

}
