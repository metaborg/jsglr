package org.spoofax.jsglr2.characters;

import com.google.common.collect.Range;

public class CharacterClassFactory implements ICharacterClassFactory {

    public static final CharacterClassFactory INSTANCE = new CharacterClassFactory();

    private CharacterClassFactory() {
    }

    @Override public ICharacters fromEmpty() {
        return CharacterClassRangeSet.EMPTY_CONSTANT;
    }

    @Override public ICharacters fromEOF() {
        return CharactersEOF.INSTANCE;
    }

    @Override public ICharacters fromSingle(int character) {
        return new CharactersSingle(character);
    }

    @Override public ICharacters fromRange(int from, int to) {
        return CharacterClassRangeSet.EMPTY_CONSTANT
            .updateRangeSet(rangeSet -> rangeSet.add(Range.closed((byte) (from - 128), (byte) (to - 128))));
    }

    @Override public ICharacters union(ICharacters a, ICharacters b) {
        boolean aIsRangeSet = a instanceof CharacterClassRangeSet;
        boolean bIsRangeSet = b instanceof CharacterClassRangeSet;

        if(aIsRangeSet || bIsRangeSet) {
            CharacterClassRangeSet rangeSet;
            ICharacters other;

            if(aIsRangeSet) {
                rangeSet = (CharacterClassRangeSet) a;
                other = b;
            } else {
                rangeSet = (CharacterClassRangeSet) b;
                other = a;
            }

            return other.rangeSetUnion(rangeSet);
        } else {
            CharacterClassRangeSet result = CharacterClassRangeSet.EMPTY_CONSTANT;

            result = a.rangeSetUnion(result);
            result = b.rangeSetUnion(result);

            return result;
        }
    }


}
