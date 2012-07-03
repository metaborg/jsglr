package org.spoofax.interpreter.library.jsglr.treediff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.spoofax.interpreter.core.Tools;
import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.ImploderAttachment;
import org.spoofax.jsglr.client.imploder.TermTreeFactory;
import org.spoofax.terms.StrategoInt;
import org.spoofax.terms.StrategoReal;
import org.spoofax.terms.StrategoString;
import org.spoofax.terms.TermFactory;
import org.spoofax.terms.attachments.ParentAttachment;
import org.spoofax.terms.attachments.ParentTermFactory;

/**
 * Creates a (minimal) edit script that changes AST1 into AST2
 * The supported edit operations are:
 * - UpdateValue: updates the value of a primitive leaf node
 * - UpdateLabel: updates the constructor name (for example: Lt => Gt)
 * - Insert: Inserts a Node of a certain signature (without any subterms)
 * - Move: Moves a node from one position in the tree to another position
 * - Delete: Deletes a node
 * The implementation is based on, 
 * title: "Change Detection in Hierarchically Structured Information"
 * authors: Sudarshan S. Chawathe, Anand Rajaraman, Hector Garcia-Molina, and Jennifer Widom
 * @author maartje
 *
 */
public class EditScriptCreator {
	private static TermTreeFactory parentFactory = new TermTreeFactory(new ParentTermFactory(new TermFactory()));
	
	public static void generateEditScript(IStrategoTerm root1, IStrategoTerm root2, AbstractTreeMatcher treeMatcher) {
		treeMatcher.constructMatching(root1, root2);
		ensureMatchingRootTerms(root1, root2);
		updateLeafnodeValues(root2);
		updateLabels(root2);
		topdownAlignSubterms(root2);
		IStrategoTerm dummyRoot2 = ParentAttachment.getParent(root2);
		if (dummyRoot2 != null) 
			insertTerms(dummyRoot2);
		else 
			insertTerms(root2); 			
		moveTerms(root2);
		deleteTerms(root1);
		//assert root1.equals(root2): "root1 should be isomorphic to root2 after the edit script is applied";
	}

	/**
	 * Relabels all nodes in root1 that are matched with a node in root2 that has a different label.
	 * example: Gt(a,b) in root1 and Lt'(a',b') in root2, relabel Gt => Lt.
	 * @param root2
	 */
	private static  void updateLabels(IStrategoTerm trm2) {
		IStrategoTerm trm1 = TermMatchAttachment.getMatchedTerm(trm2);
		if(trm1 != null){
			if(
				Tools.isTermAppl(trm2) && 
				!(Tools.isTermAppl(trm1) && HelperFunctions.haveSameConstructorName(trm1, trm2)))
			{
				System.out.println("RELABELED: (" + trm1 +", " + ((IStrategoAppl)trm2).getName() + ")");
				IStrategoTerm newTrm1 = parentFactory.recreateNode(trm2, null, null, Arrays.asList(trm1.getAllSubterms()));	
				TermMatchAttachment.forceMatchTerms(newTrm1, trm2);
				//TODO: change tree? if not then deletion phase gives problems
			}
			if(Tools.isTermList(trm2) && !(Tools.isTermList(trm1))){
				//TODO
				System.out.println("RELABELED: (" + trm1 +", " + trm2 + ")");								
			}
			if(Tools.isTermTuple(trm2) && !(Tools.isTermTuple(trm1))){
				//TODO
				System.out.println("RELABELED: (" + trm1 +", " + trm2 + ")");								
			}
		}
		for (int i = 0; i < trm2.getSubtermCount(); i++) {
			updateLabels(trm2.getSubterm(i));
		}
	}

	/**
	 * Deletes all unmatched nodes bottom-up, e.g. only nodes without child nodes can be deleted
	 * 
	 * @param root1
	 */
	private static void deleteTerms(IStrategoTerm root1) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Moves terms in t1 to their right parent.
	 * Let t2 a term in root2 that is matched with t1 in root1, e.g. M(t1,t2).
	 * Let p2 the parent of t2 that is matched with p1 a term in root1, e.g. M(p1,p2).
	 * Suppose that p1 is NOT the parent of t1.
	 * Then, (in T1) we move t1 to p1 and add the edit item Move( ...)    
	 * @param root2
	 */
	private static void moveTerms(IStrategoTerm root2) {
		//assert: all terms are matched
		//TODO
		//assert
	}

	/**
	 * When root1 and root2 are not matched, artificial root nodes are inserted that are matched.
	 * Thus, r1 => (r1) and r2 => (r2) whereby the single tuple terms are matched.
	 * @param root1
	 * @param root2
	 */
	private static void ensureMatchingRootTerms(IStrategoTerm root1, IStrategoTerm root2) {
		if(TermMatchAttachment.getMatchedTerm(root1) != root2){
			//inserts artificial dummy roots that are matched.
			IStrategoTerm dummyRoot1 = addDummyRoot(root1);
			IStrategoTerm dummyRoot2 = addDummyRoot(root2);
			TermMatchAttachment.forceMatchTerms(dummyRoot1, dummyRoot2);
		}
	}

