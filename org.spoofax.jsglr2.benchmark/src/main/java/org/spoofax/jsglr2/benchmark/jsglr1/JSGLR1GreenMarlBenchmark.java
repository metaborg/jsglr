package org.spoofax.jsglr2.benchmark.jsglr1;

import java.io.IOException;
import java.util.List;

public class JSGLR1GreenMarlBenchmark extends JSGLR1ParseTableBenchmark {
    
    public JSGLR1GreenMarlBenchmark() {
        super("GreenMarl");
    }
    
    protected List<Input> getInputs() throws IOException {
        return getSingleInput("GreenMarl/infomap.gm");
    }

}
