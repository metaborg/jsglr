package org.spoofax.jsglr2.recovery;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.IParseState;
import org.spoofax.jsglr2.stack.IStackNode;

public interface IRecoveryState
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode>
//@formatter:on
    extends IParseState<ParseForest, StackNode> {
}
