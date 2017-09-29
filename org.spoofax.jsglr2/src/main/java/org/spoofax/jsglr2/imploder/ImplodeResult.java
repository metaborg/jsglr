package org.spoofax.jsglr2.imploder;

import org.spoofax.jsglr2.JSGLR2Result;
import org.spoofax.jsglr2.parser.Parse;

public class ImplodeResult<AbstractSyntaxTree> extends JSGLR2Result {

	public final AbstractSyntaxTree ast;
	
	public ImplodeResult(Parse<?, ?> parse, AbstractSyntaxTree ast) {
	    super(parse, true);
	    
		this.ast = ast;
	}
	
}
