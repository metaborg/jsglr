package org.spoofax.jsglr2.measure;

import java.util.HashSet;
import java.util.Set;

import org.spoofax.jsglr2.characterclasses.CharacterClassFactory;
import org.spoofax.jsglr2.characterclasses.ICharacterClass;

public class MeasureCharacterClassFactory extends CharacterClassFactory {

    public int characterClassesCount = 0;

    public Set<ICharacterClass> characterClassesUnique = new HashSet<>();
    public Set<ICharacterClass> characterClassesOptimizedUnique = new HashSet<>();

    protected MeasureCharacterClassFactory() {
        super(true, true);
    }

    @Override public ICharacterClass finalize(ICharacterClass characters) {
        characterClassesCount++;

        characterClassesUnique.add(characters);

        ICharacterClass optimized = super.finalize(characters);

        characterClassesOptimizedUnique.add(characters);

        return optimized;
    }

}
