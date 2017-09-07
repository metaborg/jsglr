package org.spoofax.jsglr2.benchmark;

import java.io.IOException;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.terms.ParseError;

public abstract class JSGLR2ParseTableBenchmark extends JSGLR2Benchmark {

    private String parseTableName;
    
    protected JSGLR2ParseTableBenchmark(String parseTableName) {
        this.parseTableName = parseTableName;
    }

    protected void prepareParseTable() throws ParseError, ParseTableReadException, IOException, InvalidParseTableException {
        setupParseTable(parseTableName);
    }

}
