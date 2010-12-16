package org.spoofax.jsglr.client.incremental;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.spoofax.jsglr.client.incremental.IncrementalSGLR.isRangeOverlap;

import java.util.Iterator;
import java.util.Set;

import org.spoofax.jsglr.client.imploder.AstNodeVisitor;
import org.spoofax.jsglr.client.imploder.IAstNode;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr.client.imploder.Tokenizer;

/**
 * Expands the damage region to the two
 * neighbouring tree nodes, if they 
 * correspond to the set of incremental sorts.
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class NeighbourDamageExpander {

	private final IAstNode oldTree;

	private final Set<String> incrementalSorts;
	
	private final int damageStart;

	private final int damageEnd;
	
	private IAstNode leftNeighbour;
	
	private IAstNode rightNeighbour;

	public NeighbourDamageExpander(IAstNode oldTree, Set<String> incrementalSorts,
			int damageStart, int damageEnd) {
		this.oldTree = oldTree;
		this.incrementalSorts = incrementalSorts;
		this.damageStart = damageStart;
		this.damageEnd = damageEnd;
		initNeighbours();
	}
	
	public int getExpandedDamageStart() {
		if (leftNeighbour == null) {
			return damageStart;
		} else {
			return min(damageStart, leftNeighbour.getLeftToken().getStartOffset());
		}
	}
	
	public int getExpandedDamageEnd() {
		if (rightNeighbour == null) {
			return damageEnd;
		} else {
			return max(damageEnd, rightNeighbour.getRightToken().getEndOffset());
		}
	}
	
	private void initNeighbours() {
		new AstNodeVisitor() {
			boolean done;
			
			public void preVisit(IAstNode node) {
				if (node.isList() && incrementalSorts.contains(node.getElementSort())
						&& isDamagedNode(node, true, true)) {
					visitIncrementalSortsList(node);
				}
			}
			
			private void visitIncrementalSortsList(IAstNode list) {
				boolean foundDamagedNode = false;
				
				Iterator<IAstNode> iterator = tryGetListIterator(list);
				IAstNode lastChild = null;
				for (int i = 0, max = list.getChildCount(); i < max; i++) {
					IAstNode child = iterator == null ? list.getChildAt(i) : iterator.next();
					if (!foundDamagedNode) {
						if (isDamagedNode(child, true, true)) {
							if (leftNeighbour == null || isLeftCollateralDamage(child))
								leftNeighbour = child;
							foundDamagedNode = true;
							rightNeighbour = child; // next node, if any, replaces this value
						} else {
							leftNeighbour = child;
						}
					} else if (isDamagedNode(child, true, true)) {
						rightNeighbour = child;
					} else {
						rightNeighbour = isRightCollateralDamage(rightNeighbour) ? rightNeighbour : child;
						done = true;
						return;
					}
					lastChild = child;
				}
				if (rightNeighbour == null) rightNeighbour = lastChild;
			}
			
			public boolean isDone() {
				return done;
			}
		}.visit(oldTree);
	}

	private boolean isDamagedNode(IAstNode node, boolean considerLeftLayout, boolean considerRightLayout) {
		IToken left = node.getLeftToken();
		IToken right = node.getRightToken();
		if (left == null || right == null) return false;
		if (considerLeftLayout)
			left = Tokenizer.findLeftMostLayoutToken(left);
		if (considerRightLayout)
			right = Tokenizer.findRightMostLayoutToken(right);
		int startOffset = left.getStartOffset();
		int endOffset = right.getEndOffset();
		return isRangeOverlap(damageStart, damageEnd, startOffset, endOffset);
	}

	private boolean isLeftCollateralDamage(IAstNode child) {
		assert isDamagedNode(child, true, true);
		return !isDamagedNode(child, true, false);
	}

	private boolean isRightCollateralDamage(IAstNode child) {
		assert isDamagedNode(child, true, true);
		return !isDamagedNode(child, false, true);
	}
}