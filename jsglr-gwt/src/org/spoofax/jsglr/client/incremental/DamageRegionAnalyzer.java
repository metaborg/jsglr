package org.spoofax.jsglr.client.incremental;

import static org.spoofax.jsglr.client.imploder.Tokenizer.findLeftMostLayoutToken;
import static org.spoofax.jsglr.client.imploder.Tokenizer.findRightMostLayoutToken;
import static org.spoofax.jsglr.client.incremental.IncrementalSGLR.isRangeOverlap;
import static org.spoofax.jsglr.client.incremental.IncrementalSGLR.tryGetListIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.spoofax.jsglr.client.imploder.IAstNode;
import org.spoofax.jsglr.client.imploder.IToken;

/**
 * Analyzes the trees before and after incremental parsing,
 * determining the tree nodes that are in the damage region.
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class DamageRegionAnalyzer {

	final Set<String> incrementalSorts;
	
	final int damageStart;

	final int damageEnd;
	
	final int damageSizeChange;

	public DamageRegionAnalyzer(IncrementalSGLR<?> parser, int damageStart, int damageEnd, int damageSizeChange) {
		this.incrementalSorts = parser.incrementalSorts;
		this.damageStart = damageStart;
		this.damageEnd = damageEnd;
		this.damageSizeChange = damageSizeChange;
	}
	
	/**
	 * Gets all non-list tree nodes from the original tree
	 * that are in the damage region according to {@link #isDamageTreeNode}.
	 */
	public List<IAstNode> getDamageNodes(IAstNode tree) {
		return getDamageRegionTreeNodes(tree, new ArrayList<IAstNode>(), true, 0);
	}
	
	/**
	 * Gets all non-list tree nodes from the partial result tree
	 * that are in the damage region according to {@link #isDamageTreeNode}.
	 */
	public List<IAstNode> getDamageNodesForPartialTree(IAstNode tree, int skippedChars) {
		return getDamageRegionTreeNodes(tree, new ArrayList<IAstNode>(), false, skippedChars);
	}

	private List<IAstNode> getDamageRegionTreeNodes(IAstNode tree, List<IAstNode> results, boolean isOriginalTree, int skippedChars) {
		if (!tree.isList() && isDamageTreeNode(tree, isOriginalTree, skippedChars)) {
			results.add(tree);
		} else {
			// Recurse
			Iterator<IAstNode> iterator = tryGetListIterator(tree); 
			for (int i = 0, max = tree.getChildCount(); i < max; i++) {
				IAstNode child = iterator == null ? tree.getChildAt(i) : iterator.next();
				getDamageRegionTreeNodes(child, results, isOriginalTree, skippedChars);
			}
		}
		return results;
	}

	/**
	 * Determines if the damage region affects a particular tree node,
	 * looking only at those tokens that actually belong to the node
	 * and not to its children. Also returns true for nodes in the region
	 * with a sort in {@link #incrementalSorts} regardless of whether they own the tokens
	 * or not.
	 */
	protected boolean isDamageTreeNode(IAstNode tree, boolean isOriginalTree, int skippedChars) {
		IToken current = findLeftMostLayoutToken(tree.getLeftToken());
		IToken last = findRightMostLayoutToken(tree.getRightToken());
		if (current != null && last != null) {
			if (!isDamagedRange(
					current.getStartOffset(), last.getEndOffset(), isOriginalTree, skippedChars))
				return false;
			if (incrementalSorts.contains(tree.getSort()))
				return true;
			Iterator<IAstNode> iterator = tryGetListIterator(tree); 
			for (int i = 0, max = tree.getChildCount(); i < max; i++) {
				IAstNode child = iterator == null ? tree.getChildAt(i) : iterator.next();
				IToken childLeft = findLeftMostLayoutToken(child.getLeftToken());
				IToken childRight = findRightMostLayoutToken(child.getRightToken());
				if (childLeft != null && childRight != null) {
					if (childLeft.getIndex() > current.getIndex()
							&& isDamagedRange(
									current.getStartOffset(), childLeft.getStartOffset() - 1,
									isOriginalTree, skippedChars)) {
						return true;
					}
					current = childRight;
				}
			}
			return isDamagedRange(
					current.getEndOffset() + 1, last.getEndOffset(), isOriginalTree, skippedChars);
		} else {
			return false;
		}
	}
	
	private boolean isDamagedRange(int startOffset, int endOffset,
			boolean isOriginalTree, int skippedChars) {
		if (isOriginalTree) {
			return /*endOffset >= startOffset
				&&*/ isRangeOverlap(damageStart, damageEnd, startOffset, endOffset);
		} else {
			return /*endOffset >= startOffset
				&&*/ isRangeOverlap(damageStart - skippedChars, damageEnd - skippedChars + damageSizeChange,
						startOffset, endOffset);
		}
	}
}
