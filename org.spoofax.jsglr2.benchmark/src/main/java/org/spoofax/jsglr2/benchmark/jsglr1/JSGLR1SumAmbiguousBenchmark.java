package org.spoofax.jsglr2.benchmark.jsglr1;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.openjdk.jmh.annotations.Param;

public class JSGLR1SumAmbiguousBenchmark extends JSGLR1GrammarBenchmark {
    
    public JSGLR1SumAmbiguousBenchmark() {
        super("sum-ambiguous");
    }
    
    @Param({"5", "10", "15"})
    public int n;
    
    protected List<Input> getInputs() throws IOException {
        String string = String.join("+", Collections.nCopies(n, "x"));
        
        Input input = new Input("", string);
        
        return Arrays.asList(input);
    }

}
