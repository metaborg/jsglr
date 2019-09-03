package org.spoofax.jsglr2.elkhound;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.IParseState;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.stack.StackLink;

public class HybridElkhoundStackManager
//@formatter:off
   <ParseForest extends IParseForest,
    ParseState  extends IParseState<ParseForest, HybridElkhoundStackNode<ParseForest>>,
    Parse       extends AbstractParse<ParseForest, HybridElkhoundStackNode<ParseForest>, ParseState>>
//@formatter:on
    extends ElkhoundStackManager<ParseForest, HybridElkhoundStackNode<ParseForest>, ParseState, Parse> {

    @Override protected HybridElkhoundStackNode<ParseForest> createStackNode(IState state, Position position,
        boolean isRoot) {
        return new HybridElkhoundStackNode<>(state, position, isRoot);
    }

    @Override protected StackLink<ParseForest, HybridElkhoundStackNode<ParseForest>> addStackLink(Parse parse,
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
