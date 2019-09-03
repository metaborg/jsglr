package org.spoofax.jsglr2.stack.hybrid;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.IParseState;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.stack.StackManager;

public class HybridStackManager
//@formatter:off
   <ParseForest extends IParseForest,
    ParseState  extends IParseState<ParseForest, HybridStackNode<ParseForest>>,
    Parse       extends AbstractParse<ParseForest, HybridStackNode<ParseForest>, ParseState>>
//@formatter:on
    extends StackManager<ParseForest, HybridStackNode<ParseForest>, ParseState, Parse> {

    @Override protected HybridStackNode<ParseForest> createStackNode(IState state, Position position, boolean isRoot) {
        return new HybridStackNode<>(state, position);
    }

}
