package org.spoofax.jsglr2.inputstack.incremental;

import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;

public interface IncrementalInputStackFactory<InputStack extends IIncrementalInputStack> {

    InputStack get(String inputString, String previousInput, IncrementalParseForest previousResult);

}
