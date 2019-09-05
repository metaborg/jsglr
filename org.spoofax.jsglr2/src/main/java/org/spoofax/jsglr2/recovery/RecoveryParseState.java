package org.spoofax.jsglr2.recovery;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.IParseState;
import org.spoofax.jsglr2.parser.ParseStateFactory;
import org.spoofax.jsglr2.stack.IStackNode;

public class RecoveryParseState
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode>
//@formatter:on
    implements IParseState<ParseForest, StackNode>, IRecoveryState<ParseForest, StackNode> {

    public static
//@formatter:off
   <ParseForest_ extends IParseForest,
    StackNode_   extends IStackNode,
    ParseState_  extends IParseState<ParseForest_, StackNode_> & IRecoveryState<ParseForest_, StackNode_>>
//@formatter:on
   ParseStateFactory<ParseForest_, StackNode_, ParseState_> factory() {
        return () -> (ParseState_) new RecoveryParseState<>();
    }

}
