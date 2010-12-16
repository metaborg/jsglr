package org.spoofax.jsglr.client.incremental;

import static org.spoofax.jsglr.client.imploder.Tokenizer.findLeftMostLayoutToken;
import static org.spoofax.jsglr.client.imploder.Tokenizer.findRightMostLayoutToken;
import static org.spoofax.jsglr.client.incremental.IncrementalSGLR.isRangeOverlap;
import static org.spoofax.jsglr.client.incremental.IncrementalSGLR.tryGetListIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.spoofax.jsglr.client.SGLR;
import org.spoofax.jsglr.client.imploder.IAstNode;
import org.spoofax.jsglr.client.imploder.IToken;
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
	
	private final int damageSizeChange;

	/**
	 * @param incrementalSorts
	 *            Sorts that can be incrementally parsed (e.g., MethodDec, ImportDec).
	 *            *Must* be sorts that only occur in lists (such as MethodDec*).
	 */
	public IncrementalTreeBuilder(IncrementalSGLR<TNode> parser, String input, String filename,
			int damageStart, int damageEnd, int damageSizeChange) {
		this.parser = parser.parser;
		this.input = input;
		this.filename = filename;
		this.factory = parser.factory;
		this.incrementalSorts = parser.incrementalSorts;
		this.damageStart = damageStart;
		this.damageEnd = damageEnd;
		this.damageSizeChange = damageSizeChange;
	}
	
	/**
	 * Gets all non-list tree nodes from the original tree
	 * that are in the damaged region according to {@link #isDamageTreeNode}.
	 */
	public List<IAstNode> getDamagedTreeNodes(IAstNode tree) {
		return getDamagedRegionTreeNodes(tree, new ArrayList<IAstNode>(), true, 0);
	}
	
	/**
	 * Gets all non-list tree nodes from the partial result tree
	 * that are in the damaged region according to {@link #isDamageTreeNode}.
	 */
	public List<IAstNode> getRepairedTreeNodes(IAstNode tree, int skippedChars) {
		return getDamagedRegionTreeNodes(tree, new ArrayList<IAstNode>(), false, skippedChars);
	}

	private List<IAstNode> getDamagedRegionTreeNodes(IAstNode tree, List<IAstNode> results, boolean isOriginalTree, int skippedChars) {
		if (!tree.isList() && isDamageTreeNode(tree, isOriginalTree, skippedChars)) {
			results.add(tree);
		} else {
			// Recurse
			Iterator<IAstNode> iterator = tryGetListIterator(tree); 
			for (int i = 0, max = tree.getChildCount(); i < max; i++) {
				IAstNode child = iterator == null ? tree.getChildAt(i) : iterator.next();
				getDamagedRegionTreeNodes(child, results, isOriginalTree, skippedChars);
			}
		}
		return results;
	}

	/**
	 * Determines if the damaged region affects a particular tree node,
	 * looking only at those tokens that actually belong to the node
	 * and not to its children. Also returns true for nodes with a sort 
	 * in {@link #incrementalSorts} regardless of whether they own the tokens
	 * or not.
	 */
	protected boolean isDamageTreeNode(IAstNode tree, boolean isOriginalTree, int skippedChars) {
		IToken current = findLeftMostLayoutToken(tree.getLeftToken());
		IToken last = findRightMostLayoutToken(tree.getRightToken());
		if (current != null && last != null) {
			if (!isDamagedNonEmptyRange(
					current.getStartOffset(), last.getEndOffset(), isOriginalTree, skippedChars))
				return false;
			if (incrementalSorts.contains(tree.getSort()))
				return true;
			Iterator<IAstNode> iterator = tryGetListIterator(tree); 
			for (int i = 0, max = tree.getChildCount(); i < max; i++) {
				IAstNode child = iterator == null ? tree.getChildAt(i) : iterator.next();
				IToken childLeft = child.getLeftToken();
				IToken childRight = findRightMostLayoutToken(child.getRightToken());
				if (childLeft != null && childRight != null) {
					if (childLeft.getIndex() > current.getIndex()
							&& isDamagedNonEmptyRange(
									current.getStartOffset(), childLeft.getStartOffset() - 1,
									isOriginalTree, skippedChars)) {
						return true;
					}
					current = childRight;
				}
			}
			return isDamagedNonEmptyRange(
					current.getEndOffset() + 1, last.getEndOffset(), isOriginalTree, skippedChars);
		} else {
			return false;
		}
	}
	
	private boolean isDamagedNonEmptyRange(int startOffset, int endOffset,
			boolean isOriginalTree, int skippedChars) {
		// TODO: get rid of non-empty criterion?? at the very least for empty damage regions...
		if (isOriginalTree) {
			return /*endOffset >= startOffset
				&&*/ isRangeOverlap(damageStart, damageEnd, startOffset, endOffset);
		} else {
			return /*endOffset >= startOffset
				&&*/ isRangeOverlap(damageStart - skippedChars, damageEnd - skippedChars + damageSizeChange,
						startOffset, endOffset);
		}
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

	private static List<IAstNode> copyChildrenToList(IAstNode tree) {
		List<IAstNode> results = new ArrayList<IAstNode>(tree.getChildCount());
		Iterator<IAstNode> iterator = tryGetListIterator(tree); 
		for (int i = 0, max = tree.getChildCount(); i < max; i++) {
			IAstNode child = iterator == null ? tree.getChildAt(i) : iterator.next();
			results.add(child);
		}
		return results;
	}
}
