package org.spoofax.jsglr.client;

import static java.lang.Math.max;
import static java.lang.Math.min;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.spoofax.jsglr.client.imploder.IAstNode;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr.client.imploder.ITreeFactory;
import org.spoofax.jsglr.client.imploder.Tokenizer;
import org.spoofax.jsglr.client.imploder.TreeBuilder;
import org.spoofax.jsglr.shared.BadTokenException;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr.shared.TokenExpectedException;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class IncrementalSGLR<TNode extends IAstNode> {
	
	/**
	 * Allows for one character of extra slack based on the assumption
	 * that grammars will not have two consecutive IncrementalSort*
	 * lists.
	 */
	private static final int NEXT_CHAR = 1;
	
	private static final boolean ASSUME_MINIMAL_DIFF = true;
	
	private static final boolean DEBUG = true;
	
	private final SGLR parser;

	private final ITreeFactory<TNode> factory;
	
	private final Set<String> incrementalSorts;
	
	private boolean isDamageIncluded;

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
		int afterDamageOffset = input.length() - oldInput.length();
		int damageEnd = getDamageEnd(input, oldInput, afterDamageOffset,
				ASSUME_MINIMAL_DIFF ? max(afterDamageOffset, 0) : damageStart);
		if (damageEnd == damageStart - 1) return oldTree;
		
		sanityCheckOldTree(oldTree, damageStart, damageEnd);
		
		String partialInput = buildPartialInput(input, damageStart, afterDamageOffset, damageEnd, oldTree);
		
		IAstNode partialTree = (IAstNode) parser.parse(partialInput, startSymbol);
		
		List<IAstNode> repairedTreeNodes =
			getDamageTreeNodes(partialTree, damageStart, damageEnd, new ArrayList<IAstNode>());
		if (DEBUG) System.out.println("\nRepaired: " + repairedTreeNodes);
		sanityCheckRepairedTree(repairedTreeNodes);

		Tokenizer tokenizer = new Tokenizer(parser.getParseTable().getKeywordRecognizer(), filename, oldInput);
		return buildOutput(oldTree, repairedTreeNodes, damageStart, damageEnd, tokenizer);
	}
	
	private void sanityCheckOldTree(IAstNode oldTree, int damageStart, int damageEnd)
			throws IncrementalSGLRException {
		
		List<IAstNode> damagedNodes =
			getDamageTreeNodes(oldTree, damageStart, damageEnd, new ArrayList<IAstNode>());
		for (IAstNode node : damagedNodes) {
			if (!incrementalSorts.contains(node.getSort()))
				throw new IncrementalSGLRException("Unsafe change to tree node of type "
						+ node.getSort() + " at line " + node.getLeftToken().getLine());
		}
	}

	private void sanityCheckRepairedTree(List<IAstNode> repairedTreeNodes)
			throws IncrementalSGLRException {
		
		for (IAstNode node : repairedTreeNodes) {
			if (!incrementalSorts.contains(node.getSort()))
				throw new IncrementalSGLRException("Unsafe tree parsed at "
						+ node.getSort()  + " at line " + node.getLeftToken().getLine());
		}
	}

	private String buildPartialInput(String input, int damageStart,
			int afterDamageOffset, int damageEnd, TNode oldTree)
			throws IncrementalSGLRException {
		
		StringBuilder result = new StringBuilder(input.length());
		isDamageIncluded = false;
		buildPartialInput(input, damageStart, damageEnd, afterDamageOffset, oldTree, 0, result);
		return result.toString();
	}

	private int buildPartialInput(String input, int damageStart, int damageEnd, 
			int afterDamageOffset, IAstNode oldTree, int offset, StringBuilder result)
			throws IncrementalSGLRException {
		
		IToken left = oldTree.getLeftToken();
		IToken right = oldTree.getRightToken();
		int startOffset = 0;
		int endOffset = 0;
		
		// Print incrementalSorts nodes
		if (left != null && right != null) {
			startOffset = left.getStartOffset();
			endOffset = right.getEndOffset();
			if (!oldTree.isList() && incrementalSorts.contains(oldTree.getSort())) {
				if (isRangeOverlap(damageStart, damageEnd, startOffset, endOffset)) {
					if (!isDamageIncluded) {
						isDamageIncluded = true;
						// append(input, offset, offset + afterDamageOffset + 1, result);
						append(input, offset, damageStart, result);
						if (DEBUG) System.out.print('|');
						append(input, damageStart, damageEnd + 1, result);
						// if (DEBUG) System.out.print('|');
						// append(input, damageEnd + 1, offset + afterDamageOffset + 1, result);
						// so maybe: offset + afterDamageOffset + 1 - (offset + afterDamageOffset - damageEnd)
						//         = damageEnd + 1
						if (DEBUG) System.out.print('|');
						// append(input, offset + afterDamageOffset + 1, endOffset + afterDamageOffset + 1, result);
						append(input, damageEnd + 1, endOffset + afterDamageOffset + 1, result);
						if (DEBUG) System.out.print('|');
					}
					// possible: appendWhitespace(input, startOffset, endOffset, result);
				} else {
					appendWhitespace(input, offset, endOffset, result);
				}
				return endOffset + 1;
			}
		}
		
		// Recurse
		if (oldTree.isList() && oldTree instanceof Iterable) { // likely a linked list
			for (Object o : (Iterable<?>) oldTree) {
				IAstNode child = (IAstNode) o;
				offset = buildPartialInput(input, damageStart, damageEnd, afterDamageOffset, child, offset, result);
			}
		} else {
			for (int i = 0, count = oldTree.getChildCount(); i < count; i++) {
				IAstNode child = oldTree.getChildAt(i);
				offset = buildPartialInput(input, damageStart, damageEnd, afterDamageOffset, child, offset, result);
			}
		}
		
		/*
		for (int i = 0, count = oldTree.getChildCount(); i < count; i++) {
			IAstNode child = oldTree.getChildAt(i);
			IToken childLeft = child.getLeftToken();
			IToken childRight = child.getRightToken();
			if (childLeft != null && childRight != null) {
				
				startOffset = childRight.getEndOffset() + 1;
			}
		}
		*/

		// Print original text
		if (left != null && right != null) {
			return appendPartialInput(input, damageStart, damageEnd, afterDamageOffset, oldTree, offset, endOffset, result);
		} else {
			return offset;
		}
	}

	private void appendWhitespace(String input, int offset, int endOffset, StringBuilder result) {
		for (int i = offset; i <= endOffset; i++) {
			// if (DEBUG) System.out.print(input.charAt(i) == '\n' ? "\n" : "-" + input.charAt(i));
			result.append(input.charAt(i) == '\n' ? '\n' : ' ');
		}
	}

	private int appendPartialInput(String input, int damageStart, int damageEnd,
			int afterDamageOffset, IAstNode oldTree, int offset, int endOffset, StringBuilder result) {
		if (offset >= damageStart) {
			if (!isDamageIncluded) {
				append(input, damageStart, damageEnd + 1, result);
				isDamageIncluded = true;
			}
			append(input, offset + afterDamageOffset, endOffset + afterDamageOffset + 1, result);
		} else {
			append(input, offset, min(endOffset + 1, damageStart), result);
		}
		return endOffset + 1;
	}
	
	private static void append(String input, int offset, int endOffset, StringBuilder result) {
		if (DEBUG) System.out.print(input.substring(offset, endOffset));
		result.append(input, offset, endOffset);
 	}

	private static boolean isRangeOverlap(int start1, int end1, int start2, int end2) {
		return start1 <= end2 && start2 <= end1;
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
	
	/**
	 * Gets all non-list tree nodes that are in the damaged region
	 * according to {@link #isDamageTreeNode}.
	 */
	private List<IAstNode> getDamageTreeNodes(IAstNode tree, int damageStart, int damageEnd,
			List<IAstNode> results) {
		
		if (tree.isList()) { // ignored for getDamageTreeNodes
			getDamageTreeNodesRecurse(tree, damageStart, damageEnd, results);
		} else {
			if (isDamageTreeNode(tree, damageStart, damageEnd)) {
				results.add(tree);
			} else {
				getDamageTreeNodesRecurse(tree, damageStart, damageEnd, results);
			}
		}
		return results;
	}

	private void getDamageTreeNodesRecurse(IAstNode tree, int damageStart, int damageEnd,
			List<IAstNode> results) {
		
		if (tree instanceof Iterable) { // likely a linked list
			for (Object o : (Iterable<?>) tree) {
				getDamageTreeNodes((IAstNode) o, damageStart, damageEnd, results);
			}
		} else {
			for (int i = 0, count = tree.getChildCount(); i < count; i++) {
				getDamageTreeNodes(tree.getChildAt(i), damageStart, damageEnd, results);
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
	private boolean isDamageTreeNode(IAstNode tree, int damageStart, int damageEnd) {
		IToken left = tree.getLeftToken();
		IToken right = tree.getRightToken();
		if (left != null && right != null) {
			int startOffset = left.getStartOffset();
			int endOffset = right.getEndOffset();
			
			if (!isRangeOverlap(damageStart, damageEnd, startOffset, endOffset))
				return false;
			if (incrementalSorts.contains(tree.getSort()))
				return true;
			for (int i = 0, count = tree.getChildCount(); i < count; i++) {
				IAstNode child = tree.getChildAt(i);
				IToken childLeft = child.getLeftToken();
				IToken childRight = child.getRightToken();
				if (childLeft != null && childRight != null) {
					if (isRangeOverlap(damageStart, damageEnd, startOffset, childLeft.getStartOffset() - 1)) {
						return true;
					}
					startOffset = childRight.getEndOffset() + 1;
				}
			}
			return isRangeOverlap(damageStart, damageEnd, startOffset, endOffset);
		} else {
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	private IAstNode buildOutput(IAstNode oldTreeNode, List<IAstNode> repairedTreeNodes,
			int damageStart, int damageEnd,Tokenizer tokenizer) {
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
					insertDamagedNodes(oldTreeNode, repairedTreeNodes, damageStart, damageEnd, tokenizer, children);
				}
				children.add(oldChild);
			}
			if (!addedNewChildren && oldTreeNode.getRightToken().getEndOffset() + NEXT_CHAR >= damageStart)
				insertDamagedNodes(oldTreeNode, repairedTreeNodes, damageStart, damageEnd, tokenizer, children);
		} else {
			children = copyChildrenToList(oldTreeNode);
			for (int i = 0; i < children.size(); i++) {
				children.set(i, buildOutput(children.get(i), repairedTreeNodes, damageStart, damageEnd, tokenizer));
			}
		}
		return factory.recreateNode((TNode) oldTreeNode, leftToken, rightToken, (List<TNode>) children);
	}

	private void insertDamagedNodes(IAstNode oldTreeNode, List<IAstNode> repairedTreeNodes,
			int damageStart, int damageEnd, Tokenizer tokenizer, List<IAstNode> children) {
		
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
