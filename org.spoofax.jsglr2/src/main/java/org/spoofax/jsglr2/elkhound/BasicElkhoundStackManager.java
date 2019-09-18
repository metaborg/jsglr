package org.spoofax.jsglr2.elkhound;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.AbstractStackManager;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.StackManagerFactory;

public class BasicElkhoundStackManager
//@formatter:off
   <ParseForest extends IParseForest,
    ParseState  extends AbstractParseState<ParseForest, BasicElkhoundStackNode<ParseForest>>>
//@formatter:on
    extends ElkhoundStackManager<ParseForest, BasicElkhoundStackNode<ParseForest>, ParseState> {

    public BasicElkhoundStackManager(
        ParserObserving<ParseForest, BasicElkhoundStackNode<ParseForest>, ParseState> observing) {
        super(observing);
    }

    public static
//@formatter:off
   <ParseForest_  extends IParseForest,
    ParseState_   extends AbstractParseState<ParseForest_, BasicElkhoundStackNode<ParseForest_>>,
    StackManager_ extends AbstractStackManager<ParseForest_, BasicElkhoundStackNode<ParseForest_>, ParseState_>>
//@formatter:on
    StackManagerFactory<ParseForest_, BasicElkhoundStackNode<ParseForest_>, ParseState_, StackManager_> factory() {
        return observing -> (StackManager_) new BasicElkhoundStackManager(observing);
    }

    @Override protected BasicElkhoundStackNode<ParseForest> createStackNode(IState state, boolean isRoot) {
        return new BasicElkhoundStackNode<>(state, isRoot);
    }

    @Override protected StackLink<ParseForest, BasicElkhoundStackNode<ParseForest>> addStackLink(ParseState parseState,
        BasicElkhoundStackNode<ParseForest> from, BasicElkhoundStackNode<ParseForest> to, ParseForest parseNode) {
        return from.addLink(observing, to, parseNode, parseState.activeStacks);
    }

    @Override protected StackLink<ParseForest, BasicElkhoundStackNode<ParseForest>>
        getOnlyLink(BasicElkhoundStackNode<ParseForest> stack) {
        return stack.getOnlyLink();
    }

    @Override protected Iterable<StackLink<ParseForest, BasicElkhoundStackNode<ParseForest>>>
        stackLinksOut(BasicElkhoundStackNode<ParseForest> stack) {
        return stack.getLinks();
    }

}
