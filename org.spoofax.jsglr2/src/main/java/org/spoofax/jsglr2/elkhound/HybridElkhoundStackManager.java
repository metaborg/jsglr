package org.spoofax.jsglr2.elkhound;

import org.metaborg.parsetable.IState;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.stack.StackLink;

public class HybridElkhoundStackManager<ParseForest extends IParseForest>
    extends ElkhoundStackManager<ParseForest, HybridElkhoundStackNode<ParseForest>> {

    @Override protected HybridElkhoundStackNode<ParseForest> createStackNode(IState state, boolean isRoot) {
        return new HybridElkhoundStackNode<>(state, isRoot);
    }

    @Override protected StackLink<ParseForest, HybridElkhoundStackNode<ParseForest>> addStackLink(
        AbstractParse<ParseForest, HybridElkhoundStackNode<ParseForest>> parse,
        HybridElkhoundStackNode<ParseForest> from, HybridElkhoundStackNode<ParseForest> to, ParseForest parseNode) {
        return from.addLink(to, parseNode, parse);
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
