package org.spoofax.jsglr2.characterclasses;

import org.spoofax.jsglr2.util.Cache;

public class CharacterClassFactory implements ICharacterClassFactory {

    final private boolean optimize;
    final private boolean cache;

    private Cache<ICharacterClass> characterClassCache;
    
    public CharacterClassFactory(boolean optimize, boolean cache) {
        this.optimize = optimize;
        this.cache = cache;
        
        if (cache)
        		this.characterClassCache = new Cache<>();
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
    		ICharacterClass optimized;
    	
        if(characterClass instanceof CharacterClassRangeSet && optimize)
        		optimized = ((CharacterClassRangeSet) characterClass).optimized();
        else
        		optimized = characterClass;
        
        if (cache)
        		return characterClassCache.cached(optimized);
        else
        		return optimized;
    }


}
