package org.spoofax.jsglr2.characters;

public class CharacterSet extends Characters {
	
	// Index 0 - 255: ASCII characters
	// Index 256    : End-of-file marker (EOF)
	protected boolean[] containsCharacter = new boolean[257]; // TODO: booleans are not bits in Java

	protected CharacterSet() {}
	
	public CharacterSet(int[] characters) {
		for (int character : characters)
			this.containsCharacter[character] = true;
	}
	
	public CharacterSet(boolean[] containsCharacter) {
		this.containsCharacter = containsCharacter;
	}
	
	public boolean containsCharacter(int character) {
	    if (character > 256)
	        return false;
	    else
	        return this.containsCharacter[character];
	}
	
}
