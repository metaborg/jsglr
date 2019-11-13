package org.spoofax.jsglr2.measure.parsetable;

import java.util.HashSet;
import java.util.Set;

import org.metaborg.parsetable.characterclasses.CharacterClassFactory;
import org.metaborg.parsetable.characterclasses.ICharacterClass;

public class MeasureCharacterClassFactory extends CharacterClassFactory {

    private static final long serialVersionUID = 5184928823095118233L;

    public int characterClassesCount = 0;

    public Set<ICharacterClass> characterClassesUnique = new HashSet<>();
    public Set<ICharacterClass> characterClassesOptimizedUnique = new HashSet<>();

    protected MeasureCharacterClassFactory() {
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
