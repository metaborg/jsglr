package org.spoofax.jsglr2.testset;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.util.WithGrammar;
import org.spoofax.terms.TermFactory;
import org.spoofax.terms.io.binary.TermReader;

public abstract class TestSetReader implements WithGrammar {

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

    private void setupTestSetParseTable() throws Exception {
        switch(testSet.parseTable.source) {
            case ATERM:
                TestSetParseTableFromATerm testSetParseTableFromATerm = (TestSetParseTableFromATerm) testSet.parseTable;

                setupParseTableFile(testSetParseTableFromATerm.name);
                setupParseTable(testSetParseTableFromATerm.name);

                break;
            case GRAMMAR_DEF:
                TestSetParseTableFromGrammarDef testSetParseTableFromGrammarDef =
                    (TestSetParseTableFromGrammarDef) testSet.parseTable;

                setupParseTableFromDefFile(testSetParseTableFromGrammarDef.name);

                break;
            default:
                throw new IllegalStateException("invalid parse table source");
        }
    }

    public abstract void setupParseTableFile(String name) throws IOException;

    @Override
    public TermReader getTermReader() {
        return termReader;
    }

    @Override
    public IStrategoTerm getParseTableTerm() {
        return parseTableTerm;
    }

    @Override
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

                return Arrays.asList(testSizedInput.get(n));
            case SINGLE:
            case MULTIPLE:
            default:
                throw new IllegalStateException("invalid input type (does have a size)");
        }
    }

    public Iterable<InputBatch> getInputBatches() throws IOException {
        switch(testSet.input.type) {
            case SINGLE:
                TestSetSingleInput testSetSingleInput = (TestSetSingleInput) testSet.input;

                return Arrays.asList(new InputBatch(getSingleInput(testSetSingleInput.filename), -1));
            case MULTIPLE:
                TestSetMultipleInputs testSetMultipleInputs = (TestSetMultipleInputs) testSet.input;

                return Arrays.asList(
                    new InputBatch(getMultipleInputs(testSetMultipleInputs.path, testSetMultipleInputs.extension), -1));
            case SIZED:
                TestSetSizedInput testSizedInput = (TestSetSizedInput) testSet.input;

                if(testSizedInput.sizes == null)
                    throw new IllegalStateException("invalid input type (sizes missing)");

                List<InputBatch> result = new ArrayList<InputBatch>();

                for(int size : testSizedInput.sizes) {
                    result.add(new InputBatch(testSizedInput.get(size), size));
                }

                return result;
            default:
                throw new IllegalStateException("invalid input type (does have a size)");
        }
    }

    public class InputBatch {
        public Iterable<Input> inputs;
        public int size;

        public InputBatch(Iterable<Input> inputs, int size) {
            this.inputs = inputs;
            this.size = size;
        }

        public InputBatch(Input input, int size) {
            this.inputs = Arrays.asList(input);
            this.size = size;
        }
    }

    protected abstract String getFileAsString(String filename) throws IOException;

    @SuppressWarnings("resource")
    protected String inputStreamAsString(InputStream inputStream) throws IOException {
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");

        return s.hasNext() ? s.next() : "";
    }

    protected List<Input> getSingleInput(String filename) throws IOException {
        Input input = new Input(filename, getFileAsString(filename));

        return Arrays.asList(input);
    }

    protected List<Input> getMultipleInputs(String path, String extension) throws IOException {
        List<Input> inputs = new ArrayList<Input>();

        for(File file : filesInPath(new File(path))) {
            if(file.getName().endsWith("." + extension)) {
                String input = inputStreamAsString(new FileInputStream(file));

                inputs.add(new Input(file.getName(), input));
            }
        }

        return inputs;
    }

    private Set<File> filesInPath(File path) {
        Set<File> acc = new HashSet<File>();

        filesInPath(path, acc);

        return acc;
    }

    private Set<File> filesInPath(final File path, Set<File> acc) {
        for(final File subPath : path.listFiles()) {
            if(subPath.isDirectory())
                filesInPath(subPath, acc);
            else
                acc.add(subPath);
        }

        return acc;
    }

}
