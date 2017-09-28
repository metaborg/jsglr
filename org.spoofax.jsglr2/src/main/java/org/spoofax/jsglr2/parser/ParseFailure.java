package org.spoofax.jsglr2.parser;

public class ParseFailure<ParseForest> extends ParseResult<ParseForest> {
	
	public final ParseException parseException;
	
	public ParseFailure(Parse parse, ParseException parseException) {
        super(parse, false);
        
		this.parseException = parseException;
	}

}
