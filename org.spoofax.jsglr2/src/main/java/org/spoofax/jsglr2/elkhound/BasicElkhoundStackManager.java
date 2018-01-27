package org.spoofax.jsglr2.elkhound;

import org.metaborg.parsetable.IState;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.stack.StackLink;

public class BasicElkhoundStackManager<ParseForest extends AbstractParseForest>
    extends AbstractElkhoundStackManager<ParseForest, BasicElkhoundStackNode<ParseForest>> {

    @Override
    protected BasicElkhoundStackNode<ParseForest> createStackNode(int stackNumber, IState state, Position position,
        boolean isRoot) {
        return new BasicElkhoundStackNode<ParseForest>(stackNumber, state, position, isRoot);
    }

    @Override
    protected StackLink<ParseForest, BasicElkhoundStackNode<ParseForest>> addStackLink(
        Parse<ParseForest, BasicElkhoundStackNode<ParseForest>> parse, BasicElkhoundStackNode<ParseForest> from,
        BasicElkhoundStackNode<ParseForest> to, ParseForest parseNode) {
        return from.addLink(parse.stackLinkCount++, to, parseNode, parse);
    }

    @Override
    protected StackLink<ParseForest, BasicElkhoundStackNode<ParseForest>>
        getOnlyLink(BasicElkhoundStackNode<ParseForest> stack) {
        return stack.getOnlyLink();
    }

    @Override
    protected Iterable<StackLink<ParseForest, BasicElkhoundStackNode<ParseForest>>>
        stackLinksOut(BasicElkhoundStackNode<ParseForest> stack) {
        return stack.getLinks();
    }

}
