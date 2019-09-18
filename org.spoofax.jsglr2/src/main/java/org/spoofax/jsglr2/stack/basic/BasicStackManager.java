package org.spoofax.jsglr2.stack.basic;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.AbstractStackManager;
import org.spoofax.jsglr2.stack.StackManager;
import org.spoofax.jsglr2.stack.StackManagerFactory;

public class BasicStackManager
//@formatter:off
   <ParseForest extends IParseForest,
    ParseState  extends AbstractParseState<ParseForest, BasicStackNode<ParseForest>>>
//@formatter:on
    extends StackManager<ParseForest, BasicStackNode<ParseForest>, ParseState> {

    public BasicStackManager(ParserObserving<ParseForest, BasicStackNode<ParseForest>, ParseState> observing) {
        super(observing);
    }

    public static
//@formatter:off
   <ParseForest_  extends IParseForest,
    ParseState_   extends AbstractParseState<ParseForest_, BasicStackNode<ParseForest_>>,
    StackManager_ extends AbstractStackManager<ParseForest_, BasicStackNode<ParseForest_>, ParseState_>>
//@formatter:on
    StackManagerFactory<ParseForest_, BasicStackNode<ParseForest_>, ParseState_, StackManager_> factory() {
        return observing -> (StackManager_) new BasicStackManager(observing);
    }

    @Override protected BasicStackNode<ParseForest> createStackNode(IState state, boolean isRoot) {
        return new BasicStackNode<>(state);
    }

}
