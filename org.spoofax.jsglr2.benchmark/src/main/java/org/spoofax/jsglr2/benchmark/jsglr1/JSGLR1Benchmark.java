package org.spoofax.jsglr2.benchmark.jsglr1;

import java.io.IOException;
import java.net.URISyntaxException;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
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
import org.spoofax.jsglr2.integration.WithJSGLR1;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.testset.Input;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.terms.ParseError;

public abstract class JSGLR1Benchmark extends BaseBenchmark implements WithJSGLR1 {

    protected SGLR jsglr1parse;
    protected SGLR jsglr1parseAndImplode;

    protected JSGLR1Benchmark(TestSet testSet) {
        super(testSet);
    }

    @Param({ "false", "true" }) public boolean implode;

    @Setup
    public void prepare() throws ParseError, ParseTableReadException, IOException, InvalidParseTableException,
        InterruptedException, URISyntaxException {
        jsglr1parseAndImplode = getJSGLR1();

        jsglr1parse = getJSGLR1();
        jsglr1parse.setTreeBuilder(new NullTreeBuilder());
    }

    public IStrategoTerm getParseTableTerm() {
        return testSetReader.getParseTableTerm();
    }

    @Benchmark
    public void jsglr1default(Blackhole bh) throws ParseTableReadException, TokenExpectedException, BadTokenException,
        ParseException, SGLRException, InterruptedException {
        if(implode) {
            for(Input input : inputs)
                bh.consume(jsglr1parseAndImplode.parse(input.content, null, null));
        } else {
            for(Input input : inputs)
                bh.consume(jsglr1parse.parse(input.content, null, null));
        }
    }

}
