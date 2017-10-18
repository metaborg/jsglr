package org.spoofax.jsglr2.stack.elkhound;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parsetable.IState;

public class HybridElkhoundStackManager<ParseForest extends AbstractParseForest> extends AbstractElkhoundStackManager<AbstractElkhoundStackNode<ParseForest>, ParseForest> {

	protected HybridElkhoundStackNode<ParseForest> createStackNode(int stackNumber, IState state, int deterministicDepth) {
		return new HybridElkhoundStackNode<ParseForest>(stackNumber, state, deterministicDepth);
	}
	
}
