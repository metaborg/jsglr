package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.parsetable.IState;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public class ForShifterElement<StackNode extends AbstractStackNode<ParseForest>, ParseForest> {

	public final StackNode stack;
	public final IState state;
	
	public ForShifterElement(StackNode stack, IState state) {
		this.stack = stack;
		this.state = state;
	}
	
}
