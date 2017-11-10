package org.spoofax.jsglr2.characters;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableRangeSet;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;

public final class CharacterClassRangeSet implements ICharacters {

    private static final int BITMAP_SEGMENT_SIZE = 6; // 2^6 = 64 = 1/4 * 256

    static final CharacterClassRangeSet EMPTY_CONSTANT = new CharacterClassRangeSet();

    protected ImmutableRangeSet<Byte> rangeSet; // Contains ranges in range [-128, 127]
    protected boolean containsEOF;

    private final boolean useCachedBitSet;
    private long word0; // [0, 63]
    private long word1; // [64, 127]
    private long word2; // [128, 191]
    private long word3; // [192, 255]

    protected CharacterClassRangeSet() {
        this(ImmutableRangeSet.copyOf(TreeRangeSet.create()), false);
    }

    protected CharacterClassRangeSet(final ImmutableRangeSet<Byte> rangeSet, boolean containsEOF) {
        assert rangeSet.isEmpty() || rangeSet.span().lowerEndpoint() >= -128;
        assert rangeSet.isEmpty() || rangeSet.span().upperEndpoint() < (EOF_INT - 128);

        this.rangeSet = rangeSet;
        this.containsEOF = containsEOF;

        this.useCachedBitSet = tryOptimize();
    }

    private final long wordAt(int wordIndex) {
        switch(wordIndex) {
            case -2:
                return word0;
            case -1:
                return word1;
            case 0:
                return word2;
            case 1:
                return word3;
            default:
                return 0L;
        }
    }

    @Override public final boolean containsCharacter(byte character) {
        if(useCachedBitSet) {
            final int wordIndex = character >> BITMAP_SEGMENT_SIZE;
            final long word = wordAt(wordIndex);

            return (word & (1L << character)) != 0;
        } else
            return rangeSet.contains(character);
    }

    @Override public boolean containsEOF() {
        return containsEOF;
    }

    protected final CharacterClassRangeSet updateRangeSet(Consumer<RangeSet<Byte>> transformer) {
        final RangeSet<Byte> mutableRangeSet = TreeRangeSet.create(rangeSet);
        transformer.accept(mutableRangeSet);

        return new CharacterClassRangeSet(ImmutableRangeSet.copyOf(mutableRangeSet), containsEOF);
    }

    protected final CharacterClassRangeSet updateEOF(boolean containsEOF) {
        return new CharacterClassRangeSet(ImmutableRangeSet.copyOf(rangeSet), containsEOF);
    }

    public boolean tryOptimize() {
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

    private static final BitSet convertToBitSet(final RangeSet<Byte> rangeSet) {
        if(rangeSet.isEmpty()) {
            return new BitSet();
        }

        final BitSet bitSet = new BitSet(rangeSet.span().upperEndpoint() + 128);

        rangeSet.asRanges().forEach(range -> bitSet.set(range.lowerEndpoint() + 128, range.upperEndpoint() + 128 + 1));

        return bitSet;
    }

    @Override public int hashCode() {
        return rangeSet.hashCode();
    }

    @Override public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }

        CharacterClassRangeSet that = (CharacterClassRangeSet) o;

        return rangeSet.equals(that.rangeSet);
    }

    public CharacterClassRangeSet union(CharacterClassRangeSet other) {
        RangeSet<Byte> mutableRangeSet = TreeRangeSet.create();

        mutableRangeSet.addAll(this.rangeSet);
        mutableRangeSet.addAll(other.rangeSet);

        boolean containsEOF = this.containsEOF || other.containsEOF;

        return new CharacterClassRangeSet(ImmutableRangeSet.copyOf(mutableRangeSet), containsEOF);
    }

    @Override public CharacterClassRangeSet rangeSetUnion(CharacterClassRangeSet other) {
        return union(other);
    }

}
