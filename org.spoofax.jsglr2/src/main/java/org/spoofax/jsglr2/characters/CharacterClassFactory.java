package org.spoofax.jsglr2.characters;

public final class CharacterClassFactory implements ICharacterClassFactory {

    final private boolean optimize;

    protected CharacterClassFactory(boolean optimize) {
        this.optimize = optimize;
    }

    public CharacterClassRangeSet fromEmpty() {
        return CharacterClassRangeSet.EMPTY_CONSTANT;
    }

    @Override public final ICharacters fromSingle(int character) {
        return new CharactersClassSingle(character);
    }

    @Override public final ICharacters fromRange(int from, int to) {
        return fromEmpty().addRange(from, to);
    }

    @Override public final ICharacters union(ICharacters a, ICharacters b) {
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
            CharacterClassRangeSet result = fromEmpty();

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
