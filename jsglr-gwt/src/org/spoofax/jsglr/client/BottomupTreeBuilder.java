package org.spoofax.jsglr.client;


/**
 * An abstract bottom-up tree builder implementation.
 *
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public abstract class BottomupTreeBuilder implements ITreeBuilder {

	public void initialize(int productionCount, int labelCount) {
		
	}
	
	public Object buildTree(AbstractParseNode node) {
		return node.toTreeBottomup(this);
	}
	
	public void visitLabel(int labelNumber) {
		
	}
	
	public void endVisitLabel(int labelNumber) {
		
	}

	public Object buildTreeTop(Object subtree, int ambiguityCount) {
		return subtree;
	}

	public abstract Object buildNode(int labelNumber, Object[] subtrees);
	public abstract Object buildProduction(int productionNumber);
	public abstract Object buildAmb(Object[] alternatives);

}
