package org.spoofax.interpreter.library.jsglr.treediff;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements a Longest Common Subsequence algorithm
 * @author maartje
 *
 * @param <T>
 */
public class LCS<T> {
	
	private LCSCommand<T> lcsCommand;
	private List<T> elems1;
	private List<T> elems2;
	private ArrayList<Integer> matchedIndices1;
	private ArrayList<Integer> matchedIndices2;

	public ArrayList<Integer> getMatchedIndices1() {
		return matchedIndices1;
	}

	public ArrayList<Integer> getMatchedIndices2() {
		return matchedIndices2;
	}

	public ArrayList<Integer> getUnMatchedIndices1() {
		return getUnmatchedIndices(elems1, matchedIndices1);
	}

	public ArrayList<Integer> getUnMatchedIndices2() {
		return getUnmatchedIndices(elems2, matchedIndices2);
	}

	/**
	 * LCS result for input 1
	 * (empty before algorithm is applied)
	 * @return LCS elements for input 1
	 */
	public ArrayList<T> getResultLCS1() {
		return getIncludedElems(elems1, matchedIndices1);
	}

	/**
	 * LCS result for input 2
	 * (empty before algorithm is applied)
	 * @return LCS elements for input 2
	 */
	public ArrayList<T> getResultLCS2() {
		return getIncludedElems(elems2, matchedIndices2);
	}

	/**
	 * Elements of input 1 that are not part of the LCS
	 * (empty before algorithm is applied)
	 * @return non-LCS elements for input 1
	 */
	public ArrayList<T> getResultUnmatched1() {
		return getIncludedElems(elems1, getUnMatchedIndices1());
	}

	/**
	 * Elements of input 2 that are not part of the LCS
	 * (empty before algorithm is applied)
	 * @return non-LCS elements for input 2
	 */
	public ArrayList<T> getResultUnmatched2() {
		return getIncludedElems(elems2, getUnMatchedIndices2());
	}
	
	/**
	 * Size of the LCS
	 * @return Size of the LCS
	 */
	public int getLCSSize(){
		return matchedIndices1.size();
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
		matchedIndices1 = new ArrayList<Integer>();
		matchedIndices2 = new ArrayList<Integer>();
		this.lcsCommand = lcsCommand;
	}
		
	private void clearResults(){
		matchedIndices1.clear();
		matchedIndices2.clear();
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
		return createLCSResultsOptimized();
	}

	private ArrayList<Integer> getUnmatchedIndices(List<T> elems, ArrayList<Integer> indices) {
		assert elems.size() >= indices.size();
		ArrayList<Integer> unmatchedIndices = new ArrayList<Integer>();
		int indexIndices = 0;
		int nextIncludedIndex = -1;
		if(indexIndices < indices.size())
			nextIncludedIndex = indices.get(indexIndices).intValue();
		for (int elems_index = 0; elems_index < elems.size(); elems_index++) {
			if(elems_index == nextIncludedIndex){				
				//set next included index
				indexIndices += 1;
				nextIncludedIndex = -1;
				if(indexIndices < indices.size())
					nextIncludedIndex = indices.get(indexIndices).intValue();
			}
			else{
				//no match at elems_index!
				unmatchedIndices.add(elems_index);
			}
		}
		assert unmatchedIndices.size() + indices.size() == elems.size();
		return unmatchedIndices;
	}

	private ArrayList<T> getIncludedElems(List<T> elems, ArrayList<Integer> indices) {
		assert elems.size() >= indices.size();
		ArrayList<T> includedElems = new ArrayList<T>();
		int indexIndices = 0;
		int nextIncludedIndex = -1;
		if(indexIndices < indices.size())
			nextIncludedIndex = indices.get(indexIndices).intValue();
		for (int elems_index = 0; elems_index < elems.size(); elems_index++) {
			if(elems_index == nextIncludedIndex){
				//matched at elems_index!
				includedElems.add(elems.get(elems_index));
				
				//set next included index
				indexIndices += 1;
				nextIncludedIndex = -1;
				if(indexIndices < indices.size())
					nextIncludedIndex = indices.get(indexIndices).intValue();
			}
		}
		assert includedElems.size() == indices.size();
		return includedElems;
	}
	
