package org.spoofax.jsglr2.parser;

public class ParseSuccess<ParseForest> extends ParseResult<ParseForest> {

	public final ParseForest parseResult;
	
	public ParseSuccess(Parse parse, ParseForest parseResult) {
	    super(parse, true);
	    
		this.parseResult = parseResult;
	}
	
}
