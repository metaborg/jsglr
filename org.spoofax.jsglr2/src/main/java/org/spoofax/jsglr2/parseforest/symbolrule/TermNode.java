package org.spoofax.jsglr2.parseforest.symbolrule;

import org.spoofax.jsglr2.characters.Characters;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Position;

public class TermNode extends SRParseForest {

	public final int character;
	
	public TermNode(int nodeNumber, Parse parse, Position position, int character) {
		super(nodeNumber, parse, position, Characters.isNewLine(character) ? position.nextLine() : position.nextColumn());
		this.character = character;
	}
	
	public String descriptor() {
		return "'" + Characters.charToString(this.character) + "'";
	}
	
}
