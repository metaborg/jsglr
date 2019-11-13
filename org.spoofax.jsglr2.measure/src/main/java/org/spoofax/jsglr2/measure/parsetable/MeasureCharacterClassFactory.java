package org.spoofax.jsglr2.measure.parsetable;

import java.util.HashSet;
import java.util.Set;

import org.metaborg.parsetable.characterclasses.CharacterClassFactory;
import org.metaborg.parsetable.characterclasses.ICharacterClass;

public class MeasureCharacterClassFactory extends CharacterClassFactory {

    private static final long serialVersionUID = 5184928823095118233L;

    int characterClassesCount = 0;

    Set<ICharacterClass> characterClassesUnique = new HashSet<>();
    Set<ICharacterClass> characterClassesOptimizedUnique = new HashSet<>();

    MeasureCharacterClassFactory() {
        super();
    }

    @Override public ICharacterClass finalize(ICharacterClass characters) {
        characterClassesCount++;

        characterClassesUnique.add(characters);

        ICharacterClass optimized = super.finalize(characters);

        characterClassesOptimizedUnique.add(characters);

        return optimized;
    }

}
