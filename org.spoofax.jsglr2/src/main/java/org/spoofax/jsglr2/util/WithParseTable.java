package org.spoofax.jsglr2.util;

import java.io.IOException;
import java.io.InputStream;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.terms.ParseError;
import org.spoofax.terms.io.binary.TermReader;

public interface WithParseTable {

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
