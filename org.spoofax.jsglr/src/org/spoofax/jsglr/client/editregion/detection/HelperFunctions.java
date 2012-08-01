package org.spoofax.jsglr.client.editregion.detection;

import java.util.List;

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
}
