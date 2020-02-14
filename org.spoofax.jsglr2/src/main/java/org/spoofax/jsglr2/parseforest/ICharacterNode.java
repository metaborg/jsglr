package org.spoofax.jsglr2.parseforest;

import org.metaborg.parsetable.characterclasses.CharacterClassFactory;

public interface ICharacterNode extends IParseForest {

    int character();

    default int width() {
        return Character.charCount(character());
    }

    default String descriptor() {
        return "'" + CharacterClassFactory.intToString(character()) + "'";
    }

}
