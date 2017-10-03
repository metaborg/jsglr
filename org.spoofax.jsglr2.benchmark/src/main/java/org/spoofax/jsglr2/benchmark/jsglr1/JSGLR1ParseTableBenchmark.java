package org.spoofax.jsglr2.benchmark.jsglr1;

import java.io.IOException;
import java.io.InputStream;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.terms.ParseError;

public abstract class JSGLR1ParseTableBenchmark extends JSGLR1Benchmark {

    private String parseTableName;
    
    protected JSGLR1ParseTableBenchmark(String parseTableName) {
        this.parseTableName = parseTableName;
    }

    protected void prepareParseTable() throws ParseError, ParseTableReadException, IOException, InvalidParseTableException {
        setupParseTable(parseTableName);
    }
	
    @Override
	public IStrategoTerm parseTableTerm(String filename) throws ParseError, IOException {
    		InputStream inputStream = getClass().getResourceAsStream("/" + filename);
  		
  		return getTermReader().parseFromStream(inputStream);
	}

}
