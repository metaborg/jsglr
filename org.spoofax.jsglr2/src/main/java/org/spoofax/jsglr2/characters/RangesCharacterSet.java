package org.spoofax.jsglr2.characters;

public class RangesCharacterSet extends CharacterSet {
	
	public RangesCharacterSet(int singleRangeFrom, int singleRangeTo) {
		for (int i = singleRangeFrom; i <= singleRangeTo; i++)
			containsCharacter[i] = true;
	}
	
	public RangesCharacterSet(int[] ranges) {
		if (ranges.length % 2 != 0)
			throw new IllegalArgumentException("ranges must contains pairs of 2 integers");
		
		for (int i = 0; i < ranges.length; i += 2) {
			int iRangeFrom = ranges[i];
			int iRangeTo = ranges[i + 1];
			
			for (int j = iRangeFrom; j <= iRangeTo; j++)
				containsCharacter[j] = true;
		}
	}

}
