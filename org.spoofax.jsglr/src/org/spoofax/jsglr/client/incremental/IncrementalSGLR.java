package org.spoofax.jsglr.client.incremental;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getLeftToken;
import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getSort;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.spoofax.interpreter.terms.ISimpleTerm;
import org.spoofax.jsglr.client.ParseException;
import org.spoofax.jsglr.client.SGLR;
import org.spoofax.jsglr.client.imploder.ITreeFactory;
import org.spoofax.jsglr.shared.BadTokenException;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr.shared.TokenExpectedException;
import org.spoofax.terms.SimpleTermVisitor;

/**
 * An incremental parsing extension of SGLR.
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class IncrementalSGLR<TNode extends ISimpleTerm> {
	
	public static boolean DEBUG = false;
	
	final SGLR parser;

	final ITreeFactory<TNode> factory;
	
	final Set<String> incrementalSorts;
	
	private final CommentDamageExpander comments;
	
	private List<TNode> lastReconstructedNodes;

	/**
	 * Creates a new, reusable IncrementalSGLR instance.
	 * 
	 * @see SortAnalyzer#getInjectionsTo()
	 *            Can be used to determine the injections for incrementalSorts.
	 * 
	 * @param incrementalSorts
	 *            Sorts that can be incrementally parsed (e.g., MethodDec, ImportDec).
	 *            *Must* be sorts that only occur in lists (such as MethodDec*).
	 */
	public IncrementalSGLR(SGLR parser, CommentDamageExpander comments, ITreeFactory<TNode> factory,
			Set<String> incrementalSorts) {
		this.parser = parser;
		this.comments = comments;
		this.factory = factory;
		this.incrementalSorts = incrementalSorts;
	}
	
	/**
	 * Incrementally parse an input.
	 * 
	 * @see #getLastReconstructedNodes()
	 *             Gets the list of tree nodes that were reconstructed
	 *             after running {@link #parseIncremental}.
	 * 
	 * @throws IncrementalSGLRException
	 *             If the input could not be incrementally parsed.
	 *             It may still be possible to parse it non-incrementally.
	 */
	public TNode parseIncremental(String newInput, String filename, String startSymbol, TNode oldTree)
			throws TokenExpectedException, BadTokenException, ParseException, SGLRException, IncrementalSGLRException {
		
		if (oldTree == null)
			throw new IncrementalSGLRException("Precondition failed: oldTree is null");

		// Determine damage size
		lastReconstructedNodes = Collections.emptyList();
		String oldInput = getLeftToken(oldTree).getTokenizer().getInput();
		int damageStart = getDamageStart(newInput, oldInput);
		int damageSizeChange = newInput.length() - oldInput.length();
		int damageEnd = getDamageEnd(newInput, oldInput, damageStart, damageSizeChange);
		sanityCheckDiff(oldInput, newInput, damageStart, damageEnd, damageSizeChange);
		
		if (damageSizeChange == 0 && damageEnd == damageStart - 1) {
			assert newInput.equals(oldInput);
			return oldTree;
		}

		// Expand damage size as needed
		damageStart = comments.getExpandedDamageStart(newInput, damageStart, damageEnd, damageSizeChange);
		damageEnd = comments.getExpandedDamageEnd(newInput, damageStart, damageEnd, damageSizeChange);
		NeighbourDamageExpander neighbours = new NeighbourDamageExpander(oldTree, incrementalSorts, damageStart, damageEnd);
		damageStart = neighbours.getExpandedDamageStart();
		damageEnd = neighbours.getExpandedDamageEnd();
		sanityCheckDiff(oldInput, newInput, damageStart, damageEnd, damageSizeChange);
		
		// Analyze damage
		DamageRegionAnalyzer damageAnalyzer = new DamageRegionAnalyzer(this, damageStart, damageEnd, damageSizeChange);
		IncrementalInputBuilder inputBuilder = new IncrementalInputBuilder(damageAnalyzer, newInput, oldInput);
		sanityCheckOldTree(oldTree, damageAnalyzer.getDamageNodes(oldTree));

		// Construct and parse partial input
		String partialInput = inputBuilder.buildPartialInput(oldTree);
		int skippedChars = inputBuilder.getLastSkippedCharsBeforeDamage();
		ISimpleTerm partialTree = (ISimpleTerm) parser.parse(partialInput, filename, startSymbol);
		List<ISimpleTerm> repairedNodes = damageAnalyzer.getDamageNodesForPartialTree(partialTree, skippedChars);
		sanityCheckRepairedTree(repairedNodes);
		
		// Combine old tree with new partial tree
		IncrementalTreeBuilder<TNode> treeBuilder = 
			new IncrementalTreeBuilder<TNode>(this, damageAnalyzer, newInput, filename, repairedNodes, skippedChars);
		TNode result = treeBuilder.buildOutput(oldTree);
		lastReconstructedNodes = treeBuilder.getLastReconstructedNodes();
		return result;
	}
	
	/**
	 * Returns the list of tree nodes that was reconstructed
	 * for the last incremental parse.
	 */
	public List<TNode> getLastReconstructedNodes() {
		return lastReconstructedNodes;
	}
	
	public SGLR getParser() {
		return parser;
	}

	private void sanityCheckDiff(String oldInput, String newInput,
			int damageStart, int damageEnd, int damageSizeChange) throws IncrementalSGLRException {
		if (!(damageStart <= damageEnd + 1
			&& damageStart <= damageEnd + damageSizeChange + 1)) {
			throw new IncrementalSGLRException("Precondition failed: unable to determine valid diff");
		}
		assert (oldInput.substring(0, damageStart)
			+ newInput.substring(damageStart, damageEnd + damageSizeChange + 1)
			+ oldInput.substring(damageEnd + 1)).equals(newInput);
	}

	private void sanityCheckOldTree(ISimpleTerm oldTree, List<ISimpleTerm> damagedNodes)
			throws IncrementalSGLRException {
		
		if (DEBUG) System.out.println("Damaged: " + damagedNodes);
		
		if (isAmbiguous(oldTree)) {
			// Not yet supported by IncrementalTreeBuilder
			throw new IncrementalSGLRException("Postcondition failed: old tree is ambiguous");
		}
		
		for (ISimpleTerm node : damagedNodes) {
			if (!incrementalSorts.contains(getSort(node)))
				throw new IncrementalSGLRException("Precondition failed: unsafe change to tree node of type "
						+ getSort(node) + " at line " + getLeftToken(node).getLine());
		}
	}
	
	private void sanityCheckRepairedTree(List<ISimpleTerm> repairedTreeNodes)
			throws IncrementalSGLRException {
		
		if (DEBUG) System.out.println("\nRepaired: " + repairedTreeNodes);
		
		for (ISimpleTerm node : repairedTreeNodes) {
			if (!incrementalSorts.contains(getSort(node))) {
				throw new IncrementalSGLRException("Postcondition failed: unsafe tree parsed of type "
						+ getSort(node)  + " at line " + getLeftToken(node).getLine());
			} else if (isAmbiguous(node)) {
				// Not yet supported by IncrementalTreeBuilder
				throw new IncrementalSGLRException("Postcondition failed: updated tree is ambiguous");
			}
		}
	}
	
	private static boolean isAmbiguous(ISimpleTerm tree) {
		return getLeftToken(tree).getTokenizer().isAmbigous();
	}

	protected static boolean isRangeOverlap(int start1, int end1, int start2, int end2) {
		return start1 <= end2 && start2 <= end1; // e.g. testJava55
	}

	static Iterator<ISimpleTerm> tryGetListIterator(ISimpleTerm tree) {
		return SimpleTermVisitor.tryGetListIterator(tree);
	}


	private int getDamageStart(String input, String oldInput) {
		int limit = min(input.length(), oldInput.length());
		for (int i = 0; i < limit; i++) {
			if (input.charAt(i) != oldInput.charAt(i)) return i;
		}
		return limit - 1;
	}

	private int getDamageEnd(String input, String oldInput, int damageStart, int damageSizeChange) {
		int limit = max(damageStart, damageStart - damageSizeChange) -1;
		for (int i = oldInput.length() - 1; i > limit; i--) {
			if (oldInput.charAt(i) != input.charAt(i + damageSizeChange))
				return i;
		}
		return limit;
	}
}
