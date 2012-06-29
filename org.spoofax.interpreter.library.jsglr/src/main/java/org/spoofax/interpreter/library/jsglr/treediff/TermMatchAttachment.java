package org.spoofax.interpreter.library.jsglr.treediff;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.terms.attachments.AbstractTermAttachment;
import org.spoofax.terms.attachments.ParentAttachment;
import org.spoofax.terms.attachments.TermAttachmentType;
import org.spoofax.terms.attachments.VolatileTermAttachmentType;

/**
 * A term match attachment that stores the matching of the attached term in AST1 with a term in AST2. 
 * Matching is a symmetric 1-1 relation between terms in one AST with the terms in another AST.
 * @author maartje
 *
 */
public class TermMatchAttachment extends AbstractTermAttachment {

	private IStrategoTerm matchedTrm;
	
	private static final long serialVersionUID = -3114392265614382463L;

	public static final TermAttachmentType<TermMatchAttachment> TYPE =
		new VolatileTermAttachmentType<TermMatchAttachment>(TermMatchAttachment.class);

	public TermAttachmentType<TermMatchAttachment> getAttachmentType() {
		return TYPE;
	}
	
	private TermMatchAttachment(IStrategoTerm matchedTrm) {
		this.matchedTrm = matchedTrm;
	}

	/**
	 * Accomplishes a match between term1 in AST1 and term2 in AST2 if and only if 
	 * term1 and term2 are not already matched.
	 * @param term1 Term in AST1 
	 * @param term2 Term in AST2
	 * @return True iff term1 and term2 are matched after this function returns
	 */
	public static boolean tryMatchTerms(IStrategoTerm term1, IStrategoTerm term2) {
		assert(term1 != term2);
		assert(ParentAttachment.getRoot(term1) != ParentAttachment.getRoot(term2) || ParentAttachment.getRoot(term2) == null);
		if(TermMatchAttachment.getMatchedTerm(term1) == term2){
			return true;
		}
		if(!hasMatchedTerm(term1) && !hasMatchedTerm(term2)){
			term1.putAttachment(new TermMatchAttachment(term2));
			term2.putAttachment(new TermMatchAttachment(term1));
			return true;
		}
		return false;
	}

	/**
	 * Breaks the matching between term1 and its matched term.
	 * @param term1
	 */
	public static void unMatchTerm(IStrategoTerm term1) {
		IStrategoTerm match1 = TermMatchAttachment.getMatchedTerm(term1);
		if(match1 != null){
			match1.removeAttachment(TermMatchAttachment.TYPE);
			term1.removeAttachment(TermMatchAttachment.TYPE);
			assert TermMatchAttachment.getTermMatchAttachment(term1) == null;
			assert TermMatchAttachment.getTermMatchAttachment(match1) == null;
		}
	}

	/**
	 * Breaks the original matchings of term1 and term2, and matches them.
	 * @param term1 Term in AST1
	 * @param term2 Term in AST2
	 */
	public static void forceMatchTerms(IStrategoTerm term1, IStrategoTerm term2) {
		if(TermMatchAttachment.getMatchedTerm(term1) != term2 ){
			TermMatchAttachment.unMatchTerm(term1);
			TermMatchAttachment.unMatchTerm(term2);
			TermMatchAttachment.tryMatchTerms(term1, term2);
			assert TermMatchAttachment.getMatchedTerm(term1) == term2;
		}
	}

	/**
	 * Gets the matching partner of a term
	 * @param term
	 * @return Matching partner in other AST
	 */
	public static IStrategoTerm getMatchedTerm(IStrategoTerm term) {
		TermMatchAttachment tma = TermMatchAttachment.getTermMatchAttachment(term);
		IStrategoTerm matched = tma == null ? null : tma.matchedTrm;
		assert matched == null || TermMatchAttachment.getTermMatchAttachment(matched).matchedTrm == term : 
			"error: non-symetric term match";
		assert(term != matched || term == null);
		assert(ParentAttachment.getRoot(term) != ParentAttachment.getRoot(matched) || ParentAttachment.getRoot(term) == null);
		return matched;
	}


	/**
	 * Says wether term1 in AST1 is matched to some other term in AST2
	 * @param term1
	 * @return True iff term1 is matched
	 */
	public static boolean hasMatchedTerm(IStrategoTerm term1) {
		return getMatchedTerm(term1) != null;
	}
		
	private static TermMatchAttachment getTermMatchAttachment(IStrategoTerm term) {
		if(term == null)
			return null;
		return (TermMatchAttachment)term.getAttachment(TYPE);
	}
}
