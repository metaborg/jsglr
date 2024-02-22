package org.spoofax.interpreter.library.jsglr.treediff;

import static mb.jsglr.shared.ImploderAttachment.getLeftToken;
import static mb.jsglr.shared.ImploderAttachment.getRightToken;
import static mb.jsglr.shared.ImploderAttachment.getTokenizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.spoofax.interpreter.core.Tools;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.TermTreeFactory;
import org.spoofax.terms.StrategoAppl;
import org.spoofax.terms.TermFactory;
import org.spoofax.terms.attachments.ParentAttachment;

/**
 * Simple AST comparison module that collects primitive edit operations
 * required to transform AST1 into AST2.
 * @author maartje
 *
 */
public class TreeEditDistance {

	private TermTreeFactory termFactory = new TermTreeFactory(new TermFactory());
	
	private ArrayList<IStrategoTerm> deletions;
	private ArrayList<IStrategoTerm> insertions;
	private ArrayList<IStrategoTerm> labelChanges;
	private ArrayList<IStrategoTerm> valueChanges;
	private ArrayList<IStrategoTerm> movings;
	
	public int getDeletionCount() {
		return deletions.size();
	}

	public int getInsertionCount() {
		return insertions.size();
	}

	public int getRelabeledCount() {
		return labelChanges.size();
	}

	public int getValueChangeCount() {
		return valueChanges.size();
	}

	public int getMovedCount() {
		return movings.size();
	}

	
	public TreeEditDistance(){
		deletions = new ArrayList<IStrategoTerm>();
		insertions = new ArrayList<IStrategoTerm>();
		labelChanges = new ArrayList<IStrategoTerm>();
		valueChanges = new ArrayList<IStrategoTerm>();
		movings = new ArrayList<IStrategoTerm>();
	}
	
	private void clear(){
		deletions.clear();
		insertions.clear();
		labelChanges.clear();
		valueChanges.clear();
		movings.clear();
	}

	public void detectTreeEditActions(IStrategoTerm root1, IStrategoTerm root2, AbstractTreeMatcher treeMatcher) {
		clear();
		treeMatcher.constructMatching(root1, root2);
		insertions = collectUnmatchedTerms(root2);
		deletions = collectUnmatchedTerms(root1);
		labelChanges = collectRelabeledTerms(root1);
		valueChanges = collectValueChanges(root1);
		movings = collectMovedTerms(root1);
		
		printResult(insertions, "insertions");
		printResult(deletions, "deletions");
		printResult(labelChanges, "labelChanges");
		printResult(valueChanges, "valueChanges");
		printResult(movings, "movings");
		
		System.out.println("--------------------------------");
		
		assert labelChanges.size() == collectRelabeledTerms(root1).size();
		assert valueChanges.size() == collectValueChanges(root2).size();
		assert movings.size() == collectMovedTerms(root2).size(): movings.size() + " moved terms in root1 NOT EQUAL TO " + collectMovedTerms(root2).size() + " in root2";
		for (IStrategoTerm trm : insertions) {
			assert ParentAttachment.getRoot(trm).equals(root2);
		}
		for (IStrategoTerm trm : deletions) {
			assert ParentAttachment.getRoot(trm) == root1;
		}
		for (IStrategoTerm trm : labelChanges) {
			assert ParentAttachment.getRoot(trm) == root1;
		}
		for (IStrategoTerm trm : valueChanges) {
			assert ParentAttachment.getRoot(trm) == root1;
		}
		for (IStrategoTerm trm : movings) {
			assert ParentAttachment.getRoot(trm) == root1;
		}



	}

	private void printResult(ArrayList<IStrategoTerm> editActions, String actionString) {
		for (int i = 0; i < editActions.size(); i++) {
			IStrategoTerm m = editActions.get(i);
			assert m != null: actionString;
			String txt = getTokenizer(m).toString(getLeftToken(m), getRightToken(m));		
			System.out.println(actionString + ": " + txt + " => " + m);
		}
	}

	private ArrayList<IStrategoTerm> collectMovedTerms(IStrategoTerm trm) {
		//System.out.println("M: ");
		//System.out.println("  - " + trm );
		//System.out.println("  - " + TermMatchAttachment.getMatchedTerm(trm));
		ArrayList<IStrategoTerm> movedTerms = new ArrayList<IStrategoTerm>();
		IStrategoTerm partnerOfTrm = TermMatchAttachment.getMatchedTerm(trm);
		if(partnerOfTrm != null){
			movedTerms.addAll(movedSubterms(trm, partnerOfTrm));
		}
		else {
			for (int i = 0; i < trm.getSubtermCount(); i++) {
				if(TermMatchAttachment.hasMatchedTerm(trm.getSubterm(i)))
					movedTerms.add(trm.getSubterm(i));			
			}
		}
		for (int i = 0; i < trm.getSubtermCount(); i++) {
			movedTerms.addAll(collectMovedTerms(trm.getSubterm(i)));
		}
		return movedTerms;
	}

