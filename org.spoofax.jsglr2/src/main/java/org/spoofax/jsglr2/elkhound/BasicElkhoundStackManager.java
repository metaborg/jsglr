package org.spoofax.jsglr2.elkhound;

import org.metaborg.parsetable.IState;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.stack.StackLink;

public class BasicElkhoundStackManager<ParseForest extends IParseForest>
    extends ElkhoundStackManager<ParseForest, BasicElkhoundStackNode<ParseForest>> {

    @Override protected BasicElkhoundStackNode<ParseForest> createStackNode(IState state, boolean isRoot) {
        return new BasicElkhoundStackNode<>(state, isRoot);
    }

    @Override protected StackLink<ParseForest, BasicElkhoundStackNode<ParseForest>> addStackLink(
        AbstractParse<ParseForest, BasicElkhoundStackNode<ParseForest>> parse, BasicElkhoundStackNode<ParseForest> from,
        BasicElkhoundStackNode<ParseForest> to, ParseForest parseNode) {
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
