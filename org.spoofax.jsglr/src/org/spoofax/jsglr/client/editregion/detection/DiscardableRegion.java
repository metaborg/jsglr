package org.spoofax.jsglr.client.editregion.detection;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Fragment in the source code that represents a (possibly broken) construct
 * that (presumably) can be discarded safely, e.g., without invalidating the parse input.
 * @author maartje
 */
public class DiscardableRegion{
	
//private fields
	
	private final int startOffset;
	private final int endOffset;
	private final String inputString;
	

//constructor
	
	/**
	 * Fragment in the source code that represents a (possibly broken) construct
	 * that can be discarded safely, e.g., without invalidating the parse input.
	 */
	public DiscardableRegion(int startOffset, int endOffset, String inputString){
		this.startOffset = startOffset;
		this.endOffset = endOffset;
		this.inputString = inputString;
		assert inputString != null;
		assert startOffset <= endOffset;
		assert startOffset >= 0;
		assert endOffset <= inputString.length() - 1;
	}

//getters
	
	/**
	 * Returns the start offset of the discardable code fragment
	 * @return start offset
	 */
	public int getStartOffset() {
		return startOffset;
	}

	/**
	 * Returns the end offset of the discardable code fragment
	 * @return end offset
	 */
	public int getEndOffset() {
		return endOffset;
	}

	/**
	 * Returns the term associated to the discardable code fragment.
	 * Can be null in case the discarded region is a comment, or in case the abstract representation is not known.
	 * @return term
	 */
	public String getInputString() {
		return inputString;
	}
	
//public functions
	
	/**
	 * Returns a recovered input String by replacing erroneous regions by whitespaces
	 * @param regions
	 * @param input
	 * @return
	 */
	public static String replaceAllRegionsByWhitespace(ArrayList<DiscardableRegion> regions, String input) {
		String result = input;
		for (DiscardableRegion region : regions) {
			assert region.getInputString() == input;
			result = replaceRegionByWhitespace(region, result);
		}
		return result;
	}
	
	/**
	 * Construct regions by grouping subsequent offsets
	 * @param offsets
	 * @param inputString
	 * @return
	 */
	public static ArrayList<DiscardableRegion> constructRegionsFromOffsets(ArrayList<Integer> offsets, String inputString){
		ArrayList<DiscardableRegion> result = new ArrayList<DiscardableRegion>();
		int startOffset = -1;
		for (int i = 0; i < offsets.size(); i++) {
			int offset = offsets.get(i);
			if(i==0){
				startOffset = offsets.get(i);
			}
			else if (offset != offsets.get(i-1) + 1){
				DiscardableRegion region = new DiscardableRegion(startOffset, offsets.get(i-1), inputString);
				result.add(region);
				startOffset = offsets.get(i);
			}
		}
		if(startOffset != -1){
			DiscardableRegion region = new DiscardableRegion(startOffset, offsets.get(offsets.size()-1), inputString);
			result.add(region);
		}
		return result;		
	}
	
