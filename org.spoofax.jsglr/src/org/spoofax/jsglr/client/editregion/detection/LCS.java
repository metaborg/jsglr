package org.spoofax.jsglr.client.editregion.detection;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements a Longest Common Subsequence algorithm
 * @author maartje
 *
 * @param <T>
 */
public class LCS<T> {
	
	private final LCSCommand<T> lcsCommand;
	private ArrayList<T> elems1;
	private ArrayList<T> elems2;
	private ArrayList<Integer> matchedIndices1;
	private ArrayList<Integer> matchedIndices2;
	
	private ArrayList<Integer> unMatchedIndices1;
	private ArrayList<Integer> unMatchedIndices2;
	private ArrayList<T> resultLCS1;
	private ArrayList<T> resultLCS2;
	private ArrayList<T> resultUnmatched1;
	private ArrayList<T> resultUnmatched2;
	private int matchedPrefixSize;
	private int matchedSuffixSize;

	/**
	 * Returns the elements of the first input list
	 * @return 
	 */
	public ArrayList<T> getElems1() {
		return elems1;
	}

	/**
	 * Returns the elements of the second input list
	 * @return
	 */
	public ArrayList<T> getElems2() {
		return elems2;
	}
	
	/**
	 * Returns the index of the corresponding element in input2 (isInCorrectInputString == true), or input1 (isInCorrectInputString == false).
	 * Returns -1 in case the element is not matched.
	 * @param index of element
	 * @param isInCorrectInputString
	 * @return
	 */
	public int getMatchIndex(int index, boolean isInCorrectInputString) {
		if(isInCorrectInputString){
			return getMatchIndexInElems2(index);
		}
		return getMatchIndexInElems1(index);
	}

	/**
	 * Returns the index of the corresponding element in elems2 for the element at elems1[indexElems1].
	 * Returns -1 in case the element is not matched.
	 * @param indexElems1 index of element in input1
	 * @return
	 */
	public int getMatchIndexInElems2(int indexElems1){
		int lcsIndex = matchedIndices1.indexOf(indexElems1);
		if(lcsIndex >= 0){
			return matchedIndices2.get(lcsIndex);
		}
		return -1;
	}

	/**
	 * Returns the index of the corresponding element in elems1 for the element at elems2[indexElems2].
	 * Returns -1 in case the element is not matched.
	 * @param indexElems2 index of element in input2
	 * @return
	 */
	public int getMatchIndexInElems1(int indexElems2){
		int lcsIndex = matchedIndices2.indexOf(indexElems2);
		if(lcsIndex > 0){
			return matchedIndices1.get(lcsIndex);
		}
		return -1;
	}

	/**
	 * Returns the indexes of the matched elements in input1
	 * @return
	 */
	public ArrayList<Integer> getMatchedIndices1() {
		return matchedIndices1;
	}

	/**
	 * Returns the indexes of the matched elements in input2
	 * @return
	 */
	public ArrayList<Integer> getMatchedIndices2() {
		return matchedIndices2;
	}

	/**
	 * Returns the indexes of the unmatched elements in input1
	 * @return
	 */
	public ArrayList<Integer> getUnMatchedIndices1() {
		if(unMatchedIndices1 == null){
			unMatchedIndices1 = getUnmatchedIndices(elems1, matchedIndices1);
		}
		return unMatchedIndices1;
	}

	/**
	 * Returns the indexes of the unmatched elements in input2
	 * @return
	 */
	public ArrayList<Integer> getUnMatchedIndices2() {
		if(unMatchedIndices2 == null){
			unMatchedIndices2 = getUnmatchedIndices(elems2, matchedIndices2);
		}
		return unMatchedIndices2;
	}

	/**
	 * LCS result for input 1
	 * (empty before algorithm is applied)
	 * @return LCS elements for input 1
	 */
	public ArrayList<T> getResultLCS1() {
		if(resultLCS1 == null){
			resultLCS1 = getIncludedElems(elems1, matchedIndices1);
		}
		return resultLCS1;
	}

