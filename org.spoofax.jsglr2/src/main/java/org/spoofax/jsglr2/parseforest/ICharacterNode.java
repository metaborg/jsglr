package org.spoofax.jsglr2.parseforest;

public interface ICharacterNode extends IParseForestWidth {

    default int width() {
        return 1;
    }

}
