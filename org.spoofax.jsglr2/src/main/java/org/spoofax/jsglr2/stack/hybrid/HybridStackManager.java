package org.spoofax.jsglr2.stack.hybrid;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.StackManager;

public class HybridStackManager
//@formatter:off
   <ParseForest extends IParseForest,
    ParseState  extends AbstractParseState<ParseForest, HybridStackNode<ParseForest>>>
//@formatter:on
    extends StackManager<ParseForest, HybridStackNode<ParseForest>, ParseState> {

    @Override protected HybridStackNode<ParseForest> createStackNode(
        ParserObserving<ParseForest, HybridStackNode<ParseForest>, ParseState> observing, IState state,
        Position position, boolean isRoot) {
        return new HybridStackNode<>(state, position);
    }

}
