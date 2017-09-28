package org.spoofax.jsglr2.parseforest.hybrid;

import org.spoofax.jsglr2.characters.Characters;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Position;

public class CharacterNode extends HParseForest {

	public final int character;
	
	public CharacterNode(int nodeNumber, Parse parse, Position position, int character) {
		super(nodeNumber, parse, position, Characters.isNewLine(character) ? position.nextLine() : position.nextColumn());
		this.character = character;
	}
	
	public String descriptor() {
		return "'" + Characters.charToString(this.character) + "'";
	}
	
}
