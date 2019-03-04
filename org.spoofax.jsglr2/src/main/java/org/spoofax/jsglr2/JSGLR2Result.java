package org.spoofax.jsglr2;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;

public abstract class JSGLR2Result<ParseForest extends IParseForest, AbstractSyntaxTree> {

    public final AbstractParse<ParseForest, ?> parse;
    public final boolean isSuccess;

    protected JSGLR2Result(AbstractParse<ParseForest, ?> parse, boolean isSuccess) {
        this.parse = parse;
        this.isSuccess = isSuccess;
    }

}
