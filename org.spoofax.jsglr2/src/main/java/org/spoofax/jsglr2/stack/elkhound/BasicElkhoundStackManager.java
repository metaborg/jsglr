package org.spoofax.jsglr2.stack.elkhound;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.states.IState;

public class BasicElkhoundStackManager<ParseForest extends AbstractParseForest>
    extends AbstractElkhoundStackManager<ParseForest, AbstractElkhoundStackNode<ParseForest>> {

    @Override
    protected BasicElkhoundStackNode<ParseForest> createStackNode(int stackNumber, IState state, Position position,
        int deterministicDepth) {
        return new BasicElkhoundStackNode<ParseForest>(stackNumber, state, position, deterministicDepth);
    }

}
