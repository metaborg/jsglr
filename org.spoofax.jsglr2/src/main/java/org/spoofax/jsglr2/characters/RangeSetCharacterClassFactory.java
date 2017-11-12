package org.spoofax.jsglr2.characters;

public abstract class RangeSetCharacterClassFactory<C extends Number & Comparable<C>>
    implements ICharacterClassFactory {

    final private boolean optimize;

    protected RangeSetCharacterClassFactory(boolean optimize) {
        this.optimize = optimize;
    }

    public ICharacters fromEmpty() {
        return emptyRangeSet();
    }

    protected abstract CharacterClassRangeSet<C> emptyRangeSet();

    @Override public final ICharacters fromSingle(int character) {
        if(character == ICharacters.EOF_INT)
            return new CharacterClassEOF();
        else
            return new CharacterClassSingle(character);
    }

    @Override public final ICharacters fromRange(int from, int to) {
        return emptyRangeSet().addRange(from, to);
    }

    @Override public final ICharacters union(ICharacters a, ICharacters b) {
        boolean aIsRangeSet = a instanceof CharacterClassRangeSet;
        boolean bIsRangeSet = b instanceof CharacterClassRangeSet;

        if(aIsRangeSet || bIsRangeSet) {
            CharacterClassRangeSet<C> rangeSet;
            ICharacters other;

            if(aIsRangeSet) {
                rangeSet = (CharacterClassRangeSet<C>) a;
                other = b;
            } else {
                rangeSet = (CharacterClassRangeSet<C>) b;
                other = a;
            }

            return other.rangeSetUnion(rangeSet);
        } else {
            CharacterClassRangeSet<C> result = emptyRangeSet();

            result = a.rangeSetUnion(result);
            result = b.rangeSetUnion(result);

            return result;
        }
    }

    public ICharacters optimize(ICharacters characters) {
        if(characters instanceof CharacterClassRangeSet && optimize)
            return ((CharacterClassRangeSet) characters).optimized();
        else
            return characters;
    }


}