	/**
	 * LCS result for input 2
	 * (empty before algorithm is applied)
	 * @return LCS elements for input 2
	 */
	public ArrayList<T> getResultLCS2() {
		if(resultLCS2 == null){
			resultLCS2 = getIncludedElems(elems2, matchedIndices2);
		}
		return resultLCS2;
	}

	/**
	 * Elements of input 1 that are not part of the LCS
	 * (empty before algorithm is applied)
	 * @return non-LCS elements for input 1
	 */
	public ArrayList<T> getResultUnmatched1() {
		if(resultUnmatched1 == null){
			resultUnmatched1 = getIncludedElems(elems1, getUnMatchedIndices1());
		}
		return resultUnmatched1;
	}

	/**
	 * Elements of input 2 that are not part of the LCS
	 * (empty before algorithm is applied)
	 * @return non-LCS elements for input 2
	 */
	public ArrayList<T> getResultUnmatched2() {
		if(resultUnmatched2 == null){
			resultUnmatched2 = getIncludedElems(elems2, getUnMatchedIndices2());
		}
		return resultUnmatched2;
	}
	
	/**
	 * Size of the LCS
	 * @return Size of the LCS
	 */
	public int getLCSSize(){
		return matchedIndices1.size();
	}

	/**
	 * Returns the number of matched elements at the prefix
	 * @return
	 */
	public int getMatchedPrefixSize(){
		if(matchedPrefixSize == -1){
			matchedPrefixSize = 0;
			while(
				matchedPrefixSize < getLCSSize() &&
				matchedIndices1.get(matchedPrefixSize) == matchedPrefixSize &&
				matchedIndices2.get(matchedPrefixSize) == matchedPrefixSize
			)
			{
				matchedPrefixSize ++;
			}
		}
		return matchedPrefixSize;
	}

	/**
	 * Returns the number of matched elements at the suffix
	 * @return
	 */
	public int getMatchedSuffixSize(){
		if(matchedSuffixSize == -1){
			matchedSuffixSize = 0;
			int elems1Size = elems1.size();
			int elems2Size = elems2.size();
			while(
				matchedSuffixSize < getLCSSize() &&
				matchedIndices1.get(getLCSSize() - 1 - matchedSuffixSize) == elems1Size - 1 - matchedSuffixSize &&
				matchedIndices2.get(getLCSSize() - 1 - matchedSuffixSize) == elems2Size - 1 - matchedSuffixSize
			)
			{
				matchedSuffixSize ++;
			}
		}
		return matchedSuffixSize;
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
		unMatchedIndices1 = null;
		unMatchedIndices2 = null;
		resultLCS1 = null;
		resultLCS2 = null;
		resultUnmatched1 = null;
		resultUnmatched2 = null;
		this.lcsCommand = lcsCommand;
		matchedPrefixSize = -1;
		matchedSuffixSize = -1;
	}
			
	/**
	 * Fills the LCS results: LCS (longest common subsequence), unmatched elems1, unmatched elems2.
	 * As an optimization, the algorithm starts with matching elements at the prefix and suffix.
	 * @param elems1
	 * @param elems2
	 */
	public LCS<T> createLCSResultsOptimized(List<T> elems1, List<T> elems2) {
		clearResults();
		this.elems1.addAll(elems1);
		this.elems2.addAll(elems2);
		return createLCSResultsOptimized();
	}
	
	/**
	 * Fills the LCS results: LCS (longest common subsequence), unmatched elems1, unmatched elems2.
	 * @param elems1
	 * @param elems2
	 */
	public LCS<T> createLCSResults(List<T> elems1, List<T> elems2) {
		clearResults();
		this.elems1.addAll(elems1);
		this.elems2.addAll(elems2);
		lcs(elems1, elems2, 0);
		checkAssertions(elems1, elems2);
		return this;
	}

	
// helper functions

	private void clearResults(){
		elems1.clear();
		elems2.clear();
		matchedIndices1.clear();
		matchedIndices2.clear();
		unMatchedIndices1 = null;
		unMatchedIndices2 = null;
		resultLCS1 = null;
		resultLCS2 = null;
		resultUnmatched1 = null;
		resultUnmatched2 = null;
		matchedPrefixSize = -1;
		matchedSuffixSize = -1;
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