	private LCS<T> createLCSResultsOptimized() {
		int commonPrefixLength = commonPrefixLength();
		int commonSuffixLength = commonSuffixLength(commonPrefixLength);
		addPrefixIndices(commonPrefixLength);
		addMidIndices(commonPrefixLength, commonSuffixLength);
		addSuffixIndices(commonSuffixLength);		
		checkAssertions(elems1, elems2);
		return this;
	}

	private void addMidIndices(int commonPrefixLength, int commonSuffixLength) {
		List<T> elems1_mid = elems1.subList(commonPrefixLength, elems1.size() - commonSuffixLength);
		List<T> elems2_mid = elems2.subList(commonPrefixLength, elems2.size() - commonSuffixLength);
		lcs(
			elems1_mid, 
			elems2_mid,
			commonPrefixLength
		);
	}

	private void addSuffixIndices(int commonSuffixLength) {
		for (int suffixIndex = commonSuffixLength - 1; suffixIndex >= 0 ; suffixIndex--) {
			int el1_index = elems1.size() - 1 - suffixIndex;
			int el2_index = elems2.size() - 1 - suffixIndex;
			assert lcsCommand.isMatch(elems1.get(el1_index), elems2.get(el2_index)): "elements should match since they are in the common suffix"; 
			matchedIndices1.add(el1_index); 
			matchedIndices2.add(el2_index);			
		}
	}

	private void addPrefixIndices(int commonPrefixLength) {
		for (int prefixIndex = 0; prefixIndex < commonPrefixLength; prefixIndex++) {
			assert lcsCommand.isMatch(elems1.get(prefixIndex), elems2.get(prefixIndex)): "elements should match since they are in the common prefix"; 
			matchedIndices1.add(prefixIndex);
			matchedIndices2.add(prefixIndex);			
		}
	}

	private int commonPrefixLength() {
		int minLength = Math.min(elems1.size(), elems2.size()); 
		int commonPrefixLength = 0;
		while (commonPrefixLength < minLength) {
			T el1 = elems1.get(commonPrefixLength);
			T el2 = elems2.get(commonPrefixLength);
			if(lcsCommand.isMatch(el1, el2)){
				commonPrefixLength++;
			}
			else
				break;
		}
		return commonPrefixLength;
	}

	private int commonSuffixLength(int commonPrefixLength) {
		int minLength = Math.min(elems1.size(), elems2.size()); 
		int commonSuffixLength = 0;
		while (commonSuffixLength < minLength - commonPrefixLength) {
			int el1_index = elems1.size() - commonSuffixLength -1;
			int el2_index = elems2.size() - commonSuffixLength - 1;
			T el1 = elems1.get(el1_index);
			T el2 = elems2.get(el2_index);
			if(lcsCommand.isMatch(el1, el2)){
				commonSuffixLength++;
			}
			else
				break;
		}
		return commonSuffixLength;
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
		lcs(elems1, elems2, 0);
		checkAssertions(elems1, elems2);
		return this;
	}

	private void lcs(List<T> elems1, List<T> elems2, int startIndex) {
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
				matchedIndices1.add(i + startIndex);
				matchedIndices2.add(j + startIndex);
                i++;
                j++;
            }
            else if (opt[i+1][j] >= opt[i][j+1]){
            	i++;
            }
            else { 
            	j++;
            }
        }
	}

	
	private void checkAssertions(List<T> elems1, List<T> elems2) {
		assert matchedIndices1.size() == matchedIndices2.size();
		assert matchedIndices1.size() <= elems1.size();
		assert matchedIndices2.size() <=  elems2.size();
		for (int i = 0; i < matchedIndices1.size(); i++) {
			T el1 = elems1.get(matchedIndices1.get(i).intValue());
			T el2 = elems2.get(matchedIndices2.get(i).intValue());
			assert lcsCommand.isMatch(el1, el2);
		}
		for (int i = 1; i < matchedIndices1.size(); i++) {
			assert matchedIndices1.get(i).intValue() > matchedIndices1.get(i-1).intValue();
			assert matchedIndices2.get(i).intValue() > matchedIndices2.get(i-1).intValue();
		}
	}
}
