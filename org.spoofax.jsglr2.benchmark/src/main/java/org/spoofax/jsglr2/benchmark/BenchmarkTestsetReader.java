package org.spoofax.jsglr2.benchmark;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.jsglr2.testset.TestSetReader;
import org.spoofax.terms.ParseError;

public class BenchmarkTestsetReader extends TestSetReader {

    public BenchmarkTestsetReader(TestSet testSet) {
        super(testSet);
    }

    private String basePath() {
        try {
            return new File(
                BenchmarkTestsetReader.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath())
                    .getParent();
        } catch(URISyntaxException e) {
            throw new IllegalStateException("base path for benchmarks could not be retrieved");
        }
    }

    @Override
    public IStrategoTerm parseTableTerm(String filename) throws ParseError, IOException {
        InputStream inputStream = new FileInputStream(basePath() + "/" + filename);

        return getTermReader().parseFromStream(inputStream);
    }

    @Override
    public String grammarsPath() {
        return basePath() + "/grammars";
    }

    @Override
    public void setupParseTableFile(String parseTableName) throws IOException {
        new File(basePath() + "/parsetables").mkdirs();

        InputStream defResourceInJar = getClass().getResourceAsStream("/parsetables/" + parseTableName + ".tbl");
        String destinationInTargetDir = basePath() + "/parsetables/" + parseTableName + ".tbl";

        Files.copy(defResourceInJar, Paths.get(destinationInTargetDir), StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public void setupDefFile(String grammarName) throws IOException {
        new File(basePath() + "/grammars").mkdirs();

        InputStream defResourceInJar = getClass().getResourceAsStream("/grammars/" + grammarName + ".def");
        String destinationInTargetDir = basePath() + "/grammars/" + grammarName + ".def";

        Files.copy(defResourceInJar, Paths.get(destinationInTargetDir), StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    protected String getFileAsString(String filename) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/samples/" + filename);

        return inputStreamAsString(inputStream);
    }

}
