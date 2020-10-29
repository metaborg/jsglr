package org.spoofax.jsglr2.benchmark.jsglr2.util;

import java.util.ArrayList;
import java.util.List;

import org.spoofax.jsglr2.JSGLR2;
import org.spoofax.jsglr2.JSGLR2ImplementationWithCache;
import org.spoofax.jsglr2.parser.ParseException;

/**
 * This wrapper for JSGLR2 clears the cache after every call to parse. When parsing multiple inputs, the results are
 * cached between subsequent inputs as normal.
 */
public class JSGLR2MultiParser<AbstractSyntaxTree> {
    public final JSGLR2<AbstractSyntaxTree> jsglr2;

    public JSGLR2MultiParser(JSGLR2<AbstractSyntaxTree> jsglr2) {
        this.jsglr2 = jsglr2;
    }

    public List<AbstractSyntaxTree> parse(String... inputs) throws ParseException {
        int n = inputs.length;
        List<AbstractSyntaxTree> res = new ArrayList<>(n);
        String fileName = "" + System.currentTimeMillis();

        for(String input : inputs) {
            res.add(jsglr2.parseUnsafe(input, fileName, null));
        }

        if(jsglr2 instanceof JSGLR2ImplementationWithCache) {
            ((JSGLR2ImplementationWithCache<?, ?, ?, AbstractSyntaxTree, ?, ?>) this.jsglr2).clearCache();
        }

        return res;
    }

}
