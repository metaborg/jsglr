package org.spoofax.jsglr.client.editregion.detection;

import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getSort;

import java.util.List;

import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoTerm;

/**
 * Helper functions
 * @author maartje
 */
public class HelperFunctions {
	
	/**
	 * Returns true if this collection contains the specified element. 
	 * More formally, returns true if and only if this collection contains at least one element e 
	 * such that (o==null ? e==null : elem == e).
	 * @param lst List
	 * @param elem Element
	 * @return true iff for some element E in List, E == elem 
	 */
	public static <T> boolean contains(List<T> lst, T elem) {
		for (T lstElem : lst) {
			if(lstElem == elem){
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the sort or, for lists and list elements, the Elem* sort associated to the (parent)list 
	 * @param term
	 * @param parent
	 * @return
	 */
	public static String getGeneralSort(IStrategoTerm term, IStrategoTerm parent) {
		String termSort = getSort(term);
		if(parent != null){
			if(parent.isList())
				termSort = getSort(parent);
			assert (term.isList() || parent.isList())? termSort.endsWith("*") : !termSort.endsWith("*");
		}
		return termSort;
	}
	
	/**
	 * Returns true iff the sort represents a list sort, e.g., Elem*
	 * @param sort
	 * @return
	 */
	public static boolean isListSort(String sort) {
		return sort.endsWith("*");
	}

	/**
	 * Says wether the given node is an optional Some(_) sort.
	 * @param trm
	 * @return
	 */
	public static boolean isSomeNode(IStrategoTerm trm) {
		if(trm.getTermType() == IStrategoTerm.APPL){
			return trm.getSubtermCount() == 1 && ((IStrategoAppl)trm).getConstructor().getName().equals("Some");
		}
		return false;
	}

}
