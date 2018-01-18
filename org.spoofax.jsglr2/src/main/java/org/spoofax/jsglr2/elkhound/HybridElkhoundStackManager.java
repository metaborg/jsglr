package org.spoofax.jsglr2.elkhound;

import org.metaborg.parsetable.IState;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.stack.StackLink;

public class HybridElkhoundStackManager<ParseForest extends AbstractParseForest>
    extends AbstractElkhoundStackManager<ParseForest, HybridElkhoundStackNode<ParseForest>> {

    @Override
    protected HybridElkhoundStackNode<ParseForest> createStackNode(int stackNumber, IState state, Position position,
        boolean isRoot) {
        return new HybridElkhoundStackNode<ParseForest>(stackNumber, state, position, isRoot);
    }

    @Override
    protected StackLink<ParseForest, HybridElkhoundStackNode<ParseForest>> addStackLink(
        Parse<ParseForest, HybridElkhoundStackNode<ParseForest>> parse, HybridElkhoundStackNode<ParseForest> from,
        HybridElkhoundStackNode<ParseForest> to, ParseForest parseNode) {
        return from.addLink(parse.stackLinkCount++, to, parseNode, parse);
    }

    @Override
    protected StackLink<ParseForest, HybridElkhoundStackNode<ParseForest>>
        getOnlyLink(HybridElkhoundStackNode<ParseForest> stack) {
        return stack.getOnlyLink();
    }

    @Override
    protected Iterable<StackLink<ParseForest, HybridElkhoundStackNode<ParseForest>>>
        stackLinksOut(HybridElkhoundStackNode<ParseForest> stack) {
        return stack.getLinks();
    }

}
