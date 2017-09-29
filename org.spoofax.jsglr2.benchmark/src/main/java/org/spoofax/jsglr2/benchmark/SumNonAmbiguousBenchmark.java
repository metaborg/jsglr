package org.spoofax.jsglr2.benchmark;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.openjdk.jmh.annotations.Param;

public class SumNonAmbiguousBenchmark extends JSGLR2GrammarBenchmark {
    
    public SumNonAmbiguousBenchmark() {
        super("sum-nonambiguous");
    }
    
    @Param({"1000", "2000", "4000", "8000", "16000"})
    public int n;
    
    protected List<Input> getInputs() throws IOException {
        String string = String.join("+", Collections.nCopies(n, "x"));
        
        Input input = new Input("", string);
        
        return Arrays.asList(input);
    }

}
