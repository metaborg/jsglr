package org.spoofax.jsglr2.imploder;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Parse;

public interface IImploder<ParseForest extends AbstractParseForest, AbstractSyntaxTree> {

    public ImplodeResult<ParseForest, AbstractSyntaxTree> implode(Parse<ParseForest, ?> parse, ParseForest parseForest);

}
