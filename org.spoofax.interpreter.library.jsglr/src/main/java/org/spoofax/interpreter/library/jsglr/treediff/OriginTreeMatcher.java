package org.spoofax.interpreter.library.jsglr.treediff;

import java.util.ArrayList;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.terms.attachments.DesugaredOriginAttachment;
import org.spoofax.terms.attachments.OriginAttachment;

/** Constructs a symmetic (partial) matching between terms of AST1 and AST2
 * based on the origin relation, e.g., M(trm1,trm2) iff trm1 is the origin of trm2 
 * @author maartje
 *
 */
public class OriginTreeMatcher extends AbstractTreeMatcher {
	
	private boolean useDesugaredOrigins;
	private boolean requireSameSignature;
	private boolean requireSameValue;
	
	public OriginTreeMatcher(boolean useDesugaredOrigins, boolean requireSameSignature, boolean requireSameValue){
		super(new LCSOriginTermsCommand(), true);
		this.useDesugaredOrigins = useDesugaredOrigins;
	}

	@Override
	ArrayList<IStrategoTerm> getCandidateMatchTerms(IStrategoTerm root1, IStrategoTerm t2) {
		ArrayList<IStrategoTerm> candidates = new ArrayList<IStrategoTerm>();
		if(useDesugaredOrigins){
			IStrategoTerm desugaredOrigin = DesugaredOriginAttachment.getDesugaredOrigin(t2);
			if(desugaredOrigin != null)
				candidates.add(desugaredOrigin);			
		}
		IStrategoTerm origin = OriginAttachment.getOrigin(t2);
		if(origin != null)
			candidates.add(origin);
		return candidates;
	}

	@Override
	double matchingScore(IStrategoTerm t1, IStrategoTerm t2) {
		//same signature?
		//desugared, normal origin?
		if(t1 == null || t2 == null)
			return -1;
		if(requireSameSignature && !HelperFunctions.haveSameSignature(t1, t2)){
			return -1;
		}
		if(requireSameValue && HelperFunctions.isPrimitiveType(t1) && !HelperFunctions.isPrimitiveWithSameValue(t1, t2)){
			return -1;
		}
		if(OriginAttachment.getOrigin(t2) != t1 && DesugaredOriginAttachment.getDesugaredOrigin(t2) != t1){
			return -1; //no origin relation
		}
		double value = 0.0;
		double maxValue = 2.0 + 2.0 + 1.0;
		if(HelperFunctions.haveSameSignature(t1, t2) || HelperFunctions.isPrimitiveWithSameValue(t1, t2)){
			value += 2; //+2 for equal signatures
			if(t1.equals(t2)){
				value +=2; //+2 for equal terms
			}
		}
		if(DesugaredOriginAttachment.getDesugaredOrigin(t2)==t1)
			value +=1; //prefer desugared origin
		return 1.0 * value/maxValue;
	}

}
