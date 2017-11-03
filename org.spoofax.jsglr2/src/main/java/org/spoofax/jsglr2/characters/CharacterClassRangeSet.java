package org.spoofax.jsglr2.characters;

import java.util.BitSet;
import java.util.function.Consumer;

import com.google.common.collect.ImmutableRangeSet;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;

public final class CharacterClassRangeSet implements ICharacters {

  private static final int BITMAP_SEGMENT_SIZE = 6;

  static final CharacterClassRangeSet EMPTY_CONSTANT = new CharacterClassRangeSet();

  protected ImmutableRangeSet<Integer> rangeSet;
  // private transient BitSet cachedBitSet;

  private final boolean useCachedBitSet;
  private long word0;
  private long word1;
  private long word2;
  private long word3;
  private long word4;

  protected CharacterClassRangeSet() {
    this(ImmutableRangeSet.copyOf(TreeRangeSet.create()));
  }

  protected CharacterClassRangeSet(final ImmutableRangeSet<Integer> rangeSet) {
    assert rangeSet.isEmpty() || rangeSet.span().lowerEndpoint() >= 0;
    assert rangeSet.isEmpty() || rangeSet.span().upperEndpoint() <= EOF;

    this.rangeSet = rangeSet;
    // this.cachedBitSet = convertToBitSet(rangeSet);
    this.useCachedBitSet = tryOptimize();
  }

  private final long wordAt(int wordIndex) {
    switch (wordIndex) {
      case 0:
        return word0;
      case 1:
        return word1;
      case 2:
        return word2;
      case 3:
        return word3;
      case 4:
        return word4;
      default:
        return 0L;
    }
  }

  public final boolean containsCharacter(int character) {
    if (useCachedBitSet) {
      final int wordIndex = character >> BITMAP_SEGMENT_SIZE;
      final long word = wordAt(wordIndex);

      return (word & (1L << character)) != 0;
    } else {
      return rangeSet.contains(character);
    }
  }

  protected final CharacterClassRangeSet update(
      Consumer<RangeSet<Integer>> transformer) {
    final RangeSet<Integer> mutableRangeSet = TreeRangeSet.create(rangeSet);
    transformer.accept(mutableRangeSet);

    return new CharacterClassRangeSet(ImmutableRangeSet.copyOf(mutableRangeSet));
  }

  public boolean tryOptimize() {
    if (!rangeSet.isEmpty() && rangeSet.span().upperEndpoint() <= EOF) {
      final BitSet bitSet = convertToBitSet(rangeSet);

      final long[] words = bitSet.toLongArray();
      switch (words.length) {
        case 5:
          word4 = words[4];
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

  private static final BitSet convertToBitSet(final RangeSet<Integer> rangeSet) {
    if (rangeSet.isEmpty()) {
      return new BitSet();
    }

    final BitSet bitSet = new BitSet(rangeSet.span().upperEndpoint());

    rangeSet.asRanges().forEach(
        range -> bitSet.set(range.lowerEndpoint(), range.upperEndpoint() + 1));

    return bitSet;
  }

  @Override
  public int hashCode() {
    return rangeSet.hashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CharacterClassRangeSet that = (CharacterClassRangeSet) o;

    return rangeSet.equals(that.rangeSet);
  }

  public String toString() {
    return rangeSet.toString();
  }

}
