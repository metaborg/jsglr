package org.spoofax.jsglr2.integration;

import java.io.FileInputStream;
import java.io.InputStream;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.ParseTableVariant;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.terms.io.binary.TermReader;

public interface WithParseTableFromTerm extends WithParseTable {

    default ParseTableWithOrigin getParseTable(ParseTableVariant variant) throws Exception {
        return new ParseTableWithOrigin(getParseTableFromTerm(variant), ParseTableOrigin.ATerm);
    }

    default IParseTable getParseTableFromTerm(ParseTableVariant variant) throws Exception {
        return variant.parseTableReader().read(getParseTableTerm());
    }

    TermReader getTermReader();

    IStrategoTerm getParseTableTerm();

    void setParseTableTerm(IStrategoTerm parseTableTerm);

    default void setParseTableFromTermResource(String parseTableFilename) throws Exception {
        setParseTableTerm(parseTableTerm(resourceInputStream(parseTableFilename)));
    }

    InputStream resourceInputStream(String resource) throws Exception;

    default void setParseTableFromTermFile(String parseTablePath) throws Exception {
        setParseTableTerm(parseTableTerm(new FileInputStream(parseTablePath)));
    }

    default IStrategoTerm parseTableTerm(InputStream inputStream) throws Exception {
        return getTermReader().parseFromStream(inputStream);
    }

}
