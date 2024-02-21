package org.spoofax.interpreter.library.jsglr.origin;

import static jsglr.shared.ImploderAttachment.*;

import java.util.ArrayList;

import org.spoofax.interpreter.core.IContext;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.IStrategoTuple;
import org.spoofax.terms.attachments.OriginAttachment;

import jsglr.shared.IToken;
import jsglr.shared.ITokenizer;

/**
 * Tokens + TokenKind
 */
public class OriginTokenStreamPrimitive extends AbstractOriginPrimitive {
	
	public OriginTokenStreamPrimitive() {
		super("SSL_EXT_origin_token_stream");
	}

	@Override
	public IStrategoTerm call(IContext env, IStrategoTerm origin) {
		ITokenizer tokenizer = (ITokenizer) getTokenizer(origin);
		int startIndex=getLeftToken(origin).getIndex();
		int endIndex = getRightToken(origin).getIndex();
		ArrayList<IStrategoTerm> tokenTuples=new ArrayList<IStrategoTerm>();
		for (int i = startIndex; i <= endIndex; i++) {
			if(tokenizer.getTokenAt(i).getKind() != IToken.Kind.TK_EOF){
				IStrategoTerm tokenText = env.getFactory().makeString(tokenizer.getTokenAt(i).toString());
				IStrategoTerm tokenSort = env.getFactory().makeInt(tokenizer.getTokenAt(i).getKind().ordinal());
				IStrategoTerm tokenIndex = env.getFactory().makeInt(i);
				IStrategoTuple tokenInfo = env.getFactory().makeTuple(tokenIndex, tokenText, tokenSort);
				IStrategoTerm nodeOfToken = OriginAttachment.getOrigin(tokenizer.getTokenAt(i).getAstNode());
				if(nodeOfToken != null)
					OriginAttachment.setOrigin(tokenInfo, nodeOfToken);
				tokenTuples.add(tokenInfo);
			}
		}		
		return env.getFactory().makeList(tokenTuples);
	}
}
