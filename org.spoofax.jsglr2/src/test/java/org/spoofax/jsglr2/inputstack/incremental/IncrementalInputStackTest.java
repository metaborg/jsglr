package org.spoofax.jsglr2.inputstack.incremental;

import java.util.Arrays;

import org.spoofax.jsglr2.incremental.EditorUpdate;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseNode;

public class IncrementalInputStackTest extends AbstractIncrementalInputStackTest {

    @Override protected IIncrementalInputStack getStack(String inputString, String previousInput,
        IncrementalParseNode previousResult, EditorUpdate... editorUpdates) {
        return new IncrementalInputStack(inputString, previousInput, previousResult, Arrays.asList(editorUpdates));
    }

}
