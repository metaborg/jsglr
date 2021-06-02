package org.spoofax.jsglr2.inputstack.incremental;

import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseNode;

public class LazyPreprocessingIncrementalInputStackTest extends AbstractPreprocessingIncrementalInputStackTest {

    @Override protected IIncrementalInputStack getStack(IncrementalParseNode root, String inputString) {
        return new LazyPreprocessingIncrementalInputStack(root, inputString);
    }

}
