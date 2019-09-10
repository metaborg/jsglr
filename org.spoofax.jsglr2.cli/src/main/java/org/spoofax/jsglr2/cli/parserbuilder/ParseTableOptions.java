package org.spoofax.jsglr2.cli.parserbuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.cli.WrappedException;

import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Option;

@SuppressWarnings("WeakerAccess")
public class ParseTableOptions {
    @Option(names = { "-t", "--pt", "--parseTable" }, required = true,
        description = "Parse table file") private String parseTableFile;

    @ArgGroup(exclusive = false,
        heading = "Parse table variant%n") private ParserBuilder.ParseTableVariantOptions parseTableVariant =
            new ParserBuilder.ParseTableVariantOptions();

    IParseTable getParseTable() throws WrappedException {
        try {
            File file = new File(parseTableFile);
            final InputStream parseTableInputStream;
            if(file.isFile()) {
                parseTableInputStream = new FileInputStream(parseTableFile);
            } else {
                // If the parse table file cannot directly be found, try to dig into the local maven repository
                ZipFile languageZip = new ZipFile(SpoofaxLanguageFinder.getSpoofaxLanguage(this.parseTableFile));
                parseTableInputStream = languageZip.getInputStream(languageZip.getEntry("target/metaborg/sdf.tbl"));
            }

            return parseTableVariant.getVariant().parseTableReader().read(parseTableInputStream);
        } catch(IOException e) {
            throw new WrappedException("Invalid parse table file", e);
        } catch(ParseTableReadException e) {
            throw new WrappedException("Invalid parse table", e);
        }
    }

}
