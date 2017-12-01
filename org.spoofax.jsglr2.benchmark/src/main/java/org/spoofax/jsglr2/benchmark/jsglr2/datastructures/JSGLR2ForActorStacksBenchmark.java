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
import org.spoofax.jsglr2.benchmark.BaseBenchmark;
import org.spoofax.jsglr2.benchmark.BenchmarkParserObserver;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForest;
import org.spoofax.jsglr2.parser.ForActorStacks;
import org.spoofax.jsglr2.parser.IForActorStacks;
import org.spoofax.jsglr2.parser.IParser;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.parsetable.IParseTable;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.parsetable.ParseTableReader;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.basic.BasicStackNode;
import org.spoofax.jsglr2.testset.Input;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.terms.ParseError;

public abstract class JSGLR2ForActorStacksBenchmark extends BaseBenchmark {

    IParser<BasicParseForest, BasicStackNode<BasicParseForest>> parser;
    ForActorStacksObserver forActorStacksObserver;

    protected JSGLR2ForActorStacksBenchmark(TestSet testSet) {
        super(testSet);
    }

    public enum Representation {
        DequeuePriority
    }

    @Param({ "DequeuePriority" }) public Representation representation;

    IForActorStacks<BasicStackNode<BasicParseForest>> forActorStacks;

    @SuppressWarnings("unchecked")
    @Setup
    public void parserSetup() throws ParseError, ParseTableReadException, IOException, InvalidParseTableException,
        InterruptedException, URISyntaxException {
        IParseTable parseTable = new ParseTableReader().read(testSetReader.getParseTableTerm());

        parser = (IParser<BasicParseForest, BasicStackNode<BasicParseForest>>) JSGLR2Variants.getParser(parseTable,
            ParseForestRepresentation.Basic, ParseForestConstruction.Full, StackRepresentation.Basic, Reducing.Basic);

        forActorStacksObserver = new ForActorStacksObserver();

        parser.attachObserver(forActorStacksObserver);

        try {
            for(Input input : inputs)
                parser.parseUnsafe(input.content, input.filename, null);
        } catch(ParseException e) {
            throw new IllegalStateException("setup of benchmark should not fail");
        }

        switch(representation) {
            case DequeuePriority:
                forActorStacks = new ForActorStacks<>(Parse.empty());

                break;
            default:
                break;
        }
    }

    private interface ForActorStacksOperation {
        void execute(Blackhole bh);
    }

    class ForActorStacksObserver extends BenchmarkParserObserver<BasicParseForest, BasicStackNode<BasicParseForest>> {

        public List<ForActorStacksOperation> operations = new ArrayList<>();

        @Override
        public void parseCharacter(Parse<BasicParseForest, BasicStackNode<BasicParseForest>> parse,
            Iterable<BasicStackNode<BasicParseForest>> activeStackNodes) {
            List<BasicStackNode<BasicParseForest>> activeStacksCopy = activeStacksCopy(parse);

            operations.add(bh -> {
                for(BasicStackNode<BasicParseForest> activeStack : activeStacksCopy)
                    forActorStacks.add(activeStack);
            });
        }

        @Override
        public void handleForActorStack(BasicStackNode<BasicParseForest> stack,
            IForActorStacks<BasicStackNode<BasicParseForest>> forActorStacks_) {
            operations.add(bh -> {
                bh.consume(forActorStacks.nonEmpty()); // The condition in the while loop in Parser::parseCharacter
                bh.consume(forActorStacks.remove());
            });
        }

        @Override
        public void addForActorStack(BasicStackNode<BasicParseForest> stack) {
            operations.add(bh -> forActorStacks.add(stack));
        }

        @Override
        public void directLinkFound(Parse<BasicParseForest, BasicStackNode<BasicParseForest>> parse,
            StackLink<BasicParseForest, BasicStackNode<BasicParseForest>> directLink) {
            if(directLink == null) {
                // Only if no existing direct link is found during a reduction, a new link is created and some active
                // stacks (those that are not reject and not in for actor) need to be revisited

                List<BasicStackNode<BasicParseForest>> activeStacksCopy = activeStacksCopy(parse);

                operations.add(bh -> {
                    for(BasicStackNode<BasicParseForest> activeStack : activeStacksCopy)
                        bh.consume(!activeStack.allOutLinksRejected() && !forActorStacks.contains(activeStack));
                });
            }
        }

        private List<BasicStackNode<BasicParseForest>>
            activeStacksCopy(Parse<BasicParseForest, BasicStackNode<BasicParseForest>> parse) {
            List<BasicStackNode<BasicParseForest>> activeStacksCopy = new ArrayList<BasicStackNode<BasicParseForest>>();

            for(BasicStackNode<BasicParseForest> activeStack : parse.activeStacks)
                activeStacksCopy.add(activeStack);

            return activeStacksCopy;
        }

    }

    @Benchmark
    public void benchmark(Blackhole bh) throws ParseException {

        for(ForActorStacksOperation forActorStacksOperation : forActorStacksObserver.operations) {
            forActorStacksOperation.execute(bh);
        }

    }

}
