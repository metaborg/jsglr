package org.spoofax.jsglr2.recovery;

public class RecoveryJob {

    public int offset;
    public int iteration;
    final int iterationsQuota;
    public int quota;

    public RecoveryJob(int offset, int iterationsQuota) {
        this.offset = offset;
        this.iteration = -1;
        this.iterationsQuota = iterationsQuota;
    }

    boolean hasNextIteration() {
        return iteration + 1 < iterationsQuota;
    }

    int nextIteration() {
        quota = (++iteration + 1);

        return iteration;
    }

}
