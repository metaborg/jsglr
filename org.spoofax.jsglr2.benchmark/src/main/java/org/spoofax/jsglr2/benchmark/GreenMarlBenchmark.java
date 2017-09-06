package org.spoofax.jsglr2.benchmark;

import java.io.IOException;
import java.util.List;

public class GreenMarlBenchmark extends JSGLR2Benchmark {
    
    public GreenMarlBenchmark() {
        super("GreenMarl.tbl");
    }
    
    protected List<Input> getInputs() throws IOException {
        return getSingleInput("GreenMarl/infomap.gm");
    }

}
