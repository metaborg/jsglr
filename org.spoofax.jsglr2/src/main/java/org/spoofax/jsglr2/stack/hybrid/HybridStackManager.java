package org.spoofax.jsglr2.stack.hybrid;

import org.metaborg.parsetable.IState;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.stack.StackManager;

public class HybridStackManager<ParseForest extends AbstractParseForest>
    extends StackManager<ParseForest, HybridStackNode<ParseForest>> {

    @Override
    protected HybridStackNode<ParseForest> createStackNode(IState state, Position position, boolean isRoot) {
        return new HybridStackNode<>(state, position);
    }

}
