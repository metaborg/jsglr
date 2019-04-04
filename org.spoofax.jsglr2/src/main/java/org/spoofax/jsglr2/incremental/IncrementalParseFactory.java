package org.spoofax.jsglr2.incremental;

import java.util.List;

import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;

public interface IncrementalParseFactory<StackNode extends IStackNode, Parse extends IncrementalParse<StackNode>> {

    Parse get(List<EditorUpdate> editorUpdates, IncrementalParseForest previousVersion, String filename,
        ParserObserving<IncrementalParseForest, StackNode> observing);

}
