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
        return iteration < iterationsQuota;
    }

    int nextIteration() {
        if(this.backtrackChoicePointIndex > 0)
            this.backtrackChoicePointIndex--;

        quota = (++iteration + 1);

        return iteration;
    }

}
