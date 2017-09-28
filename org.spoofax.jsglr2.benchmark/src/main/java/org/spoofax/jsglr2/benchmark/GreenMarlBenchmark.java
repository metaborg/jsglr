package org.spoofax.jsglr2.benchmark;

import java.io.IOException;
import java.util.List;

public class GreenMarlBenchmark extends JSGLR2ParseTableBenchmark {
    
    public GreenMarlBenchmark() {
        super("GreenMarl");
    }
    
    protected List<Input> getInputs() throws IOException {
        return getSingleInput("GreenMarl/infomap.gm");
    }

}
