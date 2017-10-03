package org.spoofax.jsglr2.benchmark.jsglr1;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.openjdk.jmh.annotations.Param;

public class JSGLR1LexicalBenchmark extends JSGLR1GrammarBenchmark {
    
    public JSGLR1LexicalBenchmark() {
        super("lexical-id");
    }
    
    @Param({"50000", "100000"})
    public int n;
    
    protected List<Input> getInputs() throws IOException {
        String string = String.join("", Collections.nCopies(n, "a"));
        
        Input input = new Input("", string);
        
        return Arrays.asList(input);
    }

}
