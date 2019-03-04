package org.spoofax.jsglr2.stack.basic;

import org.metaborg.parsetable.IState;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.stack.StackManager;

public class BasicStackManager<ParseForest extends IParseForest>
    extends StackManager<ParseForest, BasicStackNode<ParseForest>> {

    @Override protected BasicStackNode<ParseForest> createStackNode(IState state, boolean isRoot) {
        return new BasicStackNode<>(state);
    }

}
