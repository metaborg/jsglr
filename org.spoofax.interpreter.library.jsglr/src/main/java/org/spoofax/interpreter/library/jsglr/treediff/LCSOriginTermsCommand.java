package org.spoofax.interpreter.library.jsglr.treediff;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.terms.attachments.OriginAttachment;

public class LCSOriginTermsCommand<T> implements LCSCommand<IStrategoTerm> {

	public boolean isMatch(IStrategoTerm t1, IStrategoTerm t2) {
		return OriginAttachment.getOrigin(t2) == t1;
	}

}
