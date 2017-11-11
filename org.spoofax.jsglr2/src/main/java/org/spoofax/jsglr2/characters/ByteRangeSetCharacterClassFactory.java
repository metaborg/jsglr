package org.spoofax.jsglr2.characters;

public class ByteRangeSetCharacterClassFactory extends RangeSetCharacterClassFactory<Byte> {

    public static final ByteRangeSetCharacterClassFactory INSTANCE = new ByteRangeSetCharacterClassFactory();

    private ByteRangeSetCharacterClassFactory() {
    }

    protected final CharacterClassByteRangeSet emptyRangeSet() {
        return CharacterClassByteRangeSet.EMPTY_CONSTANT;
    }

}
