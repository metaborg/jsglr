package org.spoofax.jsglr.client.editregion.detection;

/**
 * Implements the matching criteron applied by the LCS algorithm
 * @author maartje
 *
 * @param <T>
 */
public interface LCSCommand<T> {
	/**
	 * Returns true in case t1 and t2 can be matched.
	 * This function implements the criteria for matching in a Longest Common Subsequence procedure. 
	 * @param t1 element in input list 1
	 * @param t2 element in input list 2
	 * @return True in case t1 and t2 can be matched
	 */
	abstract boolean isMatch(T t1, T t2);

}
