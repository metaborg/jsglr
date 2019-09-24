package org.spoofax.jsglr2.datadependent;

import org.spoofax.jsglr2.parseforest.basic.BasicCharacterNode;

class DataDependentCharacterNode extends BasicCharacterNode implements IDataDependentParseForest {

    DataDependentCharacterNode(int character) {
        super(character);
    }

}
