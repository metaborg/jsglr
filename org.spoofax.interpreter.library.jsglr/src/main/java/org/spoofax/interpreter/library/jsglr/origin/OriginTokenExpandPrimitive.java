package org.spoofax.interpreter.library.jsglr.origin;

import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getLeftToken;
import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getRightToken;
import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getTokenizer;

import java.util.ArrayList;

import org.spoofax.interpreter.core.IContext;
import org.spoofax.interpreter.terms.IStrategoString;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr.client.imploder.ITokenizer;
import org.spoofax.terms.attachments.OriginAttachment;

/**
 * Tokens
 */
public class OriginTokenExpandPrimitive extends AbstractOriginPrimitive {
	
	public OriginTokenExpandPrimitive() {
		super("SSL_EXT_origin_token_expand");
	}

	@Override
	public IStrategoTerm call(IContext env, IStrategoTerm origin) {
		ITokenizer tokenizer=getTokenizer(origin);
		int startIndex=getLeftToken(origin).getIndex();
		int endIndex = getRightToken(origin).getIndex();
		ArrayList<IStrategoTerm> resultTerms=new ArrayList<IStrategoTerm>();
		int i = startIndex;
		while (i <= endIndex) {
			IToken tok = tokenizer.getTokenAt(i);
			if(tok.getKind() != IToken.TK_EOF){
				IStrategoTerm containingSubTerm = getContainingSubTerm(i, origin);
				if(containingSubTerm != null){
					resultTerms.add(containingSubTerm);
					i = getRightToken(containingSubTerm).getIndex();
				}
				else {
					IStrategoString tokenTerm = env.getFactory().makeString(tokenizer.getTokenAt(i).toString());
					OriginAttachment.setOrigin(tokenTerm, origin);
					resultTerms.add(tokenTerm);
				}
			}
			i += 1;
		}		
		return env.getFactory().makeList(resultTerms);
	}
	
	private boolean inTokenRange(int tokenIndex, IStrategoTerm origin){
		int startIndex=getLeftToken(origin).getIndex();
		int endIndex = getRightToken(origin).getIndex();
		return startIndex <= tokenIndex && tokenIndex <= endIndex; 
	}

	private IStrategoTerm getContainingSubTerm(int tokenIndex, IStrategoTerm origin){
		IStrategoTerm[] subTerms = origin.getAllSubterms();
		for (int i = 0; i < subTerms.length; i++) {
			if(inTokenRange(tokenIndex, subTerms[i]))
				return subTerms[i];
		}
		return null;
	}
}
