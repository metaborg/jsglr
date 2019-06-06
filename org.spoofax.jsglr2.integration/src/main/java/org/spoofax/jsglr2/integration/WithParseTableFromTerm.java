package org.spoofax.jsglr2.integration;

import java.io.InputStream;

import org.metaborg.characterclasses.CharacterClassFactory;
import org.metaborg.parsetable.IParseTable;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.actions.ActionsFactory;
import org.spoofax.jsglr2.parsetable.ParseTableReader;
import org.spoofax.jsglr2.states.StateFactory;
import org.spoofax.terms.io.binary.TermReader;

public interface WithParseTableFromTerm extends WithParseTable {

    default IParseTable getParseTable(ParseTableVariant variant) throws Exception {
        return new ParseTableReader(new CharacterClassFactory(true, true), new ActionsFactory(true),
            new StateFactory(variant.actionsForCharacterRepresentation, variant.productionToGotoRepresentation))
                .read(getParseTableTerm());
    }

    TermReader getTermReader();

    IStrategoTerm getParseTableTerm();

    void setParseTableTerm(IStrategoTerm parseTableTerm);

    default void setParseTableFromTermFile(String parseTableFilename) throws Exception {
        IStrategoTerm parseTableTerm = parseTableTerm(parseTableFilename);

        setParseTableTerm(parseTableTerm);
    }

    InputStream resourceInputStream(String resource) throws Exception;

    default IStrategoTerm parseTableTerm(String filename) throws Exception {
        InputStream inputStream = resourceInputStream(filename);

        return getTermReader().parseFromStream(inputStream);
    }

}
