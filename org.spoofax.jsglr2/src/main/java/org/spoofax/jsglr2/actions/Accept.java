package org.spoofax.jsglr2.actions;

import org.spoofax.jsglr2.characters.Characters;
import org.spoofax.jsglr2.characters.SingleCharacter;

public class Accept extends Action implements IAccept {

	public Accept() {
		super(new SingleCharacter(Characters.EOF));
	}
	
}
