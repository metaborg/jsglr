package org.spoofax.jsglr2.characters;

public class CharactersBooleanArray implements ICharacters {
	
	protected boolean[] containsCharacter;
	
	public CharactersBooleanArray(ICharacters characters) {
		this.containsCharacter = new boolean[257];
		
		for (int character = 0; character <= EOF; character++)
			this.containsCharacter[character] = characters.containsCharacter(character);
	}
	
	public CharactersBooleanArray(boolean[] containsCharacter) {
		this.containsCharacter = (boolean[]) containsCharacter.clone();
	}
	
	public boolean containsCharacter(int character) {
	    if (character > 256)
	        return false;
	    else
	        return this.containsCharacter[character];
	}
	
	public ICharacters union(ICharacters otherCharacters) {
		boolean[] containsCharacter = new boolean[257];
		
		for (int character = 0; character <= EOF; character++)
			containsCharacter[character] = containsCharacter(character) || otherCharacters.containsCharacter(character);
		
		return new CharactersBooleanArray(containsCharacter);
	}
	
}
