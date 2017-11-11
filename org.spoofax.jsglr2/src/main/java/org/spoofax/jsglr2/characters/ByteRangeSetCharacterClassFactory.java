package org.spoofax.jsglr2.characters;

public class ByteRangeSetCharacterClassFactory extends RangeSetCharacterClassFactory<Byte> {

    public ByteRangeSetCharacterClassFactory(boolean optimize) {
        super(optimize);
    }

    protected final CharacterClassByteRangeSet emptyRangeSet() {
        return CharacterClassByteRangeSet.EMPTY_CONSTANT;
    }

}
