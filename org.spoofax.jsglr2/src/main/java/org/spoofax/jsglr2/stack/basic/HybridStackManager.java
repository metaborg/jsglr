package org.spoofax.jsglr2.stack.basic;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parsetable.IState;

public class HybridStackManager<ParseForest extends AbstractParseForest> extends AbstractBasicStackManager<HybridStackNode<ParseForest>, ParseForest> {
    
	protected HybridStackNode<ParseForest> createStackNode(int stackNumber, IState state, int offset) {
		return new HybridStackNode<ParseForest>(stackNumber, state, offset);
	}
    
}
