package org.spoofax.jsglr2.elkhound;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.stack.StackLink;

public class BasicElkhoundStackManager
//@formatter:off
   <ParseForest extends IParseForest,
    Parse       extends AbstractParse<ParseForest, BasicElkhoundStackNode<ParseForest>>>
//@formatter:on
    extends ElkhoundStackManager<ParseForest, BasicElkhoundStackNode<ParseForest>, Parse> {

    @Override protected BasicElkhoundStackNode<ParseForest> createStackNode(IState state, Position position,
        boolean isRoot) {
        return new BasicElkhoundStackNode<>(state, position, isRoot);
    }

    @Override protected StackLink<ParseForest, BasicElkhoundStackNode<ParseForest>> addStackLink(Parse parse,
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
