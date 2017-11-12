package org.spoofax.jsglr2.actions;

import org.spoofax.jsglr2.characters.ICharacters;

public class Accept extends Action implements IAccept {

    // TODO: this can be a singleton

    public Accept() {
        super(ICharacters.eof());
    }

}
