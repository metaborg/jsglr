package org.spoofax.jsglr2.stack.basic;

import org.metaborg.parsetable.IState;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Position;

public class BasicStackManager<ParseForest extends AbstractParseForest>
    extends AbstractBasicStackManager<ParseForest, BasicStackNode<ParseForest>> {

    @Override
    protected BasicStackNode<ParseForest> createStackNode(int stackNumber, IState state, Position position) {
        return new BasicStackNode<ParseForest>(stackNumber, state, position);
    }

}
