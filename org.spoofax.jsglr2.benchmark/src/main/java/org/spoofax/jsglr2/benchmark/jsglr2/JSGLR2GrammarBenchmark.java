package org.spoofax.jsglr2.benchmark.jsglr2;

import java.io.IOException;
import java.net.URISyntaxException;

import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.terms.ParseError;

public abstract class JSGLR2GrammarBenchmark extends JSGLR2Benchmark {

    private String grammarName;
    
    protected JSGLR2GrammarBenchmark(String grammarName) {
        this.grammarName = grammarName;
    }

    protected void prepareParseTable() throws ParseError, InterruptedException, IOException, ParseTableReadException, InvalidParseTableException, URISyntaxException {
        setupParseTableFromDefFile(grammarName);
    }

}
