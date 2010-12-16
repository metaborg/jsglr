package org.spoofax.jsglr.client.incremental;

import static org.spoofax.jsglr.client.incremental.IncrementalSGLR.isRangeOverlap;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.spoofax.jsglr.client.SGLR;
import org.spoofax.jsglr.client.imploder.IAstNode;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr.client.imploder.ITokenizer;
import org.spoofax.jsglr.client.imploder.ITreeFactory;
import org.spoofax.jsglr.client.imploder.Tokenizer;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class IncrementalTreeBuilder<TNode extends IAstNode> {
	
	/**
	 * Allows for one character of extra slack based on the assumption
	 * that grammars will not have two consecutive IncrementalSort*
	 * lists.
	 */
	private static final int NEXT_CHAR = 1;
	
	private final SGLR parser;

	private final String input;

	private final String filename;

	private final ITreeFactory<TNode> factory;
	
	private final Set<String> incrementalSorts;

	private final int damageStart;

	private final int damageEnd;

	/**
	 * @param incrementalSorts
	 *            Sorts that can be incrementally parsed (e.g., MethodDec, ImportDec).
	 *            *Must* be sorts that only occur in lists (such as MethodDec*).
	 */
	public IncrementalTreeBuilder(IncrementalSGLR<TNode> parser, String input, String filename,
			int damageStart, int damageEnd) {
		this.parser = parser.parser;
		this.input = input;
		this.filename = filename;
		this.factory = parser.factory;
		this.incrementalSorts = parser.incrementalSorts;
		this.damageStart = damageStart;
		this.damageEnd = damageEnd;
	}
	
	/**
	 * Gets all non-list tree nodes that are in the damaged region
	 * according to {@link #isDamageTreeNode}.
	 */
	public List<IAstNode> getDamageTreeNodes(IAstNode tree) {
		return getDamageTreeNodes(tree, new ArrayList<IAstNode>());
	}

	private List<IAstNode> getDamageTreeNodes(IAstNode tree, List<IAstNode> results) {
		if (tree.isList()) { // ignored for getDamageTreeNodes
			getDamageTreeNodesRecurse(tree, results);
		} else {
			if (isDamageTreeNode(tree)) {
				results.add(tree);
			} else {
				getDamageTreeNodesRecurse(tree, results);
			}
		}
		return results;
	}

	private void getDamageTreeNodesRecurse(IAstNode tree, List<IAstNode> results) {
		
		if (tree instanceof Iterable) { // likely a linked list
			for (Object o : (Iterable<?>) tree) {
				getDamageTreeNodes((IAstNode) o, results);
			}
		} else {
			for (int i = 0, count = tree.getChildCount(); i < count; i++) {
				getDamageTreeNodes(tree.getChildAt(i), results);
			}
		}
	}

	/**
	 * Determines if the damaged region affects a particular tree node,
	 * looking only at those tokens that actually belong to the node
	 * and not to its children. Also returns true for nodes with a sort 
	 * in {@link #incrementalSorts} regardless of whether they own the tokens
	 * or not.
	 */
	private boolean isDamageTreeNode(IAstNode tree) {
		IToken left = tree.getLeftToken();
		IToken right = tree.getRightToken();
		if (left != null && right != null) {
			int startOffset = left.getStartOffset();
			int endOffset = right.getEndOffset();
			
			if (!isDamagedNonEmptyRange(startOffset, endOffset))
				return false;
			if (incrementalSorts.contains(tree.getSort()))
				return true;
			for (int i = 0, count = tree.getChildCount(); i < count; i++) {
				IAstNode child = tree.getChildAt(i);
				IToken childLeft = child.getLeftToken();
				IToken childRight = child.getRightToken();
				if (childLeft != null && childRight != null) {
					if (isDamagedNonEmptyRange(startOffset, childLeft.getStartOffset() - 1)) {
						return true;
					}
					startOffset = childRight.getEndOffset() + 1;
				}
			}
			return isDamagedNonEmptyRange(startOffset, getLastNonLayoutOffset(tree));
		} else {
			return false;
		}
	}
	
	private static int getLastNonLayoutOffset(IAstNode tree) {
		IToken token = tree.getRightToken();
		ITokenizer tokens = token.getTokenizer();
		while (token.getKind() == IToken.TK_LAYOUT && token.getIndex() > 0) {
			token = tokens.getTokenAt(token.getIndex() - 1);
		}
		return token.getEndOffset();
	}
	
	private boolean isDamagedNonEmptyRange(int startOffset, int endOffset) {
		return endOffset >= startOffset && isRangeOverlap(damageStart, damageEnd, startOffset, endOffset);
	}
	
	public TNode buildOutput(IAstNode oldTreeNode, List<IAstNode> repairedTreeNodes) {
		Tokenizer tokenizer =
			new Tokenizer(parser.getParseTable().getKeywordRecognizer(), filename, input);
		return buildOutput(oldTreeNode, repairedTreeNodes, tokenizer);
	}
	
	@SuppressWarnings("unchecked")
	private TNode buildOutput(IAstNode oldTreeNode, List<IAstNode> repairedTreeNodes,
			Tokenizer tokenizer) {
		// TODO: recreate tokens
		IToken leftToken = null;
		IToken rightToken = null;
		List<IAstNode> children;
		if (oldTreeNode.isList() && incrementalSorts.contains(oldTreeNode.getElementSort())) {
			List<IAstNode> oldChildren = copyChildrenToList(oldTreeNode);
			children = new ArrayList<IAstNode>(oldTreeNode.getChildCount() + repairedTreeNodes.size());
			boolean addedNewChildren = false;
			for (IAstNode oldChild : oldChildren) {
				if (!addedNewChildren && oldChild.getRightToken().getEndOffset() >= damageStart) {
					addedNewChildren = true;
					insertDamagedNodes(oldTreeNode, repairedTreeNodes, tokenizer, children);
				}
				children.add(oldChild);
			}
			if (!addedNewChildren && oldTreeNode.getRightToken().getEndOffset() + NEXT_CHAR >= damageStart)
				insertDamagedNodes(oldTreeNode, repairedTreeNodes, tokenizer, children);
		} else {
			children = copyChildrenToList(oldTreeNode);
			for (int i = 0; i < children.size(); i++) {
				children.set(i, buildOutput(children.get(i), repairedTreeNodes, tokenizer));
			}
		}
		return factory.recreateNode((TNode) oldTreeNode, leftToken, rightToken, (List<TNode>) children);
	}

	private void insertDamagedNodes(IAstNode oldTreeNode, List<IAstNode> repairedTreeNodes,
			Tokenizer tokenizer, List<IAstNode> children) {
		
		// TODO: recreate tokens
		children.addAll(repairedTreeNodes);
	}

	private static List<IAstNode> copyChildrenToList(IAstNode node) {
		List<IAstNode> results = new ArrayList<IAstNode>(node.getChildCount());
		if (node.isList() && node instanceof Iterable) { // likely a linked list
			for (Object o : ((Iterable<?>) node)) {
				results.add((IAstNode) o);
			}
		} else {
			for (int i = 0, count = node.getChildCount(); i < count; i++) {
				results.add(node.getChildAt(i));
			}
		}
		return results;
	}
}
