package org.spoofax.jsglr2.datadependent;

import org.spoofax.jsglr2.parseforest.basic.BasicCharacterNode;

public class DataDependentCharacterNode extends BasicCharacterNode implements IDataDependentParseForest {

    public DataDependentCharacterNode(int character) {
        super(character);
    }

}
