package org.spoofax.jsglr2.elkhound;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.AbstractStackManager;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.StackManagerFactory;

public class HybridElkhoundStackManager
//@formatter:off
   <ParseForest extends IParseForest,
    ParseState  extends AbstractParseState<ParseForest, HybridElkhoundStackNode<ParseForest>>>
//@formatter:on
    extends ElkhoundStackManager<ParseForest, HybridElkhoundStackNode<ParseForest>, ParseState> {

    public HybridElkhoundStackManager(
        ParserObserving<ParseForest, HybridElkhoundStackNode<ParseForest>, ParseState> observing) {
        super(observing);
    }

    public static
//@formatter:off
   <ParseForest_  extends IParseForest,
    ParseState_   extends AbstractParseState<ParseForest_, HybridElkhoundStackNode<ParseForest_>>,
    StackManager_ extends AbstractStackManager<ParseForest_, HybridElkhoundStackNode<ParseForest_>, ParseState_>>
//@formatter:on
    StackManagerFactory<ParseForest_, HybridElkhoundStackNode<ParseForest_>, ParseState_, StackManager_> factory() {
        return observing -> (StackManager_) new HybridElkhoundStackManager(observing);
    }

    @Override protected HybridElkhoundStackNode<ParseForest> createStackNode(IState state, boolean isRoot) {
        return new HybridElkhoundStackNode<>(state, isRoot);
    }

    @Override protected StackLink<ParseForest, HybridElkhoundStackNode<ParseForest>> addStackLink(ParseState parseState,
        HybridElkhoundStackNode<ParseForest> from, HybridElkhoundStackNode<ParseForest> to, ParseForest parseNode) {
        return from.addLink(observing, to, parseNode, parseState.activeStacks);
    }

    @Override protected StackLink<ParseForest, HybridElkhoundStackNode<ParseForest>>
        getOnlyLink(HybridElkhoundStackNode<ParseForest> stack) {
        return stack.getOnlyLink();
    }

    @Override protected Iterable<StackLink<ParseForest, HybridElkhoundStackNode<ParseForest>>>
        stackLinksOut(HybridElkhoundStackNode<ParseForest> stack) {
        return stack.getLinks();
    }

}
