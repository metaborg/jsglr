package org.spoofax.jsglr2.elkhound;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.stack.StackLink;

public class BasicElkhoundStackManager
//@formatter:off
   <ParseForest extends IParseForest,
    ParseState  extends AbstractParseState<ParseForest, BasicElkhoundStackNode<ParseForest>>>
//@formatter:on
    extends ElkhoundStackManager<ParseForest, BasicElkhoundStackNode<ParseForest>, ParseState> {

    @Override protected BasicElkhoundStackNode<ParseForest> createStackNode(IState state, Position position,
        boolean isRoot) {
        return new BasicElkhoundStackNode<>(state, position, isRoot);
    }

    @Override protected StackLink<ParseForest, BasicElkhoundStackNode<ParseForest>> addStackLink(
        Parse<ParseForest, BasicElkhoundStackNode<ParseForest>, ParseState> parse,
        BasicElkhoundStackNode<ParseForest> from, BasicElkhoundStackNode<ParseForest> to, ParseForest parseNode) {
        return from.addLink(to, parseNode, parse);
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
