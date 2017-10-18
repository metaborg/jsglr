package org.spoofax.jsglr2.characters;

public class SingleRangeCharacterSet implements ICharacters {

	private final int from;
	private final int to;
	
	public SingleRangeCharacterSet(int from, int to) {
		super();

		this.from = from;
		this.to = to;
	}
	
	public boolean containsCharacter(int character) {
		return from <= character && character <= to;
	}

}
