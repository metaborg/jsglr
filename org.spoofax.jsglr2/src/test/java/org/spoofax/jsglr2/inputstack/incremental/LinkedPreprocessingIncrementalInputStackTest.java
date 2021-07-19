package org.spoofax.jsglr2.inputstack.incremental;

import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseNode;

public class LinkedPreprocessingIncrementalInputStackTest extends AbstractPreprocessingIncrementalInputStackTest {

    @Override protected IIncrementalInputStack getStack(IncrementalParseNode root, String inputString) {
        return new LinkedPreprocessingIncrementalInputStack(root, inputString);
    }

}