	private ArrayList<IStrategoTerm> movedSubterms(IStrategoTerm trm, IStrategoTerm partnerOfTrm) {
		List<IStrategoTerm> subterms = Arrays.asList(trm.getAllSubterms());
		List<IStrategoTerm> subtermsOfPartner = Arrays.asList(partnerOfTrm.getAllSubterms());
		if(Tools.isTermAppl(trm) && Tools.isTermAppl(partnerOfTrm) && HelperFunctions.haveSameSignature(trm, partnerOfTrm))
			return movedApplElements(subterms, subtermsOfPartner);
		else 
			return movedListElements(subterms, subtermsOfPartner);
	}

	private ArrayList<IStrategoTerm> movedApplElements(List<IStrategoTerm> subterms, List<IStrategoTerm> subtermsOfPartner) {
		assert subterms.size() == subtermsOfPartner.size();
		ArrayList<IStrategoTerm> moved = new ArrayList<IStrategoTerm>();
		for (int i = 0; i < subterms.size(); i++) {
			IStrategoTerm partner_i = TermMatchAttachment.getMatchedTerm(subterms.get(i));
			if(partner_i != null && partner_i != subtermsOfPartner.get(i))
				moved.add(subterms.get(i));
		}
		return moved;
	}

	private ArrayList<IStrategoTerm> movedListElements(List<IStrategoTerm> subterms, List<IStrategoTerm> subtermsOfPartner) {
		ArrayList<IStrategoTerm> moved = new ArrayList<IStrategoTerm>();
		LCS<IStrategoTerm> lcs = new LCS<IStrategoTerm>(new LCSCommand<IStrategoTerm>() {
			public boolean isMatch(IStrategoTerm t1, IStrategoTerm t2) {
				return TermMatchAttachment.getMatchedTerm(t2) == t1;
			}
		});
		lcs.createLCSResults(subterms, subtermsOfPartner);
		ArrayList<IStrategoTerm> unmatched1 = lcs.getResultUnmatched1();
		for (IStrategoTerm u1 : unmatched1) {
			if(TermMatchAttachment.hasMatchedTerm(u1)){
				moved.add(u1);
			}
		}
		return moved;
	}

	private ArrayList<IStrategoTerm> collectValueChanges(IStrategoTerm trm) {
		ArrayList<IStrategoTerm> leafnodes = HelperFunctions.collectLeafnodes(trm);
		ArrayList<IStrategoTerm> valueChanges = new ArrayList<IStrategoTerm>();
		for (IStrategoTerm ln : leafnodes) {
			IStrategoTerm lnPartner = TermMatchAttachment.getMatchedTerm(ln);
			if(HelperFunctions.isPrimitiveWithDifferentValues(ln, lnPartner)){
				valueChanges.add(ln);
			}
		}
		return valueChanges;
	}

	private ArrayList<IStrategoTerm> collectRelabeledTerms(IStrategoTerm trm) {
		ArrayList<IStrategoTerm> relabeledTerms = new ArrayList<IStrategoTerm>(); 
		if(Tools.isTermAppl(trm) || Tools.isTermList(trm) || Tools.isTermTuple(trm)){
			IStrategoTerm partnerOfTrm = TermMatchAttachment.getMatchedTerm(trm);
			if(partnerOfTrm != null && !HelperFunctions.haveSameSignature(trm, partnerOfTrm) && Tools.isTermAppl(trm) && Tools.isTermAppl(partnerOfTrm)){
				List<IStrategoTerm> tupleElems = new ArrayList<IStrategoTerm>();
				tupleElems.add(trm);
				tupleElems.add(((StrategoAppl)partnerOfTrm).getConstructor());
				IStrategoTerm relabeling = termFactory.createTuple(null, null, null, tupleElems);
				//relabeledTerms.add(relabeling);
				relabeledTerms.add(trm);
				System.err.println(trm);
				System.err.println(partnerOfTrm);
			}
		}
		for (int i = 0; i < trm.getSubtermCount(); i++) {
			relabeledTerms.addAll(collectRelabeledTerms(trm.getSubterm(i)));
		}
		return relabeledTerms;
	}

	private ArrayList<IStrategoTerm> collectUnmatchedTerms(IStrategoTerm trm) {
		ArrayList<IStrategoTerm> unmatchedTerms = new ArrayList<IStrategoTerm>(); 
		if(TermMatchAttachment.getMatchedTerm(trm) == null){
			unmatchedTerms.add(trm);
		}
		for (int i = 0; i < trm.getSubtermCount(); i++) {
			unmatchedTerms.addAll(collectUnmatchedTerms(trm.getSubterm(i)));
		}
		return unmatchedTerms;
	}
}
