package org.spoofax.jsglr2.benchmark.jsglr1;

import java.io.IOException;
import java.net.URISyntaxException;

import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.terms.ParseError;

public abstract class JSGLR1GrammarBenchmark extends JSGLR1Benchmark {

private String grammarName;
    
    protected JSGLR1GrammarBenchmark(String grammarName) {
        this.grammarName = grammarName;
    }

    protected void prepareParseTable() throws ParseError, InterruptedException, IOException, ParseTableReadException, InvalidParseTableException, URISyntaxException {
        setupParseTableFromDefFile(grammarName);
    }

}
