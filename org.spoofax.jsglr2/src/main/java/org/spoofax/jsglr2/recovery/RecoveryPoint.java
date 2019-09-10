package org.spoofax.jsglr2.recovery;

import org.spoofax.jsglr2.parser.Position;

public class RecoveryPoint {

    public Position position;
    public int iteration;

    public RecoveryPoint(Position position) {
        this.position = position;
        this.iteration = 0;
    }

    boolean hasNextIteration() {
        return iteration < 3;
    }

    int nextIteration() {
        return iteration++;
    }

}
