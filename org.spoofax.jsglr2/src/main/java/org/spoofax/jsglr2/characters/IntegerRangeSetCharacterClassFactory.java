package org.spoofax.jsglr2.characters;

public class IntegerRangeSetCharacterClassFactory extends RangeSetCharacterClassFactory<Integer> {

    public IntegerRangeSetCharacterClassFactory(boolean optimize) {
        super(optimize);
    }

    protected final CharacterClassIntegerRangeSet emptyRangeSet() {
        return CharacterClassIntegerRangeSet.EMPTY_CONSTANT;
    }

}
