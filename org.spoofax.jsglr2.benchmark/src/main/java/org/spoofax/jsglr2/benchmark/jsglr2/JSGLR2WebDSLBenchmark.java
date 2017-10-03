package org.spoofax.jsglr2.benchmark.jsglr2;

import java.io.IOException;
import java.util.List;

public class JSGLR2WebDSLBenchmark extends JSGLR2ParseTableBenchmark {
    
    public JSGLR2WebDSLBenchmark() {
        super("WebDSL");
    }
    
    protected List<Input> getInputs() throws IOException {
        return getSingleInput("WebDSL/built-in.app");
    }

}
