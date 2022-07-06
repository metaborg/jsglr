package org.spoofax.jsglr2.inputstack.incremental;

import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.parser.observing.ParserObserving;

public interface IncrementalInputStackFactory<InputStack extends IIncrementalInputStack> {

    InputStack get(String inputString, String previousInput, IncrementalParseForest previousResult,
        ParserObserving observing);

}
