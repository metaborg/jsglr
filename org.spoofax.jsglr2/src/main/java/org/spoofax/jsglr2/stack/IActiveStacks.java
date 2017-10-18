package org.spoofax.jsglr2.stack;

import java.util.Collection;

import org.spoofax.jsglr2.parsetable.IState;

public interface IActiveStacks<StackNode> extends Iterable<StackNode> {

	public void add(StackNode stack);
	
	public StackNode get(int i);
	
	public int size();
	
	public default boolean isEmpty() {
		return size() == 0;
	}
	
	public StackNode findWithState(IState state);
	
	public void addAllTo(Collection<StackNode> other);
	
	public void clear();
	
}
