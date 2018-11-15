package org.spoofax.jsglr2.util;

import java.io.IOException;
import java.io.InputStream;

import org.metaborg.characterclasses.CharacterClassFactory;
import org.metaborg.parsetable.IParseTable;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.JSGLR2Variants;
import org.spoofax.jsglr2.actions.ActionsFactory;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.parsetable.ParseTableReader;
import org.spoofax.jsglr2.states.StateFactory;
import org.spoofax.terms.ParseError;
import org.spoofax.terms.io.binary.TermReader;

public interface WithParseTableFromTerm extends WithParseTable {

    default IParseTable getParseTable(JSGLR2Variants.ParseTableVariant variant) throws ParseTableReadException {
        return new ParseTableReader(new CharacterClassFactory(true, true), new ActionsFactory(true),
            new StateFactory(variant.actionsForCharacterRepresentation, variant.productionToGotoRepresentation))
                .read(getParseTableTerm());
    }

    TermReader getTermReader();

    IStrategoTerm getParseTableTerm();

    void setParseTableTerm(IStrategoTerm parseTableTerm);

    default void setupParseTable(String parseTableFilename)
        throws ParseError, ParseTableReadException, IOException, InvalidParseTableException {
        setupParseTableByFilename("parsetables/" + parseTableFilename + ".tbl");
    }

    default void setupParseTableByFilename(String parseTableFilename)
        throws ParseError, ParseTableReadException, IOException, InvalidParseTableException {
        IStrategoTerm parseTableTerm = parseTableTerm(parseTableFilename);

        setParseTableTerm(parseTableTerm);
    }

    default IStrategoTerm parseTableTerm(String filename) throws ParseError, IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename);

        return getTermReader().parseFromStream(inputStream);
    }

}
