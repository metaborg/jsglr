package org.spoofax.jsglr2.stack.basic;

import org.metaborg.parsetable.IState;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.stack.StackManager;

public class BasicStackManager<ParseForest extends AbstractParseForest>
    extends StackManager<ParseForest, BasicStackNode<ParseForest>> {

    @Override protected BasicStackNode<ParseForest> createStackNode(IState state, Position position, boolean isRoot) {
        return new BasicStackNode<>(state, position);
    }

}
