package org.spoofax.jsglr2.stack.basic;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.states.IState;

public class BasicStackManager<ParseForest extends AbstractParseForest>
    extends AbstractBasicStackManager<ParseForest, BasicStackNode<ParseForest>> {

    @Override
    protected BasicStackNode<ParseForest> createStackNode(int stackNumber, IState state, Position position) {
        return new BasicStackNode<ParseForest>(stackNumber, state, position);
    }

}
