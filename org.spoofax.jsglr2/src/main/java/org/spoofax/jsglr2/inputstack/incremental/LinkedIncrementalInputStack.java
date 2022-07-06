package org.spoofax.jsglr2.inputstack.incremental;

import java.util.List;

import org.spoofax.jsglr2.incremental.EditorUpdate;
import org.spoofax.jsglr2.incremental.diff.IStringDiff;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.parser.observing.ParserObserving;

public class LinkedIncrementalInputStack extends AbstractIncrementalInputStack {

    LinkedIncrementalInputStack(AbstractIncrementalInputStack original) {
        super(original);
    }

    public LinkedIncrementalInputStack(String input, String previousInput, IncrementalParseForest previousResult,
        List<EditorUpdate> editorUpdates, ParserObserving<?, ?, ?, ?, ?> observing) {
        super(input, previousInput, previousResult, editorUpdates, observing);
    }

    public static IncrementalInputStackFactory<IIncrementalInputStack> factory(IStringDiff diff) {
        return factoryBuilder(diff, LinkedIncrementalInputStack::new);
    }

    @Override protected LinkedStack<IncrementalParseForest> createStack() {
        return new LinkedStack<>();
    }

    @Override public LinkedIncrementalInputStack clone() {
        return new LinkedIncrementalInputStack(this);
    }
}
