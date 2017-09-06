package org.spoofax.jsglr2.benchmark;

import java.io.IOException;
import java.util.List;

public class WebDSLBenchmark extends JSGLR2Benchmark {
    
    public WebDSLBenchmark() {
        super("WebDSL.tbl");
    }
    
    protected List<Input> getInputs() throws IOException {
        return getSingleInput("WebDSL/built-in.app");
    }

}
