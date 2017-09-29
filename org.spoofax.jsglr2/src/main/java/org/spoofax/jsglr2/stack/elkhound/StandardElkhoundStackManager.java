package org.spoofax.jsglr2.stack.elkhound;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parsetable.IState;

public class StandardElkhoundStackManager<ParseForest extends AbstractParseForest> extends ElkhoundStackManager<StandardElkhoundStackNode<ParseForest>, ParseForest> {

	protected StandardElkhoundStackNode<ParseForest> createStackNode(int stackNumber, IState state, int deterministicDepth) {
		return new StandardElkhoundStackNode<ParseForest>(stackNumber, state, deterministicDepth);
	}
	
}
