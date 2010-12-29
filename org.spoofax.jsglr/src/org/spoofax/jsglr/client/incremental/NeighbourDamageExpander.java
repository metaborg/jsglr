package org.spoofax.jsglr.client.incremental;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getElementSort;
import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getLeftToken;
import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getRightToken;
import static org.spoofax.jsglr.client.incremental.IncrementalSGLR.isRangeOverlap;

import java.util.Iterator;
import java.util.Set;

import org.spoofax.interpreter.terms.ISimpleTerm;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr.client.imploder.SimpleTermVisitor;
import org.spoofax.jsglr.client.imploder.Tokenizer;

/**
 * Expands the damage region to the two
 * neighbouring tree nodes, if they 
 * correspond to the set of incremental sorts.
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class NeighbourDamageExpander {

	private final ISimpleTerm oldTree;

	private final Set<String> incrementalSorts;
	
	private final int damageStart;

	private final int damageEnd;
	
	private ISimpleTerm leftNeighbour;
	
	private ISimpleTerm rightNeighbour;

	public NeighbourDamageExpander(ISimpleTerm oldTree, Set<String> incrementalSorts,
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
			return min(damageStart, getLeftToken(leftNeighbour).getStartOffset());
		}
	}
	
	public int getExpandedDamageEnd() {
		if (rightNeighbour == null) {
			return damageEnd;
		} else {
			return max(damageEnd, getRightToken(rightNeighbour).getEndOffset());
		}
	}
	
	private void initNeighbours() {
		new SimpleTermVisitor() {
			boolean done;
			
			public void preVisit(ISimpleTerm node) {
				if (node.isList() && incrementalSorts.contains(getElementSort(node))
						&& isDamagedNode(node, true, true)) {
					visitIncrementalSortsList(node);
				}
			}
			
			private void visitIncrementalSortsList(ISimpleTerm list) {
				boolean foundDamagedNode = false;
				
				Iterator<ISimpleTerm> iterator = tryGetListIterator(list);
				ISimpleTerm lastChild = null;
				for (int i = 0, max = list.getSubtermCount(); i < max; i++) {
					ISimpleTerm child = iterator == null ? list.getSubterm(i) : iterator.next();
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

	private boolean isDamagedNode(ISimpleTerm node, boolean considerLeftLayout, boolean considerRightLayout) {
		IToken left = getLeftToken(node);
		IToken right = getRightToken(node);
		if (left == null || right == null) return false;
		if (considerLeftLayout)
			left = Tokenizer.findLeftMostLayoutToken(left);
		if (considerRightLayout)
			right = Tokenizer.findRightMostLayoutToken(right);
		int startOffset = left.getStartOffset();
		int endOffset = right.getEndOffset();
		return isRangeOverlap(damageStart, damageEnd, startOffset, endOffset);
	}

	private boolean isLeftCollateralDamage(ISimpleTerm child) {
		assert isDamagedNode(child, true, true);
		return !isDamagedNode(child, true, false);
	}

	private boolean isRightCollateralDamage(ISimpleTerm child) {
		assert isDamagedNode(child, true, true);
		return !isDamagedNode(child, false, true);
	}
}