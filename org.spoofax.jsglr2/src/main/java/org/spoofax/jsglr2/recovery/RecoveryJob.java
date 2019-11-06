package org.spoofax.jsglr2.recovery;

public class RecoveryJob {

    public int backtrackChoicePointIndex;
    public int offset;
    public int iteration;
    final int iterationsQuota;
    public int quota;

    public RecoveryJob(int backtrackChoicePointIndex, int offset, int iterationsQuota) {
        this.backtrackChoicePointIndex = backtrackChoicePointIndex;
        this.offset = offset;
        this.iteration = -1;
        this.iterationsQuota = iterationsQuota;
    }

    boolean hasNextIteration() {
        return iteration + 1 < iterationsQuota;
    }

    RecoveryJob nextIteration() {
        quota = (++iteration + 1);

        return this;
    }

    public int iterationBacktrackChoicePointIndex() {
        return Math.max(backtrackChoicePointIndex - iteration, 0);
    }

}
