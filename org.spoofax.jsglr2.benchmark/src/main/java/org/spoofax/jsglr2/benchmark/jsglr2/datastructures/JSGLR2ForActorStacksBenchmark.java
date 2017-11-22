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
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.ForActorStacks;
import org.spoofax.jsglr2.parser.IForActorStacks;
import org.spoofax.jsglr2.parser.IParser;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.parsetable.IParseTable;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.parsetable.ParseTableReader;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.testset.Input;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.terms.ParseError;

public abstract class JSGLR2ForActorStacksBenchmark extends BaseBenchmark {

    IParser<?, ?> parser;
    ForActorStacksObserver forActorStacksObserver;

    protected JSGLR2ForActorStacksBenchmark(TestSet testSet) {
        super(testSet);
    }

    public enum Representation {
        DequeuePriority
    }

    @Param({ "DequeuePriority" }) public Representation representation;

    IForActorStacks forActorStacks;

    @Setup public void parserSetup() throws ParseError, ParseTableReadException, IOException,
        InvalidParseTableException, InterruptedException, URISyntaxException {
        IParseTable parseTable = new ParseTableReader().read(testSetReader.getParseTableTerm());

        parser = JSGLR2Variants.getParser(parseTable, ParseForestRepresentation.Basic, ParseForestConstruction.Full,
            StackRepresentation.Basic, Reducing.Basic);

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
                forActorStacks = new ForActorStacks(Parse.EMTPY);

                break;
            default:
                break;
        }
    }

    private interface ForActorStacksOperation {
        void execute(Blackhole bh);
    }

    class ForActorStacksObserver<StackNode extends AbstractStackNode<ParseForest>, ParseForest extends AbstractParseForest>
        extends BenchmarkParserObserver<StackNode, ParseForest> {

        public List<ForActorStacksOperation> operations = new ArrayList<>();

        @Override public void parseCharacter(Parse<StackNode, ParseForest> parse,
            Iterable<StackNode> activeStackNodes) {
            List<StackNode> activeStacksCopy = activeStacksCopy(parse);

            operations.add(bh -> {
                for(StackNode activeStack : activeStacksCopy)
                    forActorStacks.add(activeStack);
            });
        }

        @Override public void handleForActorStack(StackNode stack, IForActorStacks<StackNode> forActorStacks_) {
            operations.add(bh -> {
                bh.consume(forActorStacks.nonEmpty()); // The condition in the while loop in Parser::parseCharacter
                bh.consume(forActorStacks.remove());
            });
        }

        @Override public void addForActorStack(StackNode stack) {
            operations.add(bh -> forActorStacks.add(stack));
        }

        @Override public void directLinkFound(Parse<StackNode, ParseForest> parse,
            StackLink<StackNode, ParseForest> directLink) {
            if(directLink == null) {
                // Only if no existing direct link is found during a reduction, a new link is created and some active
                // stacks (those that are not reject and not in for actor) need to be revisited

                List<StackNode> activeStacksCopy = activeStacksCopy(parse);

                operations.add(bh -> {
                    for(StackNode activeStack : activeStacksCopy)
                        bh.consume(!activeStack.allOutLinksRejected() && !forActorStacks.contains(activeStack));
                });
            }
        }

        private List<StackNode> activeStacksCopy(Parse<StackNode, ParseForest> parse) {
            List<StackNode> activeStacksCopy = new ArrayList<StackNode>();

            for(StackNode activeStack : parse.activeStacks)
                activeStacksCopy.add(activeStack);

            return activeStacksCopy;
        }

    }

    @Benchmark public void benchmark(Blackhole bh) throws ParseException {

        for(ForActorStacksOperation forActorStacksOperation : ((ForActorStacksObserver<?, ?>) forActorStacksObserver).operations) {
            forActorStacksOperation.execute(bh);
        }

    }

}
