package org.spoofax.jsglr2.incremental;

import java.util.List;

import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.IParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;

public interface IncrementalParseFactory
//@formatter:off
   <StackNode  extends IStackNode,
    ParseState extends IParseState<IncrementalParseForest, StackNode>,
    Parse      extends AbstractParse<IncrementalParseForest, StackNode, ParseState> & IIncrementalParse>
//@formatter:on
{

    Parse get(List<EditorUpdate> editorUpdates, IncrementalParseForest previousVersion, String inputString,
        String filename, ParserObserving<IncrementalParseForest, StackNode, ParseState> observing);

}
