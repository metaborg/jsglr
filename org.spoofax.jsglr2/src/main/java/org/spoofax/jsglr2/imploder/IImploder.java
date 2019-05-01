package org.spoofax.jsglr2.imploder;

import org.spoofax.jsglr2.parseforest.IParseForest;

public interface IImploder<ParseForest extends IParseForest, AbstractSyntaxTree> {

    ImplodeResult<AbstractSyntaxTree> implode(String input, String filename, ParseForest parseForest);

}
