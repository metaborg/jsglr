package org.spoofax.jsglr2.benchmark.jsglr2.datastructures;

import java.util.ArrayList;
import java.util.List;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.infra.Blackhole;
import org.spoofax.jsglr2.benchmark.BenchmarkParserObserver;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForest;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.basic.BasicStackNode;
import org.spoofax.jsglr2.stack.collections.ForActorStacksArrayDeque;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;
import org.spoofax.jsglr2.testset.TestSet;

public abstract class JSGLR2ForActorStacksBenchmark extends JSGLR2DataStructureBenchmark {

    ForActorStacksObserver forActorStacksObserver;

    protected JSGLR2ForActorStacksBenchmark(TestSet testSet) {
        super(testSet);
    }

    public enum Representation {
        DequeuePriority
    }

    @Param public Representation representation;

    IForActorStacks<BasicStackNode<BasicParseForest>> forActorStacks;

    @Override public void postParserSetup() {
        forActorStacksObserver = new ForActorStacksObserver();

        parser.observing().attachObserver(forActorStacksObserver);

        switch(representation) {
            case DequeuePriority:
                forActorStacks = new ForActorStacksArrayDeque<>(new ParserObserving<>());

                break;
            default:
                break;
        }
    }

    private interface ForActorStacksOperation {
        void execute(Blackhole bh);
    }

    class ForActorStacksObserver extends
        BenchmarkParserObserver<BasicParseForest, BasicStackNode<BasicParseForest>, AbstractParseState<BasicParseForest, BasicStackNode<BasicParseForest>>> {

        public List<ForActorStacksOperation> operations = new ArrayList<>();

        @Override public void parseCharacter(
            Parse<BasicParseForest, BasicStackNode<BasicParseForest>, AbstractParseState<BasicParseForest, BasicStackNode<BasicParseForest>>> parse,
            Iterable<BasicStackNode<BasicParseForest>> activeStackNodes) {
            List<BasicStackNode<BasicParseForest>> activeStacksCopy = activeStacksCopy(parse);

            operations.add(bh -> {
                for(BasicStackNode<BasicParseForest> activeStack : activeStacksCopy)
                    forActorStacks.add(activeStack);
            });
        }

        @Override public void handleForActorStack(BasicStackNode<BasicParseForest> stack,
            IForActorStacks<BasicStackNode<BasicParseForest>> forActorStacks_) {
            operations.add(bh -> {
                bh.consume(forActorStacks.nonEmpty()); // The condition in the while loop in Parser::parseCharacter
                bh.consume(forActorStacks.remove());
            });
        }

        @Override public void addForActorStack(BasicStackNode<BasicParseForest> stack) {
            operations.add(bh -> forActorStacks.add(stack));
        }

        @Override public void directLinkFound(
            Parse<BasicParseForest, BasicStackNode<BasicParseForest>, AbstractParseState<BasicParseForest, BasicStackNode<BasicParseForest>>> parse,
            StackLink<BasicParseForest, BasicStackNode<BasicParseForest>> directLink) {
            if(directLink == null) {
                // Only if no existing direct link is found during a reduction, a new link is created and some active
                // stacks (those that are not reject and not in for actor) need to be revisited

                List<BasicStackNode<BasicParseForest>> activeStacksCopy = activeStacksCopy(parse);

                operations.add(bh -> {
                    for(BasicStackNode<BasicParseForest> activeStack : activeStacksCopy)
                        bh.consume(!activeStack.allLinksRejected() && !forActorStacks.contains(activeStack));
                });
            }
        }

        private List<BasicStackNode<BasicParseForest>> activeStacksCopy(
            Parse<BasicParseForest, BasicStackNode<BasicParseForest>, AbstractParseState<BasicParseForest, BasicStackNode<BasicParseForest>>> parse) {
            List<BasicStackNode<BasicParseForest>> activeStacksCopy = new ArrayList<>();

            for(BasicStackNode<BasicParseForest> activeStack : parse.state.activeStacks)
                activeStacksCopy.add(activeStack);

            return activeStacksCopy;
        }

    }

    @Benchmark public void benchmark(Blackhole bh) {

        for(ForActorStacksOperation forActorStacksOperation : forActorStacksObserver.operations) {
            forActorStacksOperation.execute(bh);
        }

    }

}
