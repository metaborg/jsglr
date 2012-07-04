package org.spoofax.interpreter.library.jsglr.treediff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.spoofax.interpreter.core.Tools;
import org.spoofax.interpreter.terms.IStrategoTerm;


public class TreeEditDistance {
	
	public static int calculateTreeEditDistance(IStrategoTerm root1, IStrategoTerm root2, AbstractTreeMatcher treeMatcher) {
		treeMatcher.constructMatching(root1, root2);
		int deletionCount = numberOfUnmatchedTerms(root1);
		int insertionCount = numberOfUnmatchedTerms(root2);
		int relabeledCount = numberOfRelabeledTerms(root2);
		int valueChangeCount = numberOfValueChangedTerms(root2);
		int movedCount = numberOfMovedTerms(root2);
		return deletionCount + insertionCount + relabeledCount + valueChangeCount + movedCount;
	}

	private static int numberOfMovedTerms(IStrategoTerm trm) {
		int nrOfMoved = 0;
		IStrategoTerm partnerOfTrm = TermMatchAttachment.getMatchedTerm(trm);
		List<IStrategoTerm> subterms = Arrays.asList(trm.getAllSubterms());
		List<IStrategoTerm> subtermsOfPartner = Arrays.asList(partnerOfTrm.getAllSubterms());
		LCS<IStrategoTerm> lcs = new LCS<IStrategoTerm>(new LCSCommand<IStrategoTerm>() {
			public boolean isMatch(IStrategoTerm t1, IStrategoTerm t2) {
				return TermMatchAttachment.getMatchedTerm(t2) == t1;
			}
		});
		lcs.createLCSResults(subterms, subtermsOfPartner);
		ArrayList<IStrategoTerm> unmatched1 = lcs.getResultUnmatched2();
		for (IStrategoTerm u1 : unmatched1) {
			if(TermMatchAttachment.getMatchedTerm(u1) != null){
				nrOfMoved += 1;
			}
		}
		for (int i = 0; i < trm.getSubtermCount(); i++) {
			nrOfMoved += numberOfMovedTerms(trm.getSubterm(i));
		}
		return nrOfMoved;
	}

	private static int numberOfValueChangedTerms(IStrategoTerm trm) {
		ArrayList<IStrategoTerm> leafnodes = HelperFunctions.collectLeafnodes(trm);
		int valueChanges = 0;
		for (IStrategoTerm ln : leafnodes) {
			IStrategoTerm lnPartner = TermMatchAttachment.getMatchedTerm(ln);
			if(HelperFunctions.isPrimitiveWithDifferentValues(ln, lnPartner)){
				valueChanges += 1;
			}
		}
		return valueChanges;
	}

	private static int numberOfRelabeledTerms(IStrategoTerm trm) {
		int nrOfRelabeled = 0;
		if(Tools.isTermAppl(trm) || Tools.isTermList(trm) || Tools.isTermTuple(trm)){
			IStrategoTerm partnerOfTrm = TermMatchAttachment.getMatchedTerm(trm);
			if(partnerOfTrm != null && !HelperFunctions.haveSameSignature(trm, partnerOfTrm))
				nrOfRelabeled +=1;
		}
		for (int i = 0; i < trm.getSubtermCount(); i++) {
			nrOfRelabeled += numberOfRelabeledTerms(trm.getSubterm(i));
		}
		return nrOfRelabeled;
	}

	private static int numberOfUnmatchedTerms(IStrategoTerm trm) {
		int nrOfUnmatched = 0;
		if(TermMatchAttachment.getMatchedTerm(trm) == null){
			nrOfUnmatched +=1;
		}
		for (int i = 0; i < trm.getSubtermCount(); i++) {
			nrOfUnmatched += numberOfUnmatchedTerms(trm.getSubterm(i));
		}
		return nrOfUnmatched;
	}
}
