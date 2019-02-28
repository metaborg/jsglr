package org.spoofax.jsglr2.benchmark.jsglr2.datastructures;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.infra.Blackhole;
import org.spoofax.jsglr2.benchmark.BenchmarkParserObserver;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.ForShifterElement;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.basic.BasicStackNode;
import org.spoofax.jsglr2.stack.collections.ActiveStacksArrayList;
import org.spoofax.jsglr2.stack.collections.IActiveStacks;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;
import org.spoofax.jsglr2.testset.TestSet;
import org.metaborg.parsetable.IState;

public abstract class JSGLR2ActiveStacksBenchmark extends JSGLR2DataStructureBenchmark {

    ActiveStacksObserver activeStacksObserver;

    protected JSGLR2ActiveStacksBenchmark(TestSet testSet) {
        super(testSet);
    }

    public enum Representation {
        ArrayList
    }

    @Param public Representation representation;

    IActiveStacks<BasicStackNode<BasicParseForest>> activeStacks;

    @Override
    public void postParserSetup() {
        activeStacksObserver = new ActiveStacksObserver();

        parser.observing().attachObserver(activeStacksObserver);

        switch(representation) {
            case ArrayList:
                activeStacks = new ActiveStacksArrayList<>(new ParserObserving<>());

                break;
            default:
                break;
        }
    }

    private IForActorStacks<BasicStackNode<BasicParseForest>> emptyForActorStacks =
        new IForActorStacks<BasicStackNode<BasicParseForest>>() {

            @Override
            public void add(BasicStackNode<BasicParseForest> stack) {
            }

            @Override
            public boolean contains(BasicStackNode<BasicParseForest> stack) {
                return false;
            }

            @Override
            public boolean nonEmpty() {
                return false;
            }

            @Override
            public BasicStackNode<BasicParseForest> remove() {
                return null;
            }

        };

    private interface ActiveStacksOperation {
        void execute(Blackhole bh);
    }

    class ActiveStacksObserver extends BenchmarkParserObserver<BasicParseForest, BasicStackNode<BasicParseForest>> {

        public List<ActiveStacksOperation> operations = new ArrayList<>();

        @Override
        public void parseCharacter(AbstractParse<BasicParseForest, BasicStackNode<BasicParseForest>> parse,
            Iterable<BasicStackNode<BasicParseForest>> activeStackNodes) {
            operations.add(bh -> activeStacks.addAllTo(emptyForActorStacks));
        }

        @Override
        public void addActiveStack(BasicStackNode<BasicParseForest> stack) {
            operations.add(bh -> activeStacks.add(stack));
        }

        @Override
        public void directLinkFound(AbstractParse<BasicParseForest, BasicStackNode<BasicParseForest>> parse,
            StackLink<BasicParseForest, BasicStackNode<BasicParseForest>> directLink) {
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
        public void shifter(BasicParseForest termNode,
            Queue<ForShifterElement<BasicParseForest, BasicStackNode<BasicParseForest>>> forShifter) {
            operations.add(bh -> activeStacks.clear());
        }

        /*
         * IActiveStacks::isEmpty and IActiveStacks::isSingle calls are not included in the benchmark
         */

    }

    @Benchmark
    public void benchmark(Blackhole bh) {

        for(ActiveStacksOperation activeStacksOperation : activeStacksObserver.operations) {
            activeStacksOperation.execute(bh);
        }

    }

}
