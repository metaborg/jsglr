package org.spoofax.jsglr2.benchmark;

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
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.util.WithJSGLR1;
import org.spoofax.terms.ParseError;
import org.spoofax.terms.TermFactory;
import org.spoofax.terms.io.binary.TermReader;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public abstract class BaseBenchmark implements WithJSGLR1 {

    protected IStrategoTerm parseTableTerm;
    protected TermReader termReader;
    
    protected String parseTableFileName;
    protected List<Input> inputs;
    
    protected BaseBenchmark(String parseTableFileName) {
        this.parseTableFileName = parseTableFileName;
    }
    
    protected abstract List<Input> getInputs() throws IOException;
    
    class Input {
        public String filename;
        public String content;
        public Input(String filename, String content) {
            this.filename = filename;
            this.content = content;
        }
    }
    
    protected void setupWithParseTable(String parseTableFilename) throws ParseError, ParseTableReadException, IOException, InvalidParseTableException {
        this.termReader = new TermReader(new TermFactory());
        
        this.parseTableTerm = parseTableTermFromFile(parseTableFilename);
    }
    
    private IStrategoTerm parseTableTermFromFile(String filename) throws ParseError, IOException {
        InputStream inputStream = getClass().getResourceAsStream("/parsetables/" + filename);
        
        return this.termReader.parseFromStream(inputStream);
    }

    protected String getFileAsString(String filename) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/samples/" + filename);
        
        return inputStreamAsString(inputStream);
    }

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
        
        for (File file : filesInPath(new File(path))) {
            if (file.getName().endsWith("." + extension)) {
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
        for (final File subPath : path.listFiles()) {
            if (subPath.isDirectory())
                filesInPath(subPath, acc);
            else
                acc.add(subPath);
        }
        
        return acc;
    }

    public IStrategoTerm getParseTableTerm() {
        return parseTableTerm;
    }

}
