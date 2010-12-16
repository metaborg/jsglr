package org.spoofax.jsglr.client.imploder;

import org.spoofax.jsglr.client.AbstractParseNode;
import org.spoofax.jsglr.client.Amb;
import org.spoofax.jsglr.client.ITreeBuilder;
import org.spoofax.jsglr.client.ParseNode;
import org.spoofax.jsglr.client.ParseProductionNode;

/**
 * An abstract top-down tree builder implementation.
 * For most uses, the bottom-up one should be more suitable.
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public abstract class TopdownTreeBuilder implements ITreeBuilder {

	public void initialize(int productionCount, int labelCount) {
		
	}

	/**
	 * @deprecated
	 *   For a best balance of performance and stack consumption, directly call
	 *   {@link ParseNode#buildTreeBottomup} instead.
	 */
	public Object buildTree(AbstractParseNode node) {
		if (node instanceof ParseNode) {
			return buildTreeNode((ParseNode) node);
		} else if (node instanceof ParseProductionNode) {
			return buildTreeProduction((ParseProductionNode) node);
		} else {
			assert node instanceof Amb;
			return buildTreeAmb((Amb) node);
		}
	}

	public Object buildTreeTop(Object subtree, int ambiguityCount) {
		return subtree;
	}

	public abstract Object buildTreeNode(ParseNode node);

	public abstract Object buildTreeProduction(ParseProductionNode node);

	public abstract Object buildTreeAmb(Amb node);
}
