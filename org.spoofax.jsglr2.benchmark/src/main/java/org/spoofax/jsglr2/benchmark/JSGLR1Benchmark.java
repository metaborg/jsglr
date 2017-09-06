package org.spoofax.jsglr2.benchmark;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.NullTreeBuilder;
import org.spoofax.jsglr.client.ParseException;
import org.spoofax.jsglr.client.SGLR;
import org.spoofax.jsglr.shared.BadTokenException;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr.shared.TokenExpectedException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.util.WithJSGLR1;
import org.spoofax.terms.ParseError;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public abstract class JSGLR1Benchmark extends BaseBenchmark implements WithJSGLR1 {

    protected SGLR  jsglr1default;
    protected SGLR  jsglr1WithRecovery;
    protected SGLR  jsglr1noTree;
    protected SGLR  jsglr1noTreeWithRecovery;
    
    protected JSGLR1Benchmark(String parseTableFileName) {
        super(parseTableFileName);
    }
    
    @Param({"default"})
    public String parseForestRepresentation;
    
    @Param({"false"})
    public boolean elkhound;
    
    @Param({"true", "false"})
    public boolean recovery;
    
    @Setup
    public void prepare() throws ParseError, ParseTableReadException, IOException, InvalidParseTableException {
        setupWithParseTable(parseTableFileName);

        jsglr1default = getJSGLR1();
        
        jsglr1WithRecovery = getJSGLR1();
        jsglr1WithRecovery.setUseStructureRecovery(true);
        
        jsglr1noTree = getJSGLR1();
        jsglr1noTree.setTreeBuilder(new NullTreeBuilder());
        
        jsglr1noTreeWithRecovery = getJSGLR1();
        jsglr1noTreeWithRecovery.setTreeBuilder(new NullTreeBuilder());
        jsglr1noTreeWithRecovery.setUseStructureRecovery(true);
        
        inputs = getInputs();
    }
    
    protected abstract List<Input> getInputs() throws IOException;
    
    @Benchmark
    public void jsglr1default(Blackhole bh) throws ParseTableReadException, TokenExpectedException, BadTokenException, ParseException, SGLRException, InterruptedException {
        for (Input input : inputs)
            bh.consume(jsglr1default.parse(input.content, null, null));
    }
    
    @Benchmark
    public void jsglr1WithRecovery(Blackhole bh) throws ParseTableReadException, TokenExpectedException, BadTokenException, ParseException, SGLRException, InterruptedException {
        for (Input input : inputs)
            bh.consume(jsglr1WithRecovery.parse(input.content, null, null));
    }
    
    @Benchmark
    public void jsglr1noTree(Blackhole bh) throws ParseTableReadException, TokenExpectedException, BadTokenException, ParseException, SGLRException, InterruptedException {
        for (Input input : inputs)
            bh.consume(jsglr1noTree.parse(input.content, null, null));
    }
    
    @Benchmark
    public void jsglr1noTreeWithRecovery(Blackhole bh) throws ParseTableReadException, TokenExpectedException, BadTokenException, ParseException, SGLRException, InterruptedException {
        for (Input input : inputs)
            bh.consume(jsglr1noTreeWithRecovery.parse(input.content, null, null));
    }

}
