package org.spoofax.jsglr.client;

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
	public final Object buildTree(AbstractParseNode node) {
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

	protected abstract Object buildTreeNode(ParseNode node);

	protected abstract Object buildTreeProduction(ParseProductionNode node);

	protected abstract Object buildTreeAmb(Amb node);
}
