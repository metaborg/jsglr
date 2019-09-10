package org.spoofax.jsglr2.stack.basic;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.stack.StackManager;

public class BasicStackManager
//@formatter:off
   <ParseForest extends IParseForest,
    ParseState  extends AbstractParseState<ParseForest, BasicStackNode<ParseForest>>>
//@formatter:on
    extends StackManager<ParseForest, BasicStackNode<ParseForest>, ParseState> {

    @Override protected BasicStackNode<ParseForest> createStackNode(IState state, Position position, boolean isRoot) {
        return new BasicStackNode<>(state, position);
    }

}
