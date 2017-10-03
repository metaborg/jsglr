package org.spoofax.jsglr2.benchmark.jsglr1;

import java.io.IOException;
import java.util.List;

public class JSGLR1CSVBenchmark extends JSGLR1GrammarBenchmark {
    
    public JSGLR1CSVBenchmark() {
        super("csv");
    }
    
    protected List<Input> getInputs() throws IOException {
    		return getSingleInput("CSV/random_1000.csv");
    }

}
