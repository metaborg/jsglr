package org.spoofax.jsglr2.inputstack.incremental;

import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseNode;

public class LinkedIncrementalInputStackTest extends AbstractIncrementalInputStackTest {

    @Override protected IIncrementalInputStack getStack(IncrementalParseNode root, String inputString) {
        return new LinkedIncrementalInputStack(root, inputString);
    }

}
