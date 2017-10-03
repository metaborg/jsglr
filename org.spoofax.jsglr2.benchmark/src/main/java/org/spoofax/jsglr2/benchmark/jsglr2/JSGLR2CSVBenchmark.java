package org.spoofax.jsglr2.benchmark.jsglr2;

import java.io.IOException;
import java.util.List;

public class JSGLR2CSVBenchmark extends JSGLR2GrammarBenchmark {
    
    public JSGLR2CSVBenchmark() {
        super("csv");
    }
    
    protected List<Input> getInputs() throws IOException {
    		return getSingleInput("CSV/random_1000.csv");
    }

}
