package org.spoofax.jsglr2.parseforest;

import org.metaborg.parsetable.characterclasses.CharacterClassFactory;

public interface ICharacterNode extends IParseForest {

    int character();

    default int width() {
        return 1;
    }

    default String descriptor() {
        return "'" + CharacterClassFactory.intToString(character()) + "'";
    }

}
