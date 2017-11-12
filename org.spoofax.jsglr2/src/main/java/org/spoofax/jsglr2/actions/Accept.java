package org.spoofax.jsglr2.actions;

import org.spoofax.jsglr2.characters.ICharacters;

public class Accept extends Action implements IAccept {

    public static final Accept SINGLETON = new Accept();

    public Accept() {
        super(ICharacters.EOF_SINGLETON);
    }

}
