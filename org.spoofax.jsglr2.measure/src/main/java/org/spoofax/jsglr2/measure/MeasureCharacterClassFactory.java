package org.spoofax.jsglr2.measure;

import java.util.HashSet;
import java.util.Set;

import org.spoofax.jsglr2.characters.CharacterClassFactory;
import org.spoofax.jsglr2.characters.ICharacters;

public class MeasureCharacterClassFactory extends CharacterClassFactory {

    public int characterClassesCount = 0;

    public Set<ICharacters> characterClassesUnique = new HashSet<>();
    public Set<ICharacters> characterClassesOptimizedUnique = new HashSet<>();

    protected MeasureCharacterClassFactory() {
        super(true);
    }

    @Override public ICharacters finalize(ICharacters characters) {
        characterClassesCount++;

        characterClassesUnique.add(characters);

        ICharacters optimized = super.finalize(characters);

        characterClassesOptimizedUnique.add(characters);

        return optimized;
    }

}
