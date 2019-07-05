package org.spoofax.jsglr2.integration;

import java.io.InputStream;

import org.metaborg.parsetable.IParseTable;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.terms.io.binary.TermReader;

public interface WithParseTableFromTerm extends WithParseTable {

    default IParseTable getParseTable(ParseTableVariant variant) throws Exception {
        return variant.parseTableReader().read(getParseTableTerm());
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
