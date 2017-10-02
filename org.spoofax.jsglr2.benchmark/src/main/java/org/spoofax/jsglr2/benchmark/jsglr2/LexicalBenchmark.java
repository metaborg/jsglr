package org.spoofax.jsglr2.benchmark.jsglr2;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.openjdk.jmh.annotations.Param;

public class LexicalBenchmark extends JSGLR2GrammarBenchmark {
    
    public LexicalBenchmark() {
        super("lexical-id");
    }
    
    @Param({"100000"})
    public int n;
    
    protected List<Input> getInputs() throws IOException {
        String string = String.join("", Collections.nCopies(n, "a"));
        
        Input input = new Input("", string);
        
        return Arrays.asList(input);
    }

}
