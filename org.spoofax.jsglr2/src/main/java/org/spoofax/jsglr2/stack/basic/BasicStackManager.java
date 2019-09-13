package org.spoofax.jsglr2.stack.basic;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.StackManager;

public class BasicStackManager
//@formatter:off
   <ParseForest extends IParseForest,
    ParseState  extends AbstractParseState<ParseForest, BasicStackNode<ParseForest>>>
//@formatter:on
    extends StackManager<ParseForest, BasicStackNode<ParseForest>, ParseState> {

    @Override protected BasicStackNode<ParseForest> createStackNode(
        ParserObserving<ParseForest, BasicStackNode<ParseForest>, ParseState> observing, IState state, boolean isRoot) {
        return new BasicStackNode<>(state);
    }

}