	/**
	 * Inserts nodes in root1 that form pairs with unmatched nodes in root2.
	 * At the end, all terms in root2 have a partner in root1.
	 * Thus: Let t2 a term in root2 that is not matched, and let p2 its parent matched with p1 (in root1).
	 * Then, we insert a new node with the label of t2 as the ...th child of p1. 
	 * @param parent2
	 */
	private static void insertTerms(IStrategoTerm parent2) {
		assert TermMatchAttachment.hasMatchedTerm(parent2); 
		IStrategoTerm parent1 = TermMatchAttachment.getMatchedTerm(parent2);
		//System.out.println("parent2: " + parent2);
		//System.out.println("parent1: " + parent1);
		int matchChildIndex = 0;
		for (int i = 0; i < parent2.getSubtermCount(); i++) {
			IStrategoTerm child2_i = parent2.getSubterm(i);
			IStrategoTerm child1_i = TermMatchAttachment.getMatchedTerm(child2_i);
			if(child1_i != null){
				if(ParentAttachment.getParent(child1_i) == parent1){
					matchChildIndex +=1;
				}
			}
			else{
				//TODO: Insert and EditScript! 
				IStrategoTerm insertedChild1_i = parentFactory.recreateNode(child2_i, null, null, new ArrayList<IStrategoTerm>());	
				TermMatchAttachment.forceMatchTerms(insertedChild1_i, child2_i);
				System.out.println("INSERTED: (" + insertedChild1_i +", " + parent1 + ", " + matchChildIndex + ")");
			}
			insertTerms(child2_i);
		}
		//assert: all terms in root2 are matched
	}

	private static IStrategoTerm addDummyRoot(IStrategoTerm root) {
		ArrayList<IStrategoTerm> rootAsList = new ArrayList<IStrategoTerm>();
		rootAsList.add(root);
		IStrategoTerm dummyRoot = parentFactory.createTuple(
			null, ImploderAttachment.getLeftToken(root), ImploderAttachment.getRightToken(root), rootAsList
		);
		ParentAttachment.putParent(root, dummyRoot, null);
		return dummyRoot;
	}

	/**
	 * Aligns subterms of terms in root1 one by one, until they correspond with the order of their matches.
	 * Thus, Let p1 a node in root1 that matches with a node p2 in root2,
	 * and let (a,b,c,d,e) (c',a', d',x,b') their subterms. 
	 * Then this function aligns the subterms of p1 to (c, a, d, b)
	 * and adds the edit items Move(c, c, treepos-old(c), treepos-new(c)) and Move(d, d, treepos-old(d), treepos-new(d))
	 * @param root1
	 */
	private static void topdownAlignSubterms(IStrategoTerm term2) {
		alignSubterms(term2);
		for (int i = 0; i < term2.getSubtermCount(); i++) {
			topdownAlignSubterms(term2.getSubterm(i));
		}
	}

	private static void alignSubterms(IStrategoTerm term2) {
		IStrategoTerm term1 = TermMatchAttachment.getMatchedTerm(term2);
		if(term1 != null){
			LCS<IStrategoTerm> lcs = new LCS<IStrategoTerm>(new LCSCommand<IStrategoTerm>() {
				public boolean isMatch(IStrategoTerm t1, IStrategoTerm t2) {
					return TermMatchAttachment.getMatchedTerm(t2) == t1;
				}
			});
			List<IStrategoTerm> subterms1 = Arrays.asList(term1.getAllSubterms());
			List<IStrategoTerm> subterms2 = Arrays.asList(term2.getAllSubterms());
			lcs.createLCSResults(subterms1, subterms2);
			for (int i = 0; i < subterms2.size(); i++) {
				//alignement for subterms2.get(i) by relocating its partner in subterms1
				alignSingleSubterm(lcs, subterms1, subterms2, subterms2.get(i)); 
			}
			assertFullAlignment(lcs, subterms1, subterms2);
		}
	}

