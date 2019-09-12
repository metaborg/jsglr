package org.spoofax.jsglr2.recovery;

import org.spoofax.jsglr2.parser.Position;

public class RecoveryJob {

    public Position position;
    public int iteration;
    public int quota;

    public RecoveryJob(Position position) {
        this.position = position;
        this.iteration = 0;
    }

    boolean hasNextIteration() {
        return iteration < 3;
    }

    int nextIteration() {
        quota = ++iteration;

        return iteration;
    }

}
