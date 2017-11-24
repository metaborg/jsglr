package org.spoofax.jsglr2.characters;

public class CharacterClassFactory implements ICharacterClassFactory {

    final private boolean optimize;

    public CharacterClassFactory(boolean optimize) {
        this.optimize = optimize;
    }

    public CharacterClassRangeSet fromEmpty() {
        return CharacterClassRangeSet.EMPTY_CONSTANT;
    }

    @Override public final ICharacterClass fromSingle(int character) {
        return new CharacterClassSingle(character);
    }

    @Override public final ICharacterClass fromRange(int from, int to) {
        return fromEmpty().addRange(from, to);
    }

    @Override public final ICharacterClass union(ICharacterClass a, ICharacterClass b) {
        boolean aIsRangeSet = a instanceof CharacterClassRangeSet;
        boolean bIsRangeSet = b instanceof CharacterClassRangeSet;

        if(aIsRangeSet || bIsRangeSet) {
            CharacterClassRangeSet rangeSet;
            ICharacterClass other;

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

    public ICharacterClass finalize(ICharacterClass characterClass) {
        if(characterClass instanceof CharacterClassRangeSet && optimize)
            return ((CharacterClassRangeSet) characterClass).optimized();
        else
            return characterClass;
    }


}
