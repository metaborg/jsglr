package org.spoofax.jsglr2.stack.basic;

import org.metaborg.parsetable.IState;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Position;

public class HybridStackManager<ParseForest extends AbstractParseForest>
    extends AbstractBasicStackManager<ParseForest, HybridStackNode<ParseForest>> {

    @Override
    protected HybridStackNode<ParseForest> createStackNode(int stackNumber, IState state, Position position) {
        return new HybridStackNode<ParseForest>(stackNumber, state, position);
    }

}
