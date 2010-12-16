package org.spoofax.jsglr.client.imploder;

import java.util.Iterator;


/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public abstract class AstNodeVisitor implements IAstNodeVisitor {
	
	public final void visit(IAstNode tree) {
		Iterator<IAstNode> iterator = tryGetListIterator(tree); 
		for (int i = 0, max = tree.getChildCount(); i < max; i++) {
			IAstNode child = iterator == null ? tree.getChildAt(i) : iterator.next();
			if (preVisit(child)) {
				visit(child);
				postVisit(child);
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected static Iterator<IAstNode> tryGetListIterator(IAstNode tree) {
		if (tree instanceof Iterable) {
			return ((Iterable<IAstNode>) tree).iterator();
		} else {
			return null;
		}
	}
}

//Local interface avoids abstract method and subsequent @Override annotation requirement

interface IAstNodeVisitor {
	/**
	 * @return true if this node should be visited and post-visited.
	 */
	boolean preVisit(IAstNode node);

	void postVisit(IAstNode node);
}