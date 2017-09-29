package org.spoofax.jsglr2.stack.elkhound;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parsetable.IState;

public class BasicElkhoundStackManager<ParseForest extends AbstractParseForest> extends ElkhoundStackManager<BasicElkhoundStackNode<ParseForest>, ParseForest> {

	protected BasicElkhoundStackNode<ParseForest> createStackNode(int stackNumber, IState state, int deterministicDepth) {
		return new BasicElkhoundStackNode<ParseForest>(stackNumber, state, deterministicDepth);
	}
	
}
