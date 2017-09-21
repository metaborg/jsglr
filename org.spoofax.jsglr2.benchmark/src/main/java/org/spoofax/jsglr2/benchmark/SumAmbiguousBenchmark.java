package org.spoofax.jsglr2.benchmark;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.openjdk.jmh.annotations.Param;

public class SumAmbiguousBenchmark extends JSGLR2GrammarBenchmark {
    
    public SumAmbiguousBenchmark() {
        super("sum-ambiguous");
    }
    
    @Param({"10", "20", "40", "80"})
    public int n;
    
    protected List<Input> getInputs() throws IOException {
        String string = String.join("+", Collections.nCopies(n, "x"));
        
        Input input = new Input("", string);
        
        return Arrays.asList(input);
    }

}
