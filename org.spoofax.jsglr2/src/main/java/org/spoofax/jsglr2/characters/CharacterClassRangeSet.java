package org.spoofax.jsglr2.characters;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableRangeSet;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;

public final class CharacterClassRangeSet implements ICharacters {

    protected static final int BITMAP_SEGMENT_SIZE = 6; // 2^6 = 64 = 1/4 * 256

    private ImmutableRangeSet<Integer> rangeSet; // Contains ranges in range [0, 255]

    private final boolean useCachedBitSet;
    private long word0; // [0, 63]
    private long word1; // [64, 127]
    private long word2; // [128, 191]
    private long word3; // [192, 255]
    private boolean containsEOF; // [256]

    static final CharacterClassRangeSet EMPTY_CONSTANT = new CharacterClassRangeSet();

    private CharacterClassRangeSet() {
        this(ImmutableRangeSet.copyOf(TreeRangeSet.create()), false);
    }

    private CharacterClassRangeSet(final ImmutableRangeSet<Integer> rangeSet, boolean containsEOF) {
        assert rangeSet.isEmpty() || rangeSet.span().lowerEndpoint().intValue() >= 0;
        assert rangeSet.isEmpty() || rangeSet.span().upperEndpoint().intValue() < EOF_INT;

        this.rangeSet = rangeSet;
        this.containsEOF = containsEOF;

        this.useCachedBitSet = tryOptimize();
    }

    private final long wordAt(int wordIndex) {
        switch(wordIndex) {
            case 0:
                return word0;
            case 1:
                return word1;
            case 2:
                return word2;
            case 3:
                return word3;
            default:
                return 0L;
        }
    }

    @Override public final boolean containsCharacter(int character) {
        if(character == ICharacters.EOF_INT)
            return containsEOF;

        if(useCachedBitSet) {
            final int wordIndex = character >> BITMAP_SEGMENT_SIZE;
            final long word = wordAt(wordIndex);

            return (word & (1L << character)) != 0;
        } else
            return rangeSet.contains(character);
    }

    protected final CharacterClassRangeSet addRange(int from, int to) {
        final RangeSet<Integer> mutableRangeSet = TreeRangeSet.create(rangeSet);

        mutableRangeSet.add(Range.closed(from, Math.min(255, to)));

        return new CharacterClassRangeSet(ImmutableRangeSet.copyOf(mutableRangeSet),
            containsEOF || to == ICharacters.EOF_INT);
    }

    protected final CharacterClassRangeSet addSingle(int character) {
        final RangeSet<Integer> mutableRangeSet = TreeRangeSet.create(rangeSet);

        if(character < ICharacters.EOF_INT)
            mutableRangeSet.add(Range.singleton(character));

        return new CharacterClassRangeSet(ImmutableRangeSet.copyOf(mutableRangeSet),
            containsEOF || character == ICharacters.EOF_INT);
    }

    private boolean tryOptimize() {
        if(!rangeSet.isEmpty()) {
            final BitSet bitSet = convertToBitSet(rangeSet);

            final long[] words = bitSet.toLongArray();
            switch(words.length) {
                case 4:
                    word3 = words[3];
                case 3:
                    word2 = words[2];
                case 2:
                    word1 = words[1];
                case 1:
                    word0 = words[0];
            }
            return true;
        } else {
            return false;
        }
    }

    private final BitSet convertToBitSet(final RangeSet<Integer> rangeSet) {
        if(rangeSet.isEmpty()) {
            return new BitSet();
        }

        final BitSet bitSet = new BitSet(rangeSet.span().upperEndpoint().intValue());

        rangeSet.asRanges().forEach(range -> {
            bitSet.set(range.lowerEndpoint().intValue(), range.upperEndpoint().intValue() + 1);
        });

        return bitSet;
    }

    @Override public CharacterClassRangeSet rangeSetUnion(CharacterClassRangeSet other) {
        return union(other);
    }

    protected final CharacterClassRangeSet union(CharacterClassRangeSet other) {
        final RangeSet<Integer> mutableRangeSet = TreeRangeSet.create();

        mutableRangeSet.addAll(this.rangeSet);
        mutableRangeSet.addAll(other.rangeSet);

        boolean containsEOF = this.containsEOF || other.containsEOF;

        return new CharacterClassRangeSet(ImmutableRangeSet.copyOf(mutableRangeSet), containsEOF);
    }

    public final ICharacters optimized() {
        if(rangeSet.isEmpty())
            return containsEOF ? EOF_SINGLETON : new CharactersClassOptimized();
        else
            return new CharactersClassOptimized(word0, word1, word2, word3, containsEOF);
    }

    @Override public int hashCode() {
        return rangeSet.hashCode() ^ Boolean.hashCode(containsEOF);
    }

    @Override public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }

        CharacterClassRangeSet that = (CharacterClassRangeSet) o;

        return rangeSet.equals(that.rangeSet) && containsEOF == that.containsEOF;
    }

    @Override public String toString() {
        final List<String> ranges = new ArrayList<>();

        rangeSet.asRanges().forEach(range -> {
            final int from = range.lowerEndpoint().intValue();
            final int to = range.upperEndpoint().intValue();

            if(from != to)
                ranges.add("" + ICharacters.intToString(from) + "-" + ICharacters.intToString(to));
            else
                ranges.add("" + ICharacters.intToString(from));
        });

        if(containsEOF)
            ranges.add("EOF");

        return "{" + StringUtils.join(ranges, ",") + "}";
    }

}
