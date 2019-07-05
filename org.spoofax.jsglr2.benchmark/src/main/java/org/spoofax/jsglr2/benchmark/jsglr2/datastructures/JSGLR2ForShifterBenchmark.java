package org.spoofax.jsglr2.benchmark.jsglr2.datastructures;

import java.util.*;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.infra.Blackhole;
import org.spoofax.jsglr2.benchmark.BenchmarkParserObserver;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.ForShifterElement;
import org.spoofax.jsglr2.stack.basic.BasicStackNode;
import org.spoofax.jsglr2.testset.TestSet;

public abstract class JSGLR2ForShifterBenchmark extends JSGLR2DataStructureBenchmark {

    ForShifterObserver forShifterObserver;

    protected JSGLR2ForShifterBenchmark(TestSet testSet) {
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

    class ForShifterObserver extends BenchmarkParserObserver<BasicParseForest, BasicStackNode<BasicParseForest>> {

        public List<ParseRound> parseRounds = new ArrayList<>();

        @Override public void parseCharacter(AbstractParse<BasicParseForest, BasicStackNode<BasicParseForest>> parse,
            Iterable<BasicStackNode<BasicParseForest>> activeStacks) {
            parseRounds.add(new ParseRound());
        }

        private ParseRound currentParseRound() {
            return parseRounds.get(parseRounds.size() - 1);
        }

        @Override public void addForShifter(ForShifterElement<BasicStackNode<BasicParseForest>> forShifterElement) {
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
