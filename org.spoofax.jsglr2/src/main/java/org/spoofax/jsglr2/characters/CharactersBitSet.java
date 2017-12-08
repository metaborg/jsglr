package org.spoofax.jsglr2.characters;

import java.util.BitSet;

public class CharactersBitSet implements ICharacters {
	
	protected BitSet containsCharacter;
	
	public CharactersBitSet() {
		this.containsCharacter = new BitSet();
	}
	
	public CharactersBitSet(ICharacters characters) {
		this.containsCharacter = new BitSet();
		
		for (int character = 0; character <= EOF; character++)
			this.containsCharacter.set(character, characters.containsCharacter(character));
	}
	
	public CharactersBitSet(BitSet containsCharacter) {
		this.containsCharacter = (BitSet) containsCharacter.clone();
	}
	
	public boolean containsCharacter(int character) {
	    return this.containsCharacter.get(character);
	}
	
	public ICharacters union(ICharacters otherCharacters) {
		BitSet containsCharacter = (BitSet) this.containsCharacter.clone();
		
		for (int character = 0; character <= EOF; character++)
			containsCharacter.set(character, containsCharacter.get(character) || otherCharacters.containsCharacter(character));
		
		return new CharactersBitSet(containsCharacter);
	}
	
}
