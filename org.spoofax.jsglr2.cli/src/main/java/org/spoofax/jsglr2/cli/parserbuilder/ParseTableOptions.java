package org.spoofax.jsglr2.cli.parserbuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.cli.JSGLR2CLI;
import org.spoofax.jsglr2.cli.WrappedException;

import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Option;

@SuppressWarnings("WeakerAccess")
public class ParseTableOptions {
    @Option(names = { "-t", "--pt", "--parseTable" }, description = "Parse table file") public String parseTableFile;

    @ArgGroup(exclusive = false,
        heading = "Parse table variant%n") public ParserBuilder.ParseTableVariantOptions parseTableVariant =
            new ParserBuilder.ParseTableVariantOptions();

    IParseTable getParseTable() throws WrappedException {
        try {
            final InputStream parseTableInputStream;
            if(JSGLR2CLI.language != null) {
                ZipFile languageZip = new ZipFile(SpoofaxLanguageFinder.getSpoofaxLanguage(JSGLR2CLI.language));
                parseTableInputStream = languageZip.getInputStream(languageZip.getEntry("target/metaborg/sdf.tbl"));
            } else {
                parseTableInputStream = new FileInputStream(parseTableFile);
            }

            return parseTableVariant.getVariant().parseTableReader().read(parseTableInputStream);
        } catch(IOException e) {
            throw new WrappedException("Invalid parse table file", e);
        } catch(ParseTableReadException e) {
            throw new WrappedException("Invalid parse table", e);
        }
    }

}
