package org.spoofax.jsglr2.imploder;

import org.spoofax.jsglr2.JSGLR2Result;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public class ImplodeResult<StackNode extends AbstractStackNode<ParseForest>, ParseForest extends AbstractParseForest, AbstractSyntaxTree> extends JSGLR2Result<StackNode, ParseForest, AbstractSyntaxTree> {

	public final AbstractSyntaxTree ast;
	
	public ImplodeResult(Parse<StackNode, ParseForest> parse, AbstractSyntaxTree ast) {
	    super(parse, true);
	    
		this.ast = ast;
	}
	
}
