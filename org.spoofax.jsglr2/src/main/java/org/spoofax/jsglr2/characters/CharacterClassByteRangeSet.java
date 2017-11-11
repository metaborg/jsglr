package org.spoofax.jsglr2.characters;

import com.google.common.collect.ImmutableRangeSet;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;

public final class CharacterClassByteRangeSet extends CharacterClassRangeSet<Byte> {

    static final CharacterClassByteRangeSet EMPTY_CONSTANT = new CharacterClassByteRangeSet();

    protected CharacterClassByteRangeSet() {
        super(ImmutableRangeSet.copyOf(TreeRangeSet.create()), false);
    }

    protected CharacterClassByteRangeSet(final ImmutableRangeSet<Byte> rangeSet, boolean containsEOF) {
        super(rangeSet, containsEOF);
    }

    protected final CharacterClassRangeSet<Byte> from(ImmutableRangeSet<Byte> rangeSet, boolean containsEOF) {
        return new CharacterClassByteRangeSet(rangeSet, containsEOF);
    }

    protected final Byte byteToInternalNumber(byte b) {
        return b;
    }

    protected final CharacterClassRangeSet<Byte> union(CharacterClassRangeSet<Byte> other) {
        final RangeSet<Byte> mutableRangeSet = TreeRangeSet.create();

        mutableRangeSet.addAll(this.rangeSet);
        mutableRangeSet.addAll(other.rangeSet);

        boolean containsEOF = this.containsEOF || other.containsEOF;

        return from(ImmutableRangeSet.copyOf(mutableRangeSet), containsEOF);
    }

}
