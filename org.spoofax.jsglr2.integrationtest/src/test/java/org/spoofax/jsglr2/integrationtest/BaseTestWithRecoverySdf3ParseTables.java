package org.spoofax.jsglr2.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.metaborg.parsetable.IParseTable;
import org.spoofax.jsglr2.integration.ParseTableVariant;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.IObservableParser;
import org.spoofax.jsglr2.parser.observing.IParserObserver;
import org.spoofax.jsglr2.parser.result.ParseResult;
import org.spoofax.jsglr2.recovery.IBacktrackChoicePoint;
import org.spoofax.jsglr2.recovery.IRecoveryParseState;
import org.spoofax.jsglr2.recovery.RecoveryJob;
import org.spoofax.jsglr2.stack.IStackNode;

public abstract class BaseTestWithRecoverySdf3ParseTables extends BaseTestWithSdf3ParseTables {

    protected BaseTestWithRecoverySdf3ParseTables(String sdf3Resource) {
        super(sdf3Resource);
    }

    @Override public IParseTable getParseTable(ParseTableVariant variant, String sdf3Resource) throws Exception {
        return sdf3ToParseTable.getParseTable(variant, sdf3Resource);
    }

    protected Predicate<TestVariant> isRecoveryVariant = testVariant -> testVariant.variant.parser.recovery;

    protected Predicate<TestVariant> isNotRecoveryVariant = isRecoveryVariant.negate();

    protected Stream<DynamicTest> testRecovery(String inputString, boolean recovers) {
        Stream<DynamicTest> notRecoveryTests = testPerVariant(getTestVariants(isNotRecoveryVariant), variant -> () -> {
            ParseResult<?> parseResult = variant.parser().parse(inputString);

            assertEquals(false, parseResult.isSuccess(), "Non-recovering parsing should fail");
        });

        Stream<DynamicTest> recoveryTests = testPerVariant(getTestVariants(isRecoveryVariant), variant -> () -> {
            ParseResult<?> parseResult = variant.parser().parse(inputString);

            assertEquals(recovers, parseResult.isSuccess(),
                "Parsing should " + (recovers ? "succeed" : "fail") + " with recovering parsing");
        });

        return Stream.concat(notRecoveryTests, recoveryTests);
    }

    protected Stream<DynamicTest> testRecovery(String inputString) {
        return testRecovery(inputString, true);
    }

    protected Stream<DynamicTest> testRecoveryTraced(String inputString, WithRecoveryTrace withRecoveryTrace,
        boolean recovers) {
        return testPerVariant(getTestVariants(isRecoveryVariant), variant -> () -> {
            IObservableParser parser = (IObservableParser) variant.parser();

            RecoveryTrace recoveryTrace = new RecoveryTrace<>();

            parser.observing().attachObserver(recoveryTrace);

            ParseResult<?> parseResult = parser.parse(inputString);

            assertEquals(recovers, parseResult.isSuccess(),
                "Parsing should " + (recovers ? "succeed" : "fail") + " with recovering parsing");

            withRecoveryTrace.get(recoveryTrace);
        });
    }

    protected Stream<DynamicTest> testRecoveryTraced(String inputString, WithRecoveryTrace withRecoveryTrace) {
        return testRecoveryTraced(inputString, withRecoveryTrace, true);
    }

    public interface WithRecoveryTrace {

        void get(RecoveryTrace recoveryTrace);

    }

    public static class RecoveryTrace
    //@formatter:off
       <ParseForest          extends IParseForest,
        Derivation           extends IDerivation<ParseForest>,
        ParseNode            extends IParseNode<ParseForest, Derivation>,
        StackNode            extends IStackNode,
        BacktrackChoicePoint extends IBacktrackChoicePoint<?, StackNode>,
        ParseState           extends AbstractParseState<?, StackNode> & IRecoveryParseState<?, StackNode, BacktrackChoicePoint>>
    //@formatter:on
        implements IParserObserver<ParseForest, Derivation, ParseNode, StackNode, ParseState> {

        public final List<Integer> backtrackChoicePoints;
        public final List<Integer> started;
        public final List<Integer> ended;
        public final List<RecoverIteration> iterations;

        RecoveryTrace() {
            backtrackChoicePoints = new ArrayList<>();
            started = new ArrayList<>();
            ended = new ArrayList<>();
            iterations = new ArrayList<>();
        }

        @Override public void recoveryBacktrackChoicePoint(int index, IBacktrackChoicePoint<?, StackNode> choicePoint) {
            if(index > backtrackChoicePoints.size() - 1)
                backtrackChoicePoints.add(choicePoint.inputStack().offset());
            else
                backtrackChoicePoints.set(index, choicePoint.inputStack().offset());
        }

        @Override public void startRecovery(ParseState parseState) {
            started.add(parseState.inputStack.offset());
        }

        @Override public void recoveryIteration(ParseState parseState) {
            iterations
                .add(new RecoverIteration(parseState.recoveryJob(), parseState.lastBacktrackChoicePoint().offset()));
        }

        @Override public void endRecovery(ParseState parseState) {
            ended.add(parseState.inputStack.offset());
        }

    }

    public static class RecoverIteration {

        public final int iteration;
        public final int offset;
        public final int backtrackChoicePointOffset;

        RecoverIteration(RecoveryJob recoveryJob, int backtrackChoicePointOffset) {
            this.iteration = recoveryJob.iteration;
            this.offset = recoveryJob.offset;
            this.backtrackChoicePointOffset = backtrackChoicePointOffset;
        }

        public RecoverIteration(int iteration, int offset, int backtrackChoicePointOffset) {
            this.iteration = iteration;
            this.offset = offset;
            this.backtrackChoicePointOffset = backtrackChoicePointOffset;
        }

        @Override public boolean equals(Object obj) {
            if(!(obj instanceof RecoverIteration))
                return false;

            RecoverIteration other = (RecoverIteration) obj;

            return offset == other.offset && iteration == other.iteration
                && backtrackChoicePointOffset == other.backtrackChoicePointOffset;
        }

        @Override public String toString() {
            return "" + iteration + "[" + backtrackChoicePointOffset + "->" + offset + "]";
        }
    }

}
