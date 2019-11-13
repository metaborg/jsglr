package org.spoofax.jsglr2.benchmark.jsglr2.datastructures;

import java.util.ArrayList;
import java.util.List;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.infra.Blackhole;
import org.spoofax.jsglr2.benchmark.BenchmarkParserObserver;
import org.spoofax.jsglr2.inputstack.IInputStack;
import org.spoofax.jsglr2.parseforest.basic.IBasicDerivation;
import org.spoofax.jsglr2.parseforest.basic.IBasicParseForest;
import org.spoofax.jsglr2.parseforest.basic.IBasicParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.basic.BasicStackNode;
import org.spoofax.jsglr2.stack.collections.ForActorStacksArrayDeque;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.jsglr2.testset.testinput.StringInput;

public abstract class JSGLR2ForActorStacksBenchmark extends JSGLR2DataStructureBenchmark {

    ForActorStacksObserver forActorStacksObserver;

    public enum Representation {
        DequeuePriority
    }

    @Param public Representation representation;

    IForActorStacks<BasicStackNode<IBasicParseForest>> forActorStacks;

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
        BenchmarkParserObserver<IBasicParseForest, IBasicDerivation<IBasicParseForest>, IBasicParseNode<IBasicParseForest, IBasicDerivation<IBasicParseForest>>, BasicStackNode<IBasicParseForest>, AbstractParseState<IInputStack, BasicStackNode<IBasicParseForest>>> {

        public List<ForActorStacksOperation> operations = new ArrayList<>();

        @Override public void parseRound(AbstractParseState<IInputStack, BasicStackNode<IBasicParseForest>> parseState,
            Iterable<BasicStackNode<IBasicParseForest>> activeStackNodes) {
            List<BasicStackNode<IBasicParseForest>> activeStacksCopy = activeStacksCopy(parseState);

            operations.add(bh -> {
                for(BasicStackNode<IBasicParseForest> activeStack : activeStacksCopy)
                    forActorStacks.add(activeStack);
            });
        }

        @Override public void handleForActorStack(BasicStackNode<IBasicParseForest> stack,
            IForActorStacks<BasicStackNode<IBasicParseForest>> forActorStacks_) {
            operations.add(bh -> {
                bh.consume(forActorStacks.nonEmpty()); // The condition in the while loop in Parser::parseCharacter
                bh.consume(forActorStacks.remove());
            });
        }

        @Override public void addForActorStack(BasicStackNode<IBasicParseForest> stack) {
            operations.add(bh -> forActorStacks.add(stack));
        }

        @Override public void directLinkFound(
            AbstractParseState<IInputStack, BasicStackNode<IBasicParseForest>> parseState,
            StackLink<IBasicParseForest, BasicStackNode<IBasicParseForest>> directLink) {
            if(directLink == null) {
                // Only if no existing direct link is found during a reduction, a new link is created and some active
                // stacks (those that are not reject and not in for actor) need to be revisited

                List<BasicStackNode<IBasicParseForest>> activeStacksCopy = activeStacksCopy(parseState);

                operations.add(bh -> {
                    for(BasicStackNode<IBasicParseForest> activeStack : activeStacksCopy)
                        bh.consume(!activeStack.allLinksRejected() && !forActorStacks.contains(activeStack));
                });
            }
        }

        private List<BasicStackNode<IBasicParseForest>>
            activeStacksCopy(AbstractParseState<IInputStack, BasicStackNode<IBasicParseForest>> parseState) {
            List<BasicStackNode<IBasicParseForest>> activeStacksCopy = new ArrayList<>();

            for(BasicStackNode<IBasicParseForest> activeStack : parseState.activeStacks)
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
