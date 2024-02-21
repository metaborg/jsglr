package org.spoofax.interpreter.library.jsglr.origin;

import static jsglr.shared.ImploderAttachment.*;

import java.util.ArrayList;

import org.spoofax.interpreter.core.IContext;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.terms.attachments.OriginAttachment;

import jsglr.shared.IToken;
import jsglr.shared.ITokenizer;

/**
 * Tokens
 */
public class OriginTokensPrimitive extends AbstractOriginPrimitive {
	
	public OriginTokensPrimitive() {
		super("SSL_EXT_origin_tokens");
	}

	@Override
	public IStrategoTerm call(IContext env, IStrategoTerm origin) {
		ITokenizer tokenizer = (ITokenizer) getTokenizer(origin);
		int startIndex=getLeftToken(origin).getIndex();
		int endIndex = getRightToken(origin).getIndex();
		ArrayList<IStrategoTerm> tokenStrings=new ArrayList<IStrategoTerm>();
		for (int i = startIndex; i <= endIndex; i++) {
			if(tokenizer.getTokenAt(i).getKind() != IToken.Kind.TK_EOF){
				IStrategoTerm tokenString = env.getFactory().makeString(tokenizer.getTokenAt(i).toString());
				IStrategoTerm nodeOfToken = OriginAttachment.getOrigin(tokenizer.getTokenAt(i).getAstNode());
				if(nodeOfToken != null)
					OriginAttachment.setOrigin(tokenString, nodeOfToken);
				tokenStrings.add(tokenString);
			}
		}		
		return env.getFactory().makeList(tokenStrings);
	}
}
