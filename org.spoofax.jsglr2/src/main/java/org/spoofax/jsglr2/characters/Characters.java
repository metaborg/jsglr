package org.spoofax.jsglr2.characters;

public abstract class Characters implements ICharacters {
	
	public static final int EOF = 256; // 0 to 255 represents ASCII. 256 represents end-of-file marker (EOF).
	
	public abstract boolean containsCharacter(int character);
	
	public static String charToString(int character) {
		if (character == EOF)
			return "EOF";
		else
			return "" + ((char) character);
	}
    
    public static boolean isNewLine(int character) {
        return character != EOF && (char) character == '\n';
    }
	
	public Characters union(Characters otherCharacters) {
		boolean[] containsCharacter = new boolean[257];
		
		for (int character = 0; character < 257; character++)
			containsCharacter[character] = this.containsCharacter(character) || otherCharacters.containsCharacter(character);
		
		return new CharacterSet(containsCharacter);
	}

}
