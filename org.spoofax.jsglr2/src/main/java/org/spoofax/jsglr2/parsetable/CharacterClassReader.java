package org.spoofax.jsglr2.parsetable;

import static org.spoofax.terms.Term.*;

import org.metaborg.characterclasses.ICharacterClassFactory;
import org.metaborg.parsetable.characterclasses.ICharacterClass;
import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoTerm;

public class CharacterClassReader {

    final ICharacterClassFactory characterClassFactory;

    public CharacterClassReader(ICharacterClassFactory characterClassFactory) {
        this.characterClassFactory = characterClassFactory;
    }

    public ICharacterClass read(IStrategoList characterClassTermList) {
        ICharacterClass characterClass = null;

        for(IStrategoTerm characterClassTerm : characterClassTermList) {
            ICharacterClass characterClassForTerm;

            if(isTermInt(characterClassTerm)) {
                characterClassForTerm = characterClassFactory.fromSingle(javaInt(characterClassTerm));
            } else {
                int from = intAt(characterClassTerm, 0);
                int to = intAt(characterClassTerm, 1);

                characterClassForTerm = characterClassFactory.fromRange(from, to);
            }

            if(characterClass == null)
                characterClass = characterClassForTerm;
            else if(characterClassForTerm != null)
                characterClass = characterClass.union(characterClassForTerm);
        }

        return characterClassFactory.finalize(characterClass);
    }

}
