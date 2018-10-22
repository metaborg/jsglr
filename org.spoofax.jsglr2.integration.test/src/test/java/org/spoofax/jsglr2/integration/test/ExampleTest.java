package org.spoofax.jsglr2.integration.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.metaborg.parsetable.IParseTable;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.JSGLR2;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseForest;

public class ExampleTest extends BaseTest {

    @Test
    public void test() throws Exception {
        IParseTable parseTable = parseTableFromSDF3("test.sdf3");
        
        JSGLR2<HybridParseForest, IStrategoTerm> jsglr2 = JSGLR2.standard(parseTable);
        
        IStrategoTerm result = jsglr2.parse("1+2");
        
        assertEquals(result.toString(), "Add(Int(\"1\"),Int(\"2\"))");
    }

}