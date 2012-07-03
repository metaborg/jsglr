package org.spoofax.interpreter.library.jsglr.treediff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.spoofax.interpreter.terms.IStrategoTerm;

/**
 * Implements a Longest Common Subsequence algorithm
 * @author maartje
 *
 * @param <T>
 */
public class LCS<T> {
	
	private List<T> elems1;
	private List<T> elems2;
	private ArrayList<T> resultLCS1;
	private ArrayList<T> resultLCS2;
	private ArrayList<T> resultUnmatched1;
	private ArrayList<T> resultUnmatched2;
	private LCSCommand<T> lcsCommand;
	
	/**
	 * LCS result for input 1
	 * (empty before algorithm is applied)
	 * @return LCS elements for input 1
	 */
	public ArrayList<T> getResultLCS1() {
		return resultLCS1;
	}

	/**
	 * LCS result for input 2
	 * (empty before algorithm is applied)
	 * @return LCS elements for input 2
	 */
	public ArrayList<T> getResultLCS2() {
		return resultLCS2;
	}

	/**
	 * Elements of input 1 that are not part of the LCS
	 * (empty before algorithm is applied)
	 * @return non-LCS elements for input 1
	 */
	public ArrayList<T> getResultUnmatched1() {
		return resultUnmatched1;
	}

	/**
	 * Elements of input 2 that are not part of the LCS
	 * (empty before algorithm is applied)
	 * @return non-LCS elements for input 2
	 */
	public ArrayList<T> getResultUnmatched2() {
		return resultUnmatched2;
	}

	/**
	 * LCS command implements the function that says wether or not two elements can be matched
	 * @param lcsCommand Implements the matching criterion
	 */
	public void setLcsCommand(LCSCommand<T> lcsCommand) {
		this.lcsCommand = lcsCommand;
	}

	/**
	 * Finds the Longest Common Subsequence of two lists with elements
	 * @param lcsCommand Implements the matching criterion
	 */
	public LCS(LCSCommand<T> lcsCommand){
		elems1 = new ArrayList<T>();
		elems2 = new ArrayList<T>();
		resultLCS1 = new ArrayList<T>();
		resultLCS2 = new ArrayList<T>();
		resultUnmatched1 = new ArrayList<T>();
		resultUnmatched2 = new ArrayList<T>();
		this.lcsCommand = lcsCommand;
	}
	
	/**
	 * Size of the LCS
	 * @return Size of the LCS
	 */
	public int getLCSSize(){
		return resultLCS1.size();
	}
	
	private void clearResults(){
		resultLCS1.clear();
		resultLCS2.clear();
		resultUnmatched1.clear();
		resultUnmatched2.clear();
	}
	
	/**
	 * Fills the LCS results: LCS (longest common subsequence), unmatched elems1, unmatched elems2.
	 * As an optimization, the algorithm starts with matching elements at the prefix and suffix.
	 * @param elems1
	 * @param elems2
	 */
	public LCS<T> createLCSResultsOptimized(List<T> elems1, List<T> elems2) {
		clearResults();
		this.elems1 = elems1;
		this.elems2 = elems2;
		int minLength = Math.min(elems1.size(), elems2.size()); 
		int commonPrefixLength = 0;
		while (commonPrefixLength < minLength) {
			T el1 = elems1.get(commonPrefixLength);
			T el2 = elems2.get(commonPrefixLength);
			if(lcsCommand.isMatch(el1, el2)){
				addToLCS(el1, el2);
				commonPrefixLength++;
			}
			else
				break;
		}		
		int commonSuffixLength = 0;
		while (commonSuffixLength < minLength - commonPrefixLength) {
			T el1 = elems1.get(elems1.size() - commonSuffixLength -1);
			T el2 = elems2.get(elems2.size() - commonSuffixLength - 1);
			if(lcsCommand.isMatch(el1, el2)){
				addToLCS(el1, el2);
				commonSuffixLength++;
			}
			else
				break;
		}
		lcs(
			elems1.subList(commonPrefixLength, elems1.size() - commonSuffixLength), 
			elems2.subList(commonPrefixLength, elems2.size() - commonSuffixLength)
		);
		checkAssertions(elems1, elems2);
		return this;
	}

	/**
	 * Fills the LCS results: LCS (longest common subsequence), unmatched elems1, unmatched elems2.
	 * @param elems1
	 * @param elems2
	 */
	public LCS<T> createLCSResults(List<T> elems1, List<T> elems2) {
		clearResults();
		this.elems1 = elems1;
		this.elems2 = elems2;
		lcs(elems1, elems2);
		checkAssertions(elems1, elems2);
		return this;
	}

	private void lcs(List<T> elems1, List<T> elems2) {
		int lengthElems1 = elems1.size();
        int lengthElems2 = elems2.size();

        // opt[i][j] = length of LCS of x[i..M] and y[j..N]
        int[][] opt = new int[lengthElems1+1][lengthElems2+1];

        // compute length of LCS and all subproblems via dynamic programming
        for (int i = lengthElems1-1; i >= 0; i--) {
            for (int j = lengthElems2-1; j >= 0; j--) {
                if (lcsCommand.isMatch(elems1.get(i), elems2.get(j)))
                    opt[i][j] = opt[i+1][j+1] + 1;
                else 
                    opt[i][j] = Math.max(opt[i+1][j], opt[i][j+1]);
            }
        }

        // recover LCS itself
        int i = 0, j = 0;
		while(i < lengthElems1 && j < lengthElems2) {
	        T el1 = elems1.get(i);
			T el2 = elems2.get(j);
            if (lcsCommand.isMatch(el1, el2)) {
				addToLCS(el1, el2);
                i++;
                j++;
            }
            else if (opt[i+1][j] >= opt[i][j+1]){
            	resultUnmatched1.add(el1);
            	i++;
            }
            else { 
            	resultUnmatched2.add(el2);
            	j++;
            }
        }
		while (i < lengthElems1) {
        	resultUnmatched1.add(elems1.get(i));
			i++;
		}
		while (j < lengthElems2) {
        	resultUnmatched2.add(elems2.get(j));
			j++;
		}
	}

	private void addToLCS(T el1, T el2) {
		resultLCS1.add(el1);
		resultLCS2.add(el2);
	}
	
	private void checkAssertions(List<T> elems1, List<T> elems2) {
		assert resultLCS1.size() == resultLCS2.size();
		assert resultLCS1.size() + resultUnmatched1.size() == elems1.size();
		assert resultLCS2.size() + resultUnmatched2.size() == elems2.size();
	}

}
