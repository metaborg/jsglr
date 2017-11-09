package org.spoofax.jsglr2.characters;

import com.google.common.collect.ImmutableRangeSet;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;

public class CharacterClassFactory implements ICharacterClassFactory<CharacterClassRangeSet> {

    public static final CharacterClassFactory INSTANCE = new CharacterClassFactory();

    private CharacterClassFactory() {
    }

    @Override
    public CharacterClassRangeSet fromEmpty() {
        return CharacterClassRangeSet.EMPTY_CONSTANT;
    }

    @Override
    public CharacterClassRangeSet fromSingle(int character) {
        return CharacterClassRangeSet.EMPTY_CONSTANT.update(rangeSet -> rangeSet.add(Range.singleton(character)));
    }

    @Override
    public CharacterClassRangeSet fromRange(int from, int to) {
        return CharacterClassRangeSet.EMPTY_CONSTANT.update(rangeSet -> rangeSet.add(Range.closed(from, to)));
    }

    @Override
    public CharacterClassRangeSet union(ICharacters a, ICharacters b) {
        if(!(a instanceof CharacterClassRangeSet && b instanceof CharacterClassRangeSet)) {
            throw new IllegalArgumentException(
                String.format("Expected arguments of type %s", CharacterClassRangeSet.class));
        }

        final CharacterClassRangeSet one = (CharacterClassRangeSet) a;
        final CharacterClassRangeSet two = (CharacterClassRangeSet) b;

        RangeSet<Integer> mutableRangeSet = TreeRangeSet.create();
        mutableRangeSet.addAll(one.rangeSet);
        mutableRangeSet.addAll(two.rangeSet);

        return new CharacterClassRangeSet(ImmutableRangeSet.copyOf(mutableRangeSet));
    }

}
