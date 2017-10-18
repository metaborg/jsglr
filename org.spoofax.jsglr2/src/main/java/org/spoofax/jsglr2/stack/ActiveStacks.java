package org.spoofax.jsglr2.stack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.spoofax.jsglr2.parsetable.IState;

public class ActiveStacks<ParseForest, StackNode extends AbstractStackNode<ParseForest>> implements IActiveStacks<StackNode> {

	private List<StackNode> activeStacks;
	
	public ActiveStacks() {
		this.activeStacks = new ArrayList<StackNode>();
	}
	
	public Iterator<StackNode> iterator() {
		return activeStacks.iterator();
	}

	public void add(StackNode stack) {
		activeStacks.add(stack);
	}

	public StackNode get(int i) {
		return activeStacks.get(i);
	}

	public int size() {
		return activeStacks.size();
	}

	public StackNode findWithState(IState state) {
		for (StackNode stack : activeStacks)
			if (stack.state.stateNumber() == state.stateNumber())
				return stack;
		
		return null;
	}

	public void addAllTo(Collection<StackNode> other) {
		for (StackNode stack : activeStacks)
			other.add(stack);
	}

	public void clear() {
		activeStacks.clear();
	}

}
