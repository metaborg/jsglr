package org.spoofax.jsglr2.benchmark.jsglr2.datastructures;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

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
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.ForShifterElement;
import org.spoofax.jsglr2.parser.IForActorStacks;
import org.spoofax.jsglr2.parser.IParser;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.parsetable.IParseTable;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.parsetable.ParseTableReader;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.ActiveStacks;
import org.spoofax.jsglr2.stack.IActiveStacks;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.states.IState;
import org.spoofax.jsglr2.testset.Input;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.terms.ParseError;

public abstract class JSGLR2ActiveStacksBenchmark extends BaseBenchmark {

    IParser<?, ?> parser;
    ActiveStacksObserver activeStacksObserver;

    protected JSGLR2ActiveStacksBenchmark(TestSet testSet) {
        super(testSet);
    }

    public enum Representation {
        ArrayList
    }

    @Param({ "ArrayList" }) public Representation representation;

    IActiveStacks activeStacks;

    @Setup
    public void parserSetup() throws ParseError, ParseTableReadException, IOException, InvalidParseTableException,
        InterruptedException, URISyntaxException {
        IParseTable parseTable = new ParseTableReader().read(testSetReader.getParseTableTerm());

        parser = JSGLR2Variants.getParser(parseTable, ParseForestRepresentation.Basic, ParseForestConstruction.Full,
            StackRepresentation.Basic, Reducing.Basic);

        activeStacksObserver = new ActiveStacksObserver();

        parser.attachObserver(activeStacksObserver);

        try {
            for(Input input : inputs)
                parser.parseUnsafe(input.content, input.filename, null);
        } catch(ParseException e) {
            throw new IllegalStateException("setup of benchmark should not fail");
        }

        switch(representation) {
            case ArrayList:
                activeStacks = new ActiveStacks(Parse.EMTPY);

                break;
            default:
                break;
        }
    }

    private IForActorStacks<?> emptyForActorStacks = new IForActorStacks() {

        @Override
        public void add(AbstractStackNode stack) {
        }

        @Override
        public boolean contains(AbstractStackNode stack) {
            return false;
        }

        @Override
        public boolean nonEmpty() {
            return false;
        }

        @Override
        public AbstractStackNode remove() {
            return null;
        }

    };

    private interface ActiveStacksOperation {
        void execute(Blackhole bh);
    }

    class ActiveStacksObserver<StackNode extends AbstractStackNode<ParseForest>, ParseForest extends AbstractParseForest>
        extends BenchmarkParserObserver<StackNode, ParseForest> {

        public List<ActiveStacksOperation> operations = new ArrayList<>();

        @Override
        public void parseCharacter(Parse<StackNode, ParseForest> parse, Iterable<StackNode> activeStackNodes) {
            operations.add(bh -> activeStacks.addAllTo(emptyForActorStacks));
        }

        @Override
        public void addActiveStack(StackNode stack) {
            operations.add(bh -> activeStacks.add(stack));
        }

        @Override
        public void directLinkFound(Parse<StackNode, ParseForest> parse, StackLink<StackNode, ParseForest> directLink) {
            if(directLink == null) {
                // Only if no existing direct link is found during a reduction, a new link is created and some active
                // stacks (those that are not reject and not in for actor) need to be revisited
                operations.add(bh -> {
                    for(Object activeStack : activeStacks.forLimitedReductions(emptyForActorStacks))
                        bh.consume(activeStack);
                });
            }
        }

        @Override
        public void findActiveStackWithState(IState state) {
            operations.add(bh -> bh.consume(activeStacks.findWithState(state)));
        }

        @Override
        public void shifter(ParseForest termNode, Queue<ForShifterElement<StackNode, ParseForest>> forShifter) {
            operations.add(bh -> activeStacks.clear());
        }

        /*
         * IActiveStacks::isEmpty and IActiveStacks::isSingle calls are not included in the benchmark
         */

    }

    @Benchmark
    public void benchmark(Blackhole bh) throws ParseException {

        for(ActiveStacksOperation activeStacksOperation : ((ActiveStacksObserver<?, ?>) activeStacksObserver).operations) {
            activeStacksOperation.execute(bh);
        }

    }

}
