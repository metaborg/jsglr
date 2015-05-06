package org.spoofax.jsglr.client;

import java.util.Set;

public class SGLRParseResult {
    public final Set<State> completionStates;
    public final Object output;


    public SGLRParseResult(Set<State> completionStates, Object output) {
        this.completionStates = completionStates;
        this.output = output;
    }
}
