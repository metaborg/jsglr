package org.spoofax.jsglr2.inputstack.incremental;

import java.util.List;

import org.spoofax.jsglr2.incremental.EditorUpdate;
import org.spoofax.jsglr2.incremental.diff.IStringDiff;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;

public class IncrementalInputStack extends AbstractIncrementalInputStack {

    IncrementalInputStack(IncrementalInputStack original) {
        super(original);
    }

    public IncrementalInputStack(String input, IncrementalParseForest previousResult,
        List<EditorUpdate> editorUpdates) {
        super(input, previousResult, editorUpdates);
    }

    public static IncrementalInputStackFactory<IIncrementalInputStack> factory(IStringDiff diff) {
        return factoryBuilder(diff, IncrementalInputStack::new);
    }

    @Override protected CloneableStack<IncrementalParseForest> createStack() {
        return new CloneableStack<>();
    }

    @Override public IncrementalInputStack clone() {
        return new IncrementalInputStack(this);
    }
}

