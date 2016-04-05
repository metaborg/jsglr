package org.spoofax.jsglr.client;


public class SGLRParseResult {
    public final CompletionStateSet completionStates;
    public final Object output;


    public SGLRParseResult(CompletionStateSet resultCompletionStates, Object output) {
        this.completionStates = resultCompletionStates;
        this.output = output;
    }
}
