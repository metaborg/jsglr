package org.spoofax.jsglr2.parsetable.symbols;

import org.metaborg.parsetable.characterclasses.ICharacterClass;

public interface ITerminalSymbol extends ISymbol {

    default SyntaxContext syntaxContext() {
        return SyntaxContext.Lexical;
    }

    public ICharacterClass characterClass();

}
