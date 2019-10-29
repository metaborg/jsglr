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
import org.spoofax.jsglr2.recovery.BacktrackChoicePoint;
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

    protected void testRecovery(String inputString) {
        for(TestVariant variant : getTestVariants(isNotRecoveryVariant)) {
            ParseResult<?> parseResult = variant.parser().parse(inputString);

            assertEquals("Variant '" + variant.name() + "' should fail for non-recovering parsing: ", false,
                parseResult.isSuccess());
        }

        for(TestVariant variant : getTestVariants(isRecoveryVariant)) {
            ParseResult<?> parseResult = variant.parser().parse(inputString);

            assertEquals("Variant '" + variant.name() + "' should succeed for recovering parsing: ", true,
                parseResult.isSuccess());
        }
    }

    protected void testRecoveryTraced(String inputString, WithRecoveryTrace withRecoveryTrace) {
        for(TestVariant variant : getTestVariants(isRecoveryVariant)) {
            IObservableParser parser = (IObservableParser) variant.parser();

            RecoveryTrace recoveryTrace = new RecoveryTrace<>();

            parser.observing().attachObserver(recoveryTrace);

            ParseResult<?> parseResult = parser.parse(inputString);

            assertEquals("Variant '" + variant.name() + "' should succeed for recovering parsing: ", true,
                parseResult.isSuccess());

            withRecoveryTrace.get(recoveryTrace);
        }
    }

    public interface WithRecoveryTrace {

        void get(RecoveryTrace recoveryTrace);

    }

    public static class RecoveryTrace
    //@formatter:off
       <ParseForest extends IParseForest,
        Derivation  extends IDerivation<ParseForest>,
        ParseNode   extends IParseNode<ParseForest, Derivation>,
        StackNode   extends IStackNode,
        ParseState  extends AbstractParseState<ParseForest, StackNode> & IRecoveryParseState<ParseForest, StackNode>>
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

        @Override public void recoveryBacktrackChoicePoint(BacktrackChoicePoint<ParseForest, StackNode> choicePoint) {
            backtrackChoicePoints.add(choicePoint.index, choicePoint.offset);
        }

        @Override public void startRecovery(ParseState parseState) {
            started.add(parseState.currentOffset);
        }

        @Override public void recoveryIteration(ParseState parseState) {
            iterations.add(new RecoverIteration(parseState.recoveryJob()));
        }

        @Override public void endRecovery(ParseState parseState) {
            ended.add(parseState.currentOffset);
        }

    }

    public static class RecoverIteration {

        public final int iteration;
        public final int offset;
        public final int backtrackChoicePointIndex;

        RecoverIteration(RecoveryJob recoveryJob) {
            this.iteration = recoveryJob.iteration;
            this.offset = recoveryJob.offset;
            this.backtrackChoicePointIndex = recoveryJob.iterationBacktrackChoicePointIndex();
        }

        public RecoverIteration(int iteration, int offset, int backtrackChoicePointIndex) {
            this.iteration = iteration;
            this.offset = offset;
            this.backtrackChoicePointIndex = backtrackChoicePointIndex;
        }

        @Override public boolean equals(Object obj) {
            if(!(obj instanceof RecoverIteration))
                return false;

            RecoverIteration other = (RecoverIteration) obj;

            return offset == other.offset && iteration == other.iteration
                && backtrackChoicePointIndex == other.backtrackChoicePointIndex;
        }

        @Override public String toString() {
            return "" + iteration + "@" + offset + "/" + backtrackChoicePointIndex;
        }
    }

}