	/**
	 * Returns all offsets covered by the regions
	 * @param editRegions
	 * @return
	 */
	public static ArrayList<Integer> getOffsets(ArrayList<DiscardableRegion> editRegions) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (DiscardableRegion region : editRegions) {
			int startOffset = region.getStartOffset();
			int endOffset = region.getEndOffset();
			for (int offset = startOffset; offset <= endOffset; offset++) {
				// "regions can overlap, for example: term + separation and comment in separation";
				if(!result.contains(offset))
					result.add(offset);
			}
		}
		Collections.sort(result);
		return result;
	}

	public static ArrayList<DiscardableRegion> mergeSubsequentRegions(ArrayList<DiscardableRegion> regions){
		ArrayList<DiscardableRegion> merged = new ArrayList<DiscardableRegion>();
		if(!regions.isEmpty()){
			merged.add(regions.get(0));
		}
		int index = 1;
		while (index < regions.size()) {
			DiscardableRegion r1 = merged.get(0);
			DiscardableRegion r2 = regions.get(index);
			assert r2.getEndOffset() > r1.getStartOffset() : "input regions should be ordered"; 
			if(r1.getEndOffset() + 1 < r2.getStartOffset()){
				merged.add(r2);
			}
			else {
				int startOffset = Math.min(r1.getStartOffset(), r2.getStartOffset());
				int endOffset = Math.max(r1.getEndOffset(), r2.getEndOffset());
				assert r1.getInputString() == r2.getInputString();
				DiscardableRegion mergedRegion = new DiscardableRegion(startOffset, endOffset, r1.getInputString());
				merged.set(merged.size()-1, mergedRegion);
			}
			index ++;
		}
		assert merged.size() <= regions.size();
		return merged;
	}

	/**
	 * Merges ordered region lists so that the resulting lists is ordered and merges overlapping regions
	 * @param regions1
	 * @param regions2
	 * @return
	 */
	public static ArrayList<DiscardableRegion> mergeRegions(ArrayList<DiscardableRegion> regions1, ArrayList<DiscardableRegion> regions2){
		ArrayList<DiscardableRegion> merged = new ArrayList<DiscardableRegion>();
		int index_r1 = 0;
		int index_r2 = 0;
		while (index_r1 < regions1.size() && index_r2 < regions2.size()) {
			DiscardableRegion r1 = regions1.get(index_r1);			
			DiscardableRegion r2 = regions2.get(index_r2);
			if(r1.getEndOffset() < r2.getStartOffset()){
				merged.add(r1);
				index_r1 ++;
			}
			else if(r2.getEndOffset() < r1.getStartOffset()){
				merged.add(r2);
				index_r2 ++;
			}
			else {
				int startOffset = Math.min(r1.getStartOffset(), r2.getStartOffset());
				int endOffset = Math.max(r1.getEndOffset(), r2.getEndOffset());
				assert r1.getInputString() == r2.getInputString();
				DiscardableRegion mergedRegion = new DiscardableRegion(startOffset, endOffset, r1.getInputString());
				merged.add(mergedRegion);
				index_r1++;
				index_r2++;
			}
		}
		while (index_r1 < regions1.size()) {
			DiscardableRegion r1 = regions1.get(index_r1);
			merged.add(r1);
			index_r1 ++;
		}
		while (index_r2 < regions2.size()) {
			DiscardableRegion r2 = regions2.get(index_r2);
			merged.add(r2);
			index_r2++;
		}
		return merged;
	}
	
	/**
	 * Constructs the list of text fragments represented by the regions
	 * @param regions
	 * @return
	 */
	public static ArrayList<String> constructFragments(ArrayList<DiscardableRegion> regions) {
		ArrayList<String> result = new ArrayList<String>();
		for (DiscardableRegion region : regions) {
			String fragment = region.constructFragment();
			result.add(fragment);
		}
		return result;
	}

	/**
	 * Constructs the discarded fragment
	 * @return
	 */
	public String constructFragment() {
		return inputString.substring(getStartOffset(), getEndOffset() + 1);
	}
	
	/**
	 * Includes preceding and succeeding layout (to be used with the permissive grammars technique) 
	 * @return
	 */
	public DiscardableRegion extendRegionWithWhitespace(){
		int start = this.startOffset;
		while(start > 0 && Character.isWhitespace(this.inputString.charAt(start-1))){
			start -= 1;
		}
		int end = this.endOffset;
		while(end < inputString.length()-2 && Character.isWhitespace(this.inputString.charAt(end+1))){
			end += 1;
		}
		return new DiscardableRegion(start, end, inputString);
	}
	
	@Override
	public String toString(){
		return constructFragment();
	}

//private functions
	
	private static String replaceRegionByWhitespace(DiscardableRegion region, String modifiedInput) {
		assert modifiedInput.length() == region.getInputString().length();
		char[] chars = modifiedInput.toCharArray();
		for (int offset = region.getStartOffset(); offset <= region.getEndOffset(); offset++) {
			char charAtOffset = chars[offset];
			if(!Character.isWhitespace(charAtOffset)){
				chars[offset] = ' ';
			}
		}
		return String.valueOf(chars);
	}
}
