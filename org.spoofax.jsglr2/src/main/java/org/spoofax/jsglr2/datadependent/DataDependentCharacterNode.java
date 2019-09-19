package org.spoofax.jsglr2.datadependent;

import org.spoofax.jsglr2.parseforest.ICharacterNode;

public class DataDependentCharacterNode implements IDataDependentParseForest, ICharacterNode {

    public final int character;

    public DataDependentCharacterNode(int character) {
        this.character = character;
    }

    @Override public int character() {
        return character;
    }

}
