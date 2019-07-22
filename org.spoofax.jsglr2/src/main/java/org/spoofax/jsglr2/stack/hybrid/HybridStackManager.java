package org.spoofax.jsglr2.stack.hybrid;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.stack.StackManager;

public class HybridStackManager
//@formatter:off
   <ParseForest extends IParseForest,
    Parse       extends AbstractParse<ParseForest, HybridStackNode<ParseForest>>>
//@formatter:on
    extends StackManager<ParseForest, HybridStackNode<ParseForest>, Parse> {

    @Override protected HybridStackNode<ParseForest> createStackNode(IState state, Position position, boolean isRoot) {
        return new HybridStackNode<>(state, position);
    }

}
