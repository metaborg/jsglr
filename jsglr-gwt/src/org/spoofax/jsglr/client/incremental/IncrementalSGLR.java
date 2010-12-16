package org.spoofax.jsglr.client.incremental;

import static java.lang.Math.max;
import static java.lang.Math.min;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.spoofax.jsglr.client.ITreeBuilder;
import org.spoofax.jsglr.client.ParseException;
import org.spoofax.jsglr.client.SGLR;
import org.spoofax.jsglr.client.imploder.IAstNode;
import org.spoofax.jsglr.client.imploder.ITreeFactory;
import org.spoofax.jsglr.client.imploder.TreeBuilder;
import org.spoofax.jsglr.shared.BadTokenException;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr.shared.TokenExpectedException;

/**
 * An incremental parsing extension of SGLR.
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class IncrementalSGLR<TNode extends IAstNode> {
	
	static final boolean DEBUG = true;
	
	final SGLR parser;

	final ITreeFactory<TNode> factory;
	
	final Set<String> incrementalSorts;
	
	int lastRepairedTreeNodesCount;

	/**
	 * @param incrementalSorts
	 *            Sorts that can be incrementally parsed (e.g., MethodDec, ImportDec).
	 *            *Must* be sorts that only occur in lists (such as MethodDec*).
	 */
	public IncrementalSGLR(SGLR parser, ITreeBuilder builder, ITreeFactory<TNode> factory, Set<String> incrementalSorts) {
		this.parser = parser;
		this.factory = factory;
		this.incrementalSorts = incrementalSorts;
		parser.setTreeBuilder(builder);
		assert !(builder instanceof TreeBuilder)
			|| ((TreeBuilder) builder).getFactory().getClass() == factory.getClass();
		
		// TODO: support injection sorts in incrementalSorts
		//       (using injection prods in parse table; build some class like KeywordRecognizer)
	}
	
	/**
	 * Attempts to incrementally parse an input,
	 * or falls back to normal parsing if incremental parsing fails.
	 */
	public IAstNode tryParseIncremental(String input, String filename, String startSymbol, TNode oldTree)
			throws TokenExpectedException, BadTokenException, ParseException, SGLRException {
		
		try {
			return parseIncremental(input, filename, startSymbol, oldTree);
		} catch (IncrementalSGLRException e) {
			lastRepairedTreeNodesCount = -1;
			return (IAstNode) parser.parse(input, startSymbol);
		}
	}
	
	/**
	 * Incrementally parse an input.
	 * 
	 * @throws IncrementalSGLRException
	 *             If the input could not be incrementally parsed.
	 *             It may still be possible to parse it non-incrementally.
	 */
	public IAstNode parseIncremental(String newInput, String filename, String startSymbol, TNode oldTree)
			throws TokenExpectedException, BadTokenException, ParseException, SGLRException, IncrementalSGLRException {
		
		String oldInput = oldTree.getLeftToken().getTokenizer().getInput();
		//oldInput = "{old}{old}";
		//newInput = "{old}{old}{NEW}}";
		int damageStart = getDamageStart(newInput, oldInput);
		int damageSizeChange = newInput.length() - oldInput.length();
		int damageEnd = getDamageEnd(newInput, oldInput, damageStart, damageSizeChange);
		sanityCheckDiff(oldInput, newInput, damageStart, damageEnd, damageSizeChange);
		
		if (damageSizeChange == 0 && damageEnd == damageStart - 1) {
			assert newInput.equals(oldInput);
			lastRepairedTreeNodesCount = 0;
			return oldTree;
		}
		
		IncrementalInputBuilder inputBuilder =
			new IncrementalInputBuilder(incrementalSorts, newInput, oldInput, damageStart, damageEnd, damageSizeChange);
		
		IncrementalTreeBuilder<TNode> treeBuilder =
			new IncrementalTreeBuilder<TNode>(this, newInput, filename, damageStart, damageEnd, damageSizeChange);
		
		sanityCheckOldTree(oldTree, treeBuilder.getDamagedTreeNodes(oldTree));

		String partialInput = inputBuilder.buildPartialInput(oldTree);
		IAstNode partialTree = (IAstNode) parser.parse(partialInput, startSymbol);
		
		List<IAstNode> repairedTreeNodes = treeBuilder.getRepairedTreeNodes(partialTree, inputBuilder.getLastSkippedChars());
		lastRepairedTreeNodesCount = repairedTreeNodes.size();
		sanityCheckRepairedTree(repairedTreeNodes);

		return treeBuilder.buildOutput(oldTree, repairedTreeNodes);
	}
	
	/**
	 * Returns the number of tree nodes that had to be
	 * repaired for the last incremental parse task.
	 */
	public int getLastRepairedTreeNodesCount() {
		return lastRepairedTreeNodesCount;
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

	private void sanityCheckOldTree(IAstNode oldTree, List<IAstNode> damagedNodes)
			throws IncrementalSGLRException {
		
		if (DEBUG) System.out.println("Damaged: " + damagedNodes);
		
		for (IAstNode node : damagedNodes) {
			if (!incrementalSorts.contains(node.getSort()))
				throw new IncrementalSGLRException("Precondition failed: unsafe change to tree node of type "
						+ node.getSort() + " at line " + node.getLeftToken().getLine());
		}
	}
	
	private void sanityCheckRepairedTree(List<IAstNode> repairedTreeNodes)
			throws IncrementalSGLRException {
		
		if (DEBUG) System.out.println("\nRepaired: " + repairedTreeNodes);
		
		for (IAstNode node : repairedTreeNodes) {
			if (!incrementalSorts.contains(node.getSort()))
				throw new IncrementalSGLRException("Postcondition failed: unsafe tree parsed of type "
						+ node.getSort()  + " at line " + node.getLeftToken().getLine());
		}
	}

	protected static boolean isRangeOverlap(int start1, int end1, int start2, int end2) {
		return start1 < end2 && start2 < end1;
	}

	@SuppressWarnings("unchecked")
	protected static Iterator<IAstNode> tryGetListIterator(IAstNode oldTree) {
		if (oldTree.isList() && oldTree instanceof Iterable)
			return ((Iterable<IAstNode>) oldTree).iterator();
		else
			return null;
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
