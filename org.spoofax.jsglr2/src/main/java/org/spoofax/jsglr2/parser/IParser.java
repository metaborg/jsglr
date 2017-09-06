package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public interface IParser<StackNode extends AbstractStackNode<ParseForest>, ParseForest extends AbstractParseForest> {
    
    public ParseResult<ParseForest> parse(String input, String filename);
    
    public default ParseResult<ParseForest> parse(String input) {
        return parse(input, "");
    }
    
    public default ParseForest parseUnsafe(String input, String filename) throws ParseException {
        ParseResult<ParseForest> result = parse(input, filename);
        
        if (result.isSuccess) {
            ParseSuccess<ParseForest> success = (ParseSuccess<ParseForest>) result;
        
            return success.parseResult;
        } else {
            ParseFailure<ParseForest> failure = (ParseFailure<ParseForest>) result;
            
            throw failure.parseException;
        }
    }
    
    public default ParseForest parseUnsafe(String input) throws ParseException {
        return parseUnsafe(input, "");
    }
	
	public void attachObserver(IParserObserver<StackNode, ParseForest> observer);

}
