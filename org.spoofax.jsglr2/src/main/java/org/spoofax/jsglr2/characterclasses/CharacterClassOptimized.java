package org.spoofax.jsglr2.characterclasses;

public final class CharacterClassOptimized implements ICharacterClass {

    private long word0; // [0, 63]
    private long word1; // [64, 127]
    private long word2; // [128, 191]
    private long word3; // [192, 255]
    private boolean containsEOF; // [256]

    private int min, max;

    public CharacterClassOptimized() {
        this.containsEOF = false;
        this.min = this.max = -1;

        throw new IllegalStateException("empty character classes not allowed");
    }

    public CharacterClassOptimized(long word0, long word1, long word2, long word3, boolean containsEOF, int min,
        int max) {
        this.word0 = word0;
        this.word1 = word1;
        this.word2 = word2;
        this.word3 = word3;
        this.containsEOF = containsEOF;
        this.min = min;
        this.max = max;
    }

    public final boolean contains(int character) {
        final int wordIndex = character >> CharacterClassRangeSet.BITMAP_SEGMENT_SIZE;
        final long word;

        switch(wordIndex) {
            case 0:
                word = word0;
                break;
            case 1:
                word = word1;
                break;
            case 2:
                word = word2;
                break;
            case 3:
                word = word3;
                break;
            case 4:
                return containsEOF;
            default:
                word = 0L;
        }

        return (word & (1L << character)) != 0;
    }

    public int min() {
        return min;
    }

    public int max() {
        return max;
    }

    public final CharacterClassRangeSet rangeSetUnion(CharacterClassRangeSet rangeSet) {
        throw new IllegalStateException();
    }

    @Override public int hashCode() {
        return (int) (word0 ^ word1 ^ word2 ^ word3 ^ Boolean.hashCode(containsEOF));
    }

    @Override public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }

        CharacterClassOptimized that = (CharacterClassOptimized) o;

        return word0 == that.word0 && word1 == that.word1 && word2 == that.word2 && word3 == that.word3
            && containsEOF == that.containsEOF;
    }

    @Override public final String toString() {
        // This is an optimized representation of character classes. If you want nice toString results, disable
        // optimizing in CharacterClassFactory.

        return "{..optimized..}";
    }

}
