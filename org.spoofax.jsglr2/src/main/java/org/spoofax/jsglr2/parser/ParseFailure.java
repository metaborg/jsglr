package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public class ParseFailure<StackNode extends AbstractStackNode<ParseForest>, ParseForest extends AbstractParseForest, AbstractSyntaxTree> extends ParseResult<StackNode, ParseForest, AbstractSyntaxTree> {
	
	public final ParseException parseException;
	
	public ParseFailure(Parse<StackNode, ParseForest> parse, ParseException parseException) {
        super(parse, false);
        
		this.parseException = parseException;
	}

}
