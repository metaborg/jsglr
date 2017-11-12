package org.spoofax.jsglr2.characters;

import com.google.common.collect.ImmutableRangeSet;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;

public final class CharacterClassIntegerRangeSet extends CharacterClassRangeSet<Integer> {

    static final CharacterClassIntegerRangeSet EMPTY_CONSTANT = new CharacterClassIntegerRangeSet();

    protected CharacterClassIntegerRangeSet() {
        super(ImmutableRangeSet.copyOf(TreeRangeSet.create()), false);
    }

    protected CharacterClassIntegerRangeSet(final ImmutableRangeSet<Integer> rangeSet, boolean containsEOF) {
        super(rangeSet, containsEOF);
    }

    protected final CharacterClassRangeSet<Integer> from(ImmutableRangeSet<Integer> rangeSet, boolean containsEOF) {
        return new CharacterClassIntegerRangeSet(rangeSet, containsEOF);
    }

    protected final Integer intToInternalNumber(int i) {
        return i - 128;
    }

    protected final CharacterClassRangeSet<Integer> union(CharacterClassRangeSet<Integer> other) {
        final RangeSet<Integer> mutableRangeSet = TreeRangeSet.create();

        mutableRangeSet.addAll(this.rangeSet);
        mutableRangeSet.addAll(other.rangeSet);

        boolean containsEOF = this.containsEOF || other.containsEOF;

        return from(ImmutableRangeSet.copyOf(mutableRangeSet), containsEOF);
    }

}