	private static void alignSingleSubterm(LCS<IStrategoTerm> lcs,List<IStrategoTerm> elems1, List<IStrategoTerm> elems2, IStrategoTerm el2) {
		IStrategoTerm child_i_1 = TermMatchAttachment.getMatchedTerm(el2);
		if(lcs.getResultUnmatched1().contains(child_i_1)){ //child_i_1 and child_i_1 can be matched by aligning them. 
			assert lcs.getResultUnmatched2().contains(el2): "M(el1,el2), then both elements are in LCS or neither."; 
			int indexFrom = elems1.indexOf(child_i_1);
			//alignement of child_i_1 so that M(child_i_1, child_i_2) in LCS.
			IStrategoTerm preceedingLCS2Elem = getPreceedingLCSElem(el2, elems2, lcs.getResultLCS2());
			IStrategoTerm preceedingLCS1Elem = null;
			int indexTo = 0;
			if(preceedingLCS2Elem != null){
				preceedingLCS1Elem = TermMatchAttachment.getMatchedTerm(preceedingLCS2Elem);
				indexTo = elems1.indexOf(preceedingLCS1Elem) + 1;
				assert(preceedingLCS1Elem != null);
				assert(indexTo >= 0);
			}
			assert indexFrom != indexTo: "Terms are already aligned, so they should be in LCS.";
			if(indexFrom > indexTo){
				elems1.remove(indexFrom);
				elems1.add(indexTo, child_i_1);
			}
			else{
				elems1.add(indexTo, child_i_1);						
				elems1.remove(indexFrom);
			}
			System.out.println("Align: " + child_i_1 + ", "+ indexFrom + " => " + indexTo);
		}
	}

	private static void assertFullAlignment(LCS<IStrategoTerm> lcs,
			List<IStrategoTerm> subterms1, List<IStrategoTerm> subterms2) {
		lcs.createLCSResults(subterms1, subterms2);
		for (IStrategoTerm noLCSElem2 : lcs.getResultUnmatched2()) {
			IStrategoTerm noLCSElem1 = TermMatchAttachment.getMatchedTerm(noLCSElem2);
			assert !lcs.getResultUnmatched1().contains(noLCSElem1):
				"LCS could be improved by aligning M(noLCSElem1, noLCSElem2)";
		}
	}

	private static IStrategoTerm getPreceedingLCSElem(IStrategoTerm el, List<IStrategoTerm> elems, List<IStrategoTerm> lcsElems) {
		int elIndex = elems.indexOf(el);
		for (int j = elIndex - 1; j >= 0; j--) {
			if(lcsElems.contains(elems.get(j))){
				return elems.get(j);
			}
		}
		return null;
	}
	

	/**
	 * Updates StringTerms and IntTerms that are matched but do not have the same value.
	 * Thus: Let s1 a String leafnode in root1, and let s2 a String leafnode in root2 so that
	 * s1 and s2 are matched and s1 and s2 have different values.
	 * Then, this function updates the value of s1 to the value of s2.
	 * In addition, an edit item UpdateValue("value1", "value2", treeposition1, treeposition1) is added. 
	 * @param root1
	 */
	private static void updateLeafnodeValues(IStrategoTerm root2) {
		ArrayList<IStrategoTerm> leafnodes2 = HelperFunctions.collectLeafnodes(root2);
		for (int i = 0; i < leafnodes2.size(); i++) {
			IStrategoTerm ln2 = leafnodes2.get(i); 
			updateSingleLeafnodeValue(ln2);
		}
		assertSameValueForMatchedPrimitives(root2);
	}

	private static void assertSameValueForMatchedPrimitives(IStrategoTerm root2) {
		ArrayList<IStrategoTerm> leafnodes2 = HelperFunctions.collectLeafnodes(root2);
		for (int i = 0; i < HelperFunctions.collectLeafnodes(root2).size(); i++) {
			IStrategoTerm ln2 = leafnodes2.get(i); 
			IStrategoTerm ln1 = TermMatchAttachment.getMatchedTerm(ln2);
			assert ln1 == null || !HelperFunctions.isPrimitiveType(ln1) || ln1.equals(ln2) : 
				"values should be equal for matched primitve nodes";
		}
	}

	private static void updateSingleLeafnodeValue(IStrategoTerm ln2) {
		IStrategoTerm ln1 = TermMatchAttachment.getMatchedTerm(ln2);
		if(ln1 != null && !ln2.equals(ln1)){
			IStrategoTerm p = ParentAttachment.getParent(ln1);
			if(Tools.isTermInt(ln1) && Tools.isTermInt(ln2)){
				System.out.println("Update Int Value: " + ln1 + " => " + ln2);
				ln1 = parentFactory.createIntTerminal(null, ImploderAttachment.getLeftToken(ln1), ((StrategoInt)ln2).intValue());
			}
			else if(Tools.isTermString(ln1) && Tools.isTermString(ln2)){
				System.out.println("Update String Value: " + ln1 + " => " + ln2);
				ln1 = parentFactory.createStringTerminal(null, ImploderAttachment.getLeftToken(ln1), ImploderAttachment.getRightToken(ln1), ((StrategoString)ln2).stringValue());
			}
			else if(Tools.isTermReal(ln1) && Tools.isTermReal(ln2)){
				System.out.println("Update Real Value: " + ln1 + " => " + ln2);
				ln1 = parentFactory.createRealTerminal(null, ImploderAttachment.getLeftToken(ln1), ((StrategoReal)ln2).realValue());
			}
		} 
	}
}
