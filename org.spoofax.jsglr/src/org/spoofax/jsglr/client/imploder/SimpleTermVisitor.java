package org.spoofax.jsglr.client.imploder;

import java.util.Iterator;

import org.spoofax.interpreter.terms.ISimpleTerm;


/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public abstract class SimpleTermVisitor implements ISimpleTermVisitor {
	
	public final boolean visit(ISimpleTerm tree) {
		Iterator<ISimpleTerm> iterator = tryGetListIterator(tree); 
		for (int i = 0, max = tree.getSubtermCount(); i < max; i++) {
			ISimpleTerm child = iterator == null ? tree.getSubterm(i) : iterator.next();
			preVisit(child);
			boolean isDone = visit(child);
			postVisit(child);
			if (isDone || isDone()) return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	protected static Iterator<ISimpleTerm> tryGetListIterator(ISimpleTerm tree) {
		if (tree instanceof Iterable) {
			return ((Iterable<ISimpleTerm>) tree).iterator();
		} else {
			return null;
		}
	}
	
	public void preVisit(ISimpleTerm node) {
		// No default implementation
	}
	
	public void postVisit(ISimpleTerm node) {
		// No default implementation
	}
	
	public boolean isDone() {
		return false;
	}
}

//Local interface avoids abstract method and subsequent @Override annotation requirement

interface ISimpleTermVisitor {
	void preVisit(ISimpleTerm node);

	void postVisit(ISimpleTerm node);
}