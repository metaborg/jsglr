package org.spoofax.jsglr2.stack.hybrid;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.AbstractStackManager;
import org.spoofax.jsglr2.stack.StackManager;
import org.spoofax.jsglr2.stack.StackManagerFactory;

public class HybridStackManager
//@formatter:off
   <ParseForest extends IParseForest,
    ParseState  extends AbstractParseState<ParseForest, HybridStackNode<ParseForest>>>
//@formatter:on
    extends StackManager<ParseForest, HybridStackNode<ParseForest>, ParseState> {

    public HybridStackManager(ParserObserving<ParseForest, HybridStackNode<ParseForest>, ParseState> observing) {
        super(observing);
    }

    public static
//@formatter:off
   <ParseForest_  extends IParseForest,
    ParseState_   extends AbstractParseState<ParseForest_, HybridStackNode<ParseForest_>>,
    StackManager_ extends AbstractStackManager<ParseForest_, HybridStackNode<ParseForest_>, ParseState_>>
//@formatter:on
    StackManagerFactory<ParseForest_, HybridStackNode<ParseForest_>, ParseState_, StackManager_> factory() {
        return observing -> (StackManager_) new HybridStackManager(observing);
    }

    @Override protected HybridStackNode<ParseForest> createStackNode(IState state, boolean isRoot) {
        return new HybridStackNode<>(state);
    }

}
