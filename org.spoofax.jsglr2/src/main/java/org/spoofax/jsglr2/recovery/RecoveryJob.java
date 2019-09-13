package org.spoofax.jsglr2.recovery;

public class RecoveryJob {

    public int backtrackChoicePointIndex;
    public int offset;
    public int iteration;
    public int quota;

    public RecoveryJob(int backtrackChoicePointIndex, int offset) {
        this.backtrackChoicePointIndex = backtrackChoicePointIndex;
        this.offset = offset;
        this.iteration = 0;
    }

    boolean hasNextIteration() {
        return iteration < 3;
    }

    int nextIteration() {
        if(this.backtrackChoicePointIndex > 0)
            this.backtrackChoicePointIndex--;

        quota = ++iteration;

        return iteration;
    }

}
