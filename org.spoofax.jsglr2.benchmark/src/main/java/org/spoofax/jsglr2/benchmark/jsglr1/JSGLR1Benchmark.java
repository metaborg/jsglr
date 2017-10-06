package org.spoofax.jsglr2.benchmark.jsglr1;

import java.io.IOException;
import java.net.URISyntaxException;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.infra.Blackhole;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.NullTreeBuilder;
import org.spoofax.jsglr.client.ParseException;
import org.spoofax.jsglr.client.SGLR;
import org.spoofax.jsglr.shared.BadTokenException;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr.shared.TokenExpectedException;
import org.spoofax.jsglr2.benchmark.BaseBenchmark;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.testset.Input;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.jsglr2.util.WithJSGLR1;
import org.spoofax.terms.ParseError;

public abstract class JSGLR1Benchmark extends BaseBenchmark implements WithJSGLR1 {
    
    protected JSGLR1Benchmark(TestSet testSet) {
		super(testSet);
	}

	protected SGLR  jsglr1default;
    protected SGLR  jsglr1WithRecovery;
    protected SGLR  jsglr1noTree;
    protected SGLR  jsglr1noTreeWithRecovery;
    
    @Setup
    public void prepare() throws ParseError, ParseTableReadException, IOException, InvalidParseTableException, InterruptedException, URISyntaxException {
    		jsglr1default = getJSGLR1();
        
        jsglr1WithRecovery = getJSGLR1();
        jsglr1WithRecovery.setUseStructureRecovery(true);
        
        jsglr1noTree = getJSGLR1();
        jsglr1noTree.setTreeBuilder(new NullTreeBuilder());
        
        jsglr1noTreeWithRecovery = getJSGLR1();
        jsglr1noTreeWithRecovery.setTreeBuilder(new NullTreeBuilder());
        jsglr1noTreeWithRecovery.setUseStructureRecovery(true);
    }
    
    public IStrategoTerm getParseTableTerm() {
    		return testSetReader.getParseTableTerm();
    }
    
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
