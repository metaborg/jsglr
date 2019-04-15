package org.spoofax.jsglr2.testset;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.metaborg.characterclasses.CharacterClassFactory;
import org.metaborg.parsetable.IParseTable;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.JSGLR2Variants;
import org.spoofax.jsglr2.actions.ActionsFactory;
import org.spoofax.jsglr2.integration.Sdf3ToParseTable;
import org.spoofax.jsglr2.integration.WithParseTableFromTerm;
import org.spoofax.jsglr2.parsetable.ParseTableReader;
import org.spoofax.jsglr2.states.StateFactory;
import org.spoofax.terms.TermFactory;
import org.spoofax.terms.io.binary.TermReader;

public abstract class TestSetReader<Input> implements WithParseTableFromTerm {

    protected final TestSet testSet;

    protected TermReader termReader;

    protected IStrategoTerm parseTableTerm;

    protected TestSetReader(TestSet testSet) {
        this.testSet = testSet;

        this.termReader = new TermReader(new TermFactory());

        try {
            setupTestSetParseTable();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public IParseTable getParseTable(JSGLR2Variants.ParseTableVariant variant) throws Exception {
        return new ParseTableReader(new CharacterClassFactory(true, true), new ActionsFactory(true),
            new StateFactory(variant.actionsForCharacterRepresentation, variant.productionToGotoRepresentation))
                .read(getParseTableTerm());
    }

    private void setupTestSetParseTable() throws Exception {
        switch(testSet.parseTable.source) {
            case ATERM:
                TestSetParseTableFromATerm testSetParseTableFromATerm = (TestSetParseTableFromATerm) testSet.parseTable;

                setParseTableFromTermFile("/parsetables/" + testSetParseTableFromATerm.name + ".tbl");

                break;
            case SDF3:
                TestSetParseTableFromSDF3 testSetParseTableFromSDF3 = (TestSetParseTableFromSDF3) testSet.parseTable;

                Sdf3ToParseTable sdf3ToParseTable = new Sdf3ToParseTable(resource -> basePath() + resource);

                this.parseTableTerm = sdf3ToParseTable.getParseTableTerm(testSetParseTableFromSDF3.name + ".sdf3");

                break;
            default:
                throw new IllegalStateException("invalid parse table source");
        }
    }

    protected abstract String basePath();

    public abstract InputStream resourceInputStream(String filename) throws Exception;

    public TermReader getTermReader() {
        return termReader;
    }

    public IStrategoTerm getParseTableTerm() {
        return parseTableTerm;
    }

    public void setParseTableTerm(IStrategoTerm parseTableTerm) {
        this.parseTableTerm = parseTableTerm;
    }

    public Iterable<Input> getInputs() throws IOException {
        switch(testSet.input.type) {
            case SINGLE:
                TestSetSingleInput testSetSingleInput = (TestSetSingleInput) testSet.input;

                return getSingleInput(testSetSingleInput.filename);
            case MULTIPLE:
                TestSetMultipleInputs testSetMultipleInputs = (TestSetMultipleInputs) testSet.input;

                return getMultipleInputs(testSetMultipleInputs.path, testSetMultipleInputs.extension);
            case SIZED:
            default:
                throw new IllegalStateException("invalid input type (does not have a size)");
        }
    }

    public Iterable<Input> getInputsForSize(int n) throws IOException {
        switch(testSet.input.type) {
            case SIZED:
                TestSetSizedInput testSizedInput = (TestSetSizedInput) testSet.input;

                return Arrays.asList(getInput("", testSizedInput.get(n)));
            case SINGLE:
            case MULTIPLE:
            default:
                throw new IllegalStateException("invalid input type (does have a size)");
        }
    }

    protected abstract String getFileAsString(String filename) throws IOException;

    protected String inputStreamAsString(InputStream inputStream) throws IOException {
        try(Scanner s = new Scanner(inputStream)) {
            s.useDelimiter("\\A");

            return s.hasNext() ? s.next() : "";
        }
    }

    protected List<Input> getSingleInput(String filename) throws IOException {
        Input input = getInput(filename, getFileAsString(filename));

        return Arrays.asList(input);
    }

    protected List<Input> getMultipleInputs(String path, String extension) throws IOException {
        List<Input> inputs = new ArrayList<>();

        for(File file : filesInPath(new File(path))) {
            String filename = file.getName();
            if(filename.endsWith("." + extension)) {
                String input = inputStreamAsString(new FileInputStream(file));

                inputs.add(getInput(filename, input));
            }
        }

        return inputs;
    }

    protected abstract Input getInput(String filename, String input);

    private Set<File> filesInPath(File path) {
        Set<File> acc = new HashSet<>();

        filesInPath(path, acc);

        return acc;
    }

    private Set<File> filesInPath(final File path, Set<File> acc) {
        File[] files = path.listFiles();
        if(files == null)
            throw new IllegalStateException("Directory " + path + " not found!");
        for(final File subPath : files) {
            if(subPath.isDirectory())
                filesInPath(subPath, acc);
            else
                acc.add(subPath);
        }

        return acc;
    }

}
