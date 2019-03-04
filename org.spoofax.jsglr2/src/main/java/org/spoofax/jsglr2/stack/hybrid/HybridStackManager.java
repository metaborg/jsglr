package org.spoofax.jsglr2.stack.hybrid;

import org.metaborg.parsetable.IState;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.stack.StackManager;

public class HybridStackManager<ParseForest extends IParseForest>
    extends StackManager<ParseForest, HybridStackNode<ParseForest>> {

    @Override protected HybridStackNode<ParseForest> createStackNode(IState state, boolean isRoot) {
        return new HybridStackNode<>(state);
    }

}
