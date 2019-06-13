package org.spoofax.jsglr2.benchmark.jsglr2.datastructures;

import java.util.*;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.infra.Blackhole;
import org.spoofax.jsglr2.benchmark.BenchmarkParserObserver;
import org.spoofax.jsglr2.inputstack.IInputStack;
import org.spoofax.jsglr2.parseforest.basic.IBasicDerivation;
import org.spoofax.jsglr2.parseforest.basic.IBasicParseForest;
import org.spoofax.jsglr2.parseforest.basic.IBasicParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.ForShifterElement;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.basic.BasicStackNode;
import org.spoofax.jsglr2.testset.StringInput;
import org.spoofax.jsglr2.testset.TestSet;

public abstract class JSGLR2ForShifterBenchmark extends JSGLR2DataStructureBenchmark {

    ForShifterObserver forShifterObserver;

    protected JSGLR2ForShifterBenchmark(TestSet<StringInput> testSet) {
        super(testSet);
    }

    public enum Representation {
        ArrayDequeue, ArrayList, LinkedList, Set
    }

    @Param public Representation representation;

    Collection<ForShifterElement<?>> forShifter;

    @Override public void postParserSetup() {
        forShifterObserver = new ForShifterObserver();

        parser.observing().attachObserver(forShifterObserver);

        switch(representation) {
            case ArrayDequeue:
                forShifter = new ArrayDeque<>();

                break;
            case ArrayList:
                forShifter = new ArrayList<>();

                break;
            case LinkedList:
                forShifter = new LinkedList<>();

                break;
            case Set:
                forShifter = new HashSet<>();

                break;
            default:
                break;
        }
    }

    class ParseRound {
        final List<ForShifterElement<?>> forShifterElements = new ArrayList<>();
    }

    class ForShifterObserver extends
        BenchmarkParserObserver<IBasicParseForest, IBasicDerivation<IBasicParseForest>, IBasicParseNode<IBasicParseForest, IBasicDerivation<IBasicParseForest>>, BasicStackNode<IBasicParseForest>, AbstractParseState<IInputStack, BasicStackNode<IBasicParseForest>>> {

        public List<ParseRound> parseRounds = new ArrayList<>();

        @Override public void parseRound(AbstractParseState<IInputStack, BasicStackNode<IBasicParseForest>> parseState,
            Iterable<BasicStackNode<IBasicParseForest>> activeStacks,
            ParserObserving<IBasicParseForest, IBasicDerivation<IBasicParseForest>, IBasicParseNode<IBasicParseForest, IBasicDerivation<IBasicParseForest>>, BasicStackNode<IBasicParseForest>, AbstractParseState<IInputStack, BasicStackNode<IBasicParseForest>>> observing) {
            parseRounds.add(new ParseRound());
        }

        private ParseRound currentParseRound() {
            return parseRounds.get(parseRounds.size() - 1);
        }

        @Override public void addForShifter(ForShifterElement<BasicStackNode<IBasicParseForest>> forShifterElement) {
            currentParseRound().forShifterElements.add(forShifterElement);
        }

    }

    @Benchmark public void benchmark(Blackhole bh) {
        for(ParseRound parseRound : forShifterObserver.parseRounds) {
            forShifter.clear();

            for(ForShifterElement<?> forShifterElement : parseRound.forShifterElements)
                forShifter.add(forShifterElement);

            for(ForShifterElement<?> forShifterElement : forShifter)
                bh.consume(forShifterElement);
        }
    }

}
