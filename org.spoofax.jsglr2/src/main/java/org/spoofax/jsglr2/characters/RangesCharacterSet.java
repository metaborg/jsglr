package org.spoofax.jsglr2.characters;

public class RangesCharacterSet extends CharactersBitSet {
	
	public RangesCharacterSet(int singleRangeFrom, int singleRangeTo) {
		super();
		
		for (int character = singleRangeFrom; character <= singleRangeTo; character++)
			containsCharacter.set(character);
	}
	
	public RangesCharacterSet(int[] ranges) {
		if (ranges.length % 2 != 0)
			throw new IllegalArgumentException("ranges must contains pairs of 2 integers");
		
		for (int i = 0; i < ranges.length; i += 2) {
			int iRangeFrom = ranges[i];
			int iRangeTo = ranges[i + 1];
			
			for (int character = iRangeFrom; character <= iRangeTo; character++)
				containsCharacter.set(character);
		}
	}

}
