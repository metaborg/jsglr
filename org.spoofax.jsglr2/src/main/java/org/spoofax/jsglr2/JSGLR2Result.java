package org.spoofax.jsglr2;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Parse;

public abstract class JSGLR2Result<ParseForest extends AbstractParseForest, AbstractSyntaxTree> {

    public final Parse<ParseForest, ?> parse;
    public final boolean isSuccess;

    protected JSGLR2Result(Parse<ParseForest, ?> parse, boolean isSuccess) {
        this.parse = parse;
        this.isSuccess = isSuccess;
    }

}
