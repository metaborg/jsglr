package org.spoofax.jsglr2.characters;

public interface ICharacters {

	// Index 0 - 255: ASCII characters
	// Index 256    : End-of-file marker (EOF)
	public static final int EOF = 256;

    public boolean containsCharacter(int character);
    
    default ICharacters union(ICharacters other) {
    		CharactersBitSet charactersBitSet = new CharactersBitSet ();
    		
    		for (int character = 0; character <= EOF; character++)
    			charactersBitSet.containsCharacter.set(character, containsCharacter(character) || other.containsCharacter(character));
    		
    		return charactersBitSet;
    }
	
	public static String charToString(int character) {
		if (character == EOF)
			return "EOF";
		else
			return "" + ((char) character);
	}
    
    public static boolean isNewLine(int character) {
        return character != EOF && (char) character == '\n';
    }
    
}
