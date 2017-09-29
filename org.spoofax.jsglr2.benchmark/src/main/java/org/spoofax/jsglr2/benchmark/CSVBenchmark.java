package org.spoofax.jsglr2.benchmark;

import java.io.IOException;
import java.util.List;

public class CSVBenchmark extends JSGLR2GrammarBenchmark {
    
    public CSVBenchmark() {
        super("csv");
    }
    
    protected List<Input> getInputs() throws IOException {
    		return getSingleInput("CSV/random_1000.csv");
    }

}
