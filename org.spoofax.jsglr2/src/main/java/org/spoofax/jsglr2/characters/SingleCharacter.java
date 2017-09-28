package org.spoofax.jsglr2.characters;

public class SingleCharacter extends Characters {

	private final int containsCharacter;
	
	public SingleCharacter(int containsCharacter) {
		this.containsCharacter = containsCharacter;
	}
	
	public boolean containsCharacter(int character) {
		return character == containsCharacter;
	}
	
}
