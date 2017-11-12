package org.spoofax.jsglr2.characters;

public final class CharactersOptimized implements ICharacters {

    private final long word0; // [0, 63]
    private final long word1; // [64, 127]
    private final long word2; // [128, 191]
    private final long word3; // [192, 255]
    private final boolean containsEOF; // [256]

    public CharactersOptimized(long word0, long word1, long word2, long word3, boolean containsEOF) {
        this.word0 = word0;
        this.word1 = word1;
        this.word2 = word2;
        this.word3 = word3;
        this.containsEOF = containsEOF;
    }

    public final boolean containsCharacter(int character) {
        final int wordIndex = character >> CharacterClassRangeSet.BITMAP_SEGMENT_SIZE;
        final long word;

        switch(wordIndex) {
            case -2:
                word = word0;
                break;
            case -1:
                word = word1;
                break;
            case 0:
                word = word2;
                break;
            case 1:
                word = word3;
                break;
            default:
                word = 0L;
        }

        return (word & (1L << character)) != 0;
    }

    public final boolean containsEOF() {
        return containsEOF;
    }

    public final <C extends Number & Comparable<C>> CharacterClassRangeSet<C>
        rangeSetUnion(CharacterClassRangeSet<C> rangeSet) {
        throw new IllegalStateException();
    }

    @Override public final String toString() {
        return "{..optimized..}";
    }

}
