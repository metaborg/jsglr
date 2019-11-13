package org.spoofax.jsglr2.benchmark.jsglr2.datastructures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import org.metaborg.parsetable.states.IState;
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
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.basic.BasicStackNode;
import org.spoofax.jsglr2.stack.collections.ActiveStacksArrayList;
import org.spoofax.jsglr2.stack.collections.IActiveStacks;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.jsglr2.testset.testinput.StringInput;

public abstract class JSGLR2ActiveStacksBenchmark extends JSGLR2DataStructureBenchmark {

    ActiveStacksObserver activeStacksObserver;

    protected JSGLR2ActiveStacksBenchmark(TestSet<String, StringInput> testSet) {
        super(testSet);
    }

    public enum Representation {
        ArrayList
    }

    @Param public Representation representation;

    IActiveStacks<BasicStackNode<IBasicParseForest>> activeStacks;

    @Override public void postParserSetup() {
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

    private IForActorStacks<BasicStackNode<IBasicParseForest>> emptyForActorStacks =
        new IForActorStacks<BasicStackNode<IBasicParseForest>>() {

            @Override public void add(BasicStackNode<IBasicParseForest> stack) {
            }

            @Override public boolean contains(BasicStackNode<IBasicParseForest> stack) {
                return false;
            }

            @Override public boolean nonEmpty() {
                return false;
            }

            @Override public BasicStackNode<IBasicParseForest> remove() {
                return null;
            }

            @Override public Iterator<BasicStackNode<IBasicParseForest>> iterator() {
                return null;
            }

        };

    private interface ActiveStacksOperation {
        void execute(Blackhole bh);
    }

    class ActiveStacksObserver extends
        BenchmarkParserObserver<IBasicParseForest, IBasicDerivation<IBasicParseForest>, IBasicParseNode<IBasicParseForest, IBasicDerivation<IBasicParseForest>>, BasicStackNode<IBasicParseForest>, AbstractParseState<IInputStack, BasicStackNode<IBasicParseForest>>> {

        public List<ActiveStacksOperation> operations = new ArrayList<>();

        @Override public void parseRound(AbstractParseState<IInputStack, BasicStackNode<IBasicParseForest>> parseState,
            Iterable<BasicStackNode<IBasicParseForest>> activeStackNodes) {
            operations.add(bh -> activeStacks.addAllTo(emptyForActorStacks));
        }

        @Override public void addActiveStack(BasicStackNode<IBasicParseForest> stack) {
            operations.add(bh -> activeStacks.add(stack));
        }

        @Override public void directLinkFound(
            AbstractParseState<IInputStack, BasicStackNode<IBasicParseForest>> parseState,
            StackLink<IBasicParseForest, BasicStackNode<IBasicParseForest>> directLink) {
            if(directLink == null) {
                // Only if no existing direct link is found during a reduction, a new link is created and some active
                // stacks (those that are not reject and not in for actor) need to be revisited
                operations.add(bh -> {
                    for(Object activeStack : activeStacks.forLimitedReductions(emptyForActorStacks))
                        bh.consume(activeStack);
                });
            }
        }

        @Override public void findActiveStackWithState(IState state) {
            operations.add(bh -> bh.consume(activeStacks.findWithState(state)));
        }

        @Override public void shifter(IBasicParseForest termNode,
            Queue<ForShifterElement<BasicStackNode<IBasicParseForest>>> forShifter) {
            operations.add(bh -> activeStacks.clear());
        }

        /*
         * IActiveStacks::isEmpty and IActiveStacks::isSingle calls are not included in the benchmark
         */

    }

    @Benchmark public void benchmark(Blackhole bh) {

        for(ActiveStacksOperation activeStacksOperation : activeStacksObserver.operations) {
            activeStacksOperation.execute(bh);
        }

    }

}
