package org.spoofax.jsglr2.integrationtest;

import org.metaborg.parsetable.IParseTable;
import org.spoofax.jsglr2.integration.ParseTableVariant;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.IObservableParser;
import org.spoofax.jsglr2.parser.observing.ParserObserver;
import org.spoofax.jsglr2.parser.result.ParseResult;
import org.spoofax.jsglr2.recovery.IBacktrackChoicePoint;
import org.spoofax.jsglr2.recovery.IRecoveryParseState;
import org.spoofax.jsglr2.recovery.RecoveryJob;
import org.spoofax.jsglr2.stack.IStackNode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;

public abstract class BaseTestWithRecoverySdf3ParseTables extends BaseTestWithSdf3ParseTables {

    protected BaseTestWithRecoverySdf3ParseTables(String sdf3Resource) {
        super(sdf3Resource);
    }

    @Override public IParseTable getParseTable(ParseTableVariant variant, String sdf3Resource) throws Exception {
        return sdf3ToParseTable.getParseTable(variant, sdf3Resource);
    }

    private Predicate<TestVariant> isRecoveryVariant = testVariant -> testVariant.variant.parser.recovery;

    private Predicate<TestVariant> isNotRecoveryVariant = testVariant -> !testVariant.variant.parser.recovery;

    protected void testRecovery(String inputString, boolean recovers) {
        for(TestVariant variant : getTestVariants(isNotRecoveryVariant)) {
            ParseResult<?> parseResult = variant.parser().parse(inputString);

            assertEquals("Variant '" + variant.name() + "' should fail for non-recovering parsing: ", false,
                parseResult.isSuccess());
        }

        for(TestVariant variant : getTestVariants(isRecoveryVariant)) {
            ParseResult<?> parseResult = variant.parser().parse(inputString);

            assertEquals("Variant '" + variant.name() + "' should " + (recovers ? "succeed" : "fail")
                + " for recovering parsing: ", recovers, parseResult.isSuccess());
        }
    }

    protected void testRecovery(String inputString) {
        testRecovery(inputString, true);
    }

    protected void testRecoveryTraced(String inputString, WithRecoveryTrace withRecoveryTrace, boolean recovers) {
        for(TestVariant variant : getTestVariants(isRecoveryVariant)) {
            IObservableParser parser = (IObservableParser) variant.parser();

            RecoveryTrace recoveryTrace = new RecoveryTrace<>();

            parser.observing().attachObserver(recoveryTrace);

            ParseResult<?> parseResult = parser.parse(inputString);

            assertEquals("Variant '" + variant.name() + "' should " + (recovers ? "succeed" : "fail")
                + " for recovering parsing: ", recovers, parseResult.isSuccess());

            withRecoveryTrace.get(recoveryTrace);
        }
    }

    protected void testRecoveryTraced(String inputString, WithRecoveryTrace withRecoveryTrace) {
        testRecoveryTraced(inputString, withRecoveryTrace, true);
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
        BacktrackChoicePoint extends IBacktrackChoicePoint<StackNode>,
        ParseState           extends AbstractParseState<StackNode> & IRecoveryParseState<StackNode, BacktrackChoicePoint>>
    //@formatter:on
        extends ParserObserver<ParseForest, Derivation, ParseNode, StackNode, ParseState> {

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

        @Override public void recoveryBacktrackChoicePoint(int index, IBacktrackChoicePoint<StackNode> choicePoint) {
            if(index > backtrackChoicePoints.size() - 1)
                backtrackChoicePoints.add(choicePoint.offset());
            else
                backtrackChoicePoints.set(index, choicePoint.offset());
        }

        @Override public void startRecovery(ParseState parseState) {
            started.add(parseState.currentOffset);
        }

        @Override public void recoveryIteration(ParseState parseState) {
            iterations
                .add(new RecoverIteration(parseState.recoveryJob(), parseState.lastBacktrackChoicePoint().offset()));
        }

        @Override public void endRecovery(ParseState parseState) {
            ended.add(parseState.currentOffset);
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
