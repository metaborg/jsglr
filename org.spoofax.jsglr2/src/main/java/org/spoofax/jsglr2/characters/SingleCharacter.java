package org.spoofax.jsglr2.characters;

public class SingleCharacter implements ICharacters {

	private final int containsCharacter;
	
	public SingleCharacter(int containsCharacter) {
		this.containsCharacter = containsCharacter;
	}
	
	public boolean containsCharacter(int character) {
		return character == containsCharacter;
	}

	public ICharacters union(ICharacters other) {
		CharactersBitSet characters = new CharactersBitSet(other);
		
		characters.containsCharacter.set(containsCharacter);
		
		return characters;
	}
	
}
