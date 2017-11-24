package org.spoofax.jsglr2.stack.elkhound;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.states.IState;

public class HybridElkhoundStackManager<ParseForest extends AbstractParseForest>
    extends AbstractElkhoundStackManager<AbstractElkhoundStackNode<ParseForest>, ParseForest> {

    @Override
    protected HybridElkhoundStackNode<ParseForest> createStackNode(int stackNumber, IState state, Position position,
        int deterministicDepth) {
        return new HybridElkhoundStackNode<ParseForest>(stackNumber, state, position, deterministicDepth);
    }

}
