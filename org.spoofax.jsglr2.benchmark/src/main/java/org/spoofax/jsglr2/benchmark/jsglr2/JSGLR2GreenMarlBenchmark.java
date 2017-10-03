package org.spoofax.jsglr2.benchmark.jsglr2;

import java.io.IOException;
import java.util.List;

public class JSGLR2GreenMarlBenchmark extends JSGLR2ParseTableBenchmark {
    
    public JSGLR2GreenMarlBenchmark() {
        super("GreenMarl");
    }
    
    protected List<Input> getInputs() throws IOException {
        return getSingleInput("GreenMarl/infomap.gm");
    }

}
