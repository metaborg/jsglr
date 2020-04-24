package org.spoofax.jsglr2.benchmark.jsglr1;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.infra.Blackhole;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.NullTreeBuilder;
import org.spoofax.jsglr.client.SGLR;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr2.benchmark.BaseBenchmark;
import org.spoofax.jsglr2.integration.WithJSGLR1;
import org.spoofax.jsglr2.testset.TestSetReader;
import org.spoofax.jsglr2.testset.TestSetWithParseTableReader;
import org.spoofax.jsglr2.testset.testinput.StringInput;
import org.spoofax.terms.ParseError;

public abstract class JSGLR1Benchmark extends BaseBenchmark<String, StringInput> implements WithJSGLR1 {

    protected TestSetWithParseTableReader<String, StringInput> testSetReader;

    protected SGLR jsglr1parse;
    protected SGLR jsglr1parseAndImplode;

    @Param({ "false", "true" }) public boolean implode;

    protected void setTestSetReader(TestSetWithParseTableReader<String, StringInput> testSetReader) {
        super.setTestSetReader(testSetReader);
        this.testSetReader = testSetReader;
    }

    @Setup public void prepare() throws ParseError, InvalidParseTableException {
        jsglr1parseAndImplode = getJSGLR1();

        jsglr1parse = getJSGLR1();
        jsglr1parse.setTreeBuilder(new NullTreeBuilder());
    }

    public IStrategoTerm getParseTableTerm() {
        return testSetReader.getParseTableTerm();
    }

    @Benchmark public void jsglr1default(Blackhole bh) throws SGLRException, InterruptedException {
        if(implode) {
            for(StringInput input : inputs)
                bh.consume(jsglr1parseAndImplode.parse(input.content, null, null));
        } else {
            for(StringInput input : inputs)
                bh.consume(jsglr1parse.parse(input.content, null, null));
        }
    }

}
