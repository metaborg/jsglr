package org.spoofax.jsglr.client.incremental;

import static java.lang.Math.max;

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
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class IncrementalSGLR<TNode extends IAstNode> {
	
	private static final boolean ASSUME_MINIMAL_DIFF = true;
	
	static final boolean DEBUG = false;
	
	final SGLR parser;

	final ITreeFactory<TNode> factory;
	
	final Set<String> incrementalSorts;

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
	
	public IAstNode parseIncremental(String input, String filename, String startSymbol, TNode oldTree)
			throws TokenExpectedException, BadTokenException, ParseException, SGLRException, IncrementalSGLRException {
		
		String oldInput = oldTree.getLeftToken().getTokenizer().getInput();
		int damageStart = getDamageStart(input, oldInput);
		int damageSizeChange = input.length() - oldInput.length();
		int damageEnd = getDamageEnd(input, oldInput, damageSizeChange,
				ASSUME_MINIMAL_DIFF ? max(damageSizeChange, 0) : damageStart);
		if (damageEnd == damageStart - 1) return oldTree;
		
		IncrementalInputBuilder inputBuilder =
			new IncrementalInputBuilder(incrementalSorts, input, damageStart, damageEnd, damageSizeChange);
		
		IncrementalTreeBuilder<TNode> treeBuilder =
			new IncrementalTreeBuilder<TNode>(this, input, filename, damageStart, damageEnd);
		
		sanityCheckOldTree(treeBuilder, oldTree);

		String partialInput = inputBuilder.buildPartialInput(oldTree);
		
		IAstNode partialTree = (IAstNode) parser.parse(partialInput, startSymbol);
		
		List<IAstNode> repairedTreeNodes = treeBuilder.getDamageTreeNodes(partialTree);
		
		sanityCheckRepairedTree(repairedTreeNodes);

		return treeBuilder.buildOutput(oldTree, repairedTreeNodes);
	}
	
	private void sanityCheckOldTree(IncrementalTreeBuilder<TNode> builder, IAstNode oldTree)
			throws IncrementalSGLRException {
		
		List<IAstNode> damagedNodes = builder.getDamageTreeNodes(oldTree);
		if (DEBUG) System.err.println("Damaged: " + damagedNodes);
		
		for (IAstNode node : damagedNodes) {
			if (!incrementalSorts.contains(node.getSort()))
				throw new IncrementalSGLRException("Precondition failed: unsafe change to tree node of type "
						+ node.getSort() + " at line " + node.getLeftToken().getLine());
		}
	}

	private void sanityCheckRepairedTree(List<IAstNode> repairedTreeNodes)
			throws IncrementalSGLRException {
		
		if (DEBUG) System.err.println("\nRepaired: " + repairedTreeNodes);
		
		for (IAstNode node : repairedTreeNodes) {
			if (!incrementalSorts.contains(node.getSort()))
				throw new IncrementalSGLRException("Postcondition failed: unsafe tree parsed at "
						+ node.getSort()  + " at line " + node.getLeftToken().getLine());
		}
	}

	protected static boolean isRangeOverlap(int start1, int end1, int start2, int end2) {
		return start1 < end2 && start2 < end1;
	}


	private int getDamageStart(String input, String oldInput) {
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) != oldInput.charAt(i)) return i;
		}
		return input.length() - 1;
	}

	private int getDamageEnd(String input, String oldInput, int offset, int damageStart) {
		for (int i = input.length() - 1; i > damageStart + offset; i--) {
			if (input.charAt(i) != oldInput.charAt(i - offset)) return i;
		}
		return damageStart - 1;
	}
}
