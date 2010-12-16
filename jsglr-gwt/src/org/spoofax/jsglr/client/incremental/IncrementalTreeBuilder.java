package org.spoofax.jsglr.client.incremental;

import static java.lang.Math.min;
import static org.spoofax.jsglr.client.imploder.IToken.TK_EOF;
import static org.spoofax.jsglr.client.imploder.IToken.TK_ERROR;
import static org.spoofax.jsglr.client.imploder.IToken.TK_ERROR_EOF_UNEXPECTED;
import static org.spoofax.jsglr.client.imploder.IToken.TK_ERROR_KEYWORD;
import static org.spoofax.jsglr.client.imploder.IToken.TK_UNKNOWN;
import static org.spoofax.jsglr.client.imploder.Tokenizer.findLeftMostLayoutToken;
import static org.spoofax.jsglr.client.imploder.Tokenizer.findRightMostLayoutToken;
import static org.spoofax.jsglr.client.imploder.Tokenizer.getTokenAfter;
import static org.spoofax.jsglr.client.imploder.Tokenizer.getTokenBefore;
import static org.spoofax.jsglr.client.incremental.IncrementalSGLR.tryGetListIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.spoofax.jsglr.client.imploder.IAstNode;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr.client.imploder.ITokenizer;
import org.spoofax.jsglr.client.imploder.ITreeFactory;
import org.spoofax.jsglr.client.imploder.Tokenizer;

/**
 * Constructs the output tree based on the old tree and the list of repaired tree nodes.
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class IncrementalTreeBuilder<TNode extends IAstNode> {
	
	private static final int NO_STOP_OFFSET = Integer.MAX_VALUE;

	private final ITreeFactory<TNode> factory;
	
	private final Set<String> incrementalSorts;

	private final int damageStart;

	private final int damageEnd;

	private final int skippedChars;
	
	private final int damageSizeChange;

	private final List<IAstNode> repairedNodes;

	private final Tokenizer newTokenizer;

	private final DamageRegionAnalyzer damageAnalyzer;
	
	private final List<TNode> reconstructedNodes = new ArrayList<TNode>();
	
	private boolean isRepairedNodesInserted;

	/**
	 * @param skippedChars @see {@link IncrementalInputBuilder#getLastSkippedCharsBeforeDamage()}
	 */
	public IncrementalTreeBuilder(IncrementalSGLR<TNode> parser, DamageRegionAnalyzer damageAnalyzer,
			String input, String filename, List<IAstNode> repairedTreeNodes, int skippedChars) {
		this.damageAnalyzer = damageAnalyzer;
		this.factory = parser.factory;
		this.incrementalSorts = damageAnalyzer.incrementalSorts;
		this.damageStart = damageAnalyzer.damageStart;
		this.damageEnd = damageAnalyzer.damageEnd;
		this.skippedChars = skippedChars;
		this.damageSizeChange = damageAnalyzer.damageSizeChange;
		this.repairedNodes = repairedTreeNodes;
		this.newTokenizer = new Tokenizer(parser.parser.getParseTable().getKeywordRecognizer(), filename, input);
	}
	
	/**
	 * Builds the output tree based on the old tree and the list of repaired tree nodes.
	 */
	public TNode buildOutput(IAstNode oldTreeNode) throws IncrementalSGLRException {
		isRepairedNodesInserted = false;
		TNode result = buildOutputSubtree(oldTreeNode, 0);
		if (!isRepairedNodesInserted)
			throw new IncrementalSGLRException("Postcondition failed: unable to insert repaired tree nodes in original tree: " + repairedNodes);
		newTokenizer.makeToken(newTokenizer.getStartOffset() - 1, TK_EOF, true);
		return result;
	}
	
	public List<TNode> getLastReconstructedNodes() {
		return reconstructedNodes;
	}
	
	private TNode buildOutputSubtree(IAstNode oldTreeNode, int offsetChange) {

		final List<IAstNode> children;
		final IToken beforeStartToken = newTokenizer.currentToken();
		IToken startToken = oldTreeNode.getLeftToken();
		
		sanityCheckOldTreeNode(oldTreeNode);
		
		// TODO: copy tokens before first child??
		if (oldTreeNode.isList() && incrementalSorts.contains(oldTreeNode.getElementSort())) {
			assert offsetChange == 0 : "Nested incrementalSorts lists?";
			children = new ArrayList<IAstNode>(oldTreeNode.getChildCount() + repairedNodes.size());

			Iterator<IAstNode> iterator = tryGetListIterator(oldTreeNode); 
			for (int i = 0, max = oldTreeNode.getChildCount(); i < max; i++) {
				IAstNode child = iterator == null ? oldTreeNode.getChildAt(i) : iterator.next();

				copyTokensAndTryAddRepairedNodes(oldTreeNode, children, startToken, child.getLeftToken(), child.getRightToken());

				if (!damageAnalyzer.isDamageTreeNode(child, true, skippedChars)) {
					startToken = getTokenAfter(child.getRightToken());
					children.add(buildOutputSubtree(child, offsetChange));
				}
			}
			IToken stopToken = getTokenAfter(findRightMostLayoutToken(oldTreeNode.getRightToken()));
			copyTokensAndTryAddRepairedNodes(oldTreeNode, children, startToken, stopToken, stopToken);
		} else {
			children = copyChildrenToList(oldTreeNode);
			for (int i = 0; i < children.size(); i++) {
				IAstNode child = children.get(i);
				
				int myOffsetChange = offsetChange + (isRepairedNodesInserted ? damageSizeChange : 0);
				copyTokens(startToken, findLeftMostLayoutToken(child.getLeftToken()), NO_STOP_OFFSET/*child.getLeftToken().getEndOffset() + 1*/, myOffsetChange);
				startToken = getTokenAfter(child.getRightToken());

				children.set(i, buildOutputSubtree(child, offsetChange));
			}
		}
		int myOffsetChange = offsetChange + (isRepairedNodesInserted ? damageSizeChange : 0);
		IToken stopToken = getTokenAfter(oldTreeNode.getRightToken());
		copyTokens(startToken, stopToken, NO_STOP_OFFSET/*stopToken.getEndOffset() + 1*/, myOffsetChange);
		return buildOutputNode(oldTreeNode, children, beforeStartToken);
	}

	private void sanityCheckOldTreeNode(IAstNode oldTreeNode) {
		// (Also checked in IncrementalSGLR precondition.)
		// TODO: incremental parsing with ambiguous trees
		@SuppressWarnings("unchecked")
		TNode tOldTreeNode = (TNode) oldTreeNode;
		assert factory.tryGetAmbChildren(tOldTreeNode) == null :
			"Incremental tree building with ambiguities not implemented";
	}

	@SuppressWarnings("unchecked")
	private TNode buildOutputNode(IAstNode oldTreeNode, List<IAstNode> children, IToken beforeStartToken) {
		IToken startToken;
		if (newTokenizer.currentToken() == beforeStartToken) {
			startToken = newTokenizer.makeToken(newTokenizer.getStartOffset() - 1, TK_UNKNOWN, true);
		} else {
			startToken = getTokenAfter(beforeStartToken);
		}
		return factory.recreateNode((TNode) oldTreeNode, startToken, newTokenizer.currentToken(), (List<TNode>) children);
	}

	private void copyTokensAndTryAddRepairedNodes(IAstNode oldTreeNode, List<IAstNode> children,
			IToken firstToken, IToken stopToken, IToken childEndToken) {
		
		if (!isRepairedNodesInserted && childEndToken.getEndOffset() >= damageStart) {
			//copyTokens(firstToken, stopToken, damageStart, -skippedChars);
			copyTokens(firstToken, stopToken, damageStart, 0);
			insertRepairedNodes(oldTreeNode, children);
			copyTokens(firstToken, stopToken, damageEnd + damageSizeChange - skippedChars + 1, damageSizeChange);
		} else {
			copyTokens(firstToken, stopToken, damageStart, 0);
		}
	}

	private void insertRepairedNodes(IAstNode oldTreeNode, List<IAstNode> children) {
		if (repairedNodes.size() > 0) {
			IToken firstToken = getTokenBefore(repairedNodes.get(0).getLeftToken());
	
			for (IAstNode node : repairedNodes) {
				copyTokens(firstToken, node.getLeftToken(), NO_STOP_OFFSET/*node.getLeftToken().getEndOffset() + 1*/, skippedChars);
				firstToken = getTokenAfter(node.getRightToken());
				
				TNode reconstructed = buildOutputSubtree(node, skippedChars);
				reconstructedNodes.add(reconstructed);
				children.add(reconstructed);
			}
		}
		isRepairedNodesInserted = true;
	}
	
	/**
	 * Copies tokens to {@link #newTokenizer}.
	 * 
	 * @param startToken
	 *           Start copying at this token.
	 * @param stopToken
	 *           Stop copying just before this token.
	 * @param stopOffset
	 *           Stop copying just before this offset in the original token stream.
	 * @param offsetChange
	 *           The change in offset between the given tokens and the copied tokens in the new tokenizer.
	 */
	private void copyTokens(IToken startToken, IToken stopToken, int stopOffset, int offsetChange) {
		ITokenizer fromTokenizer = startToken.getTokenizer();
		assert fromTokenizer == stopToken.getTokenizer();
		int oldStartOffset = newTokenizer.getStartOffset();
		for (int i = findLeftMostLayoutToken(startToken).getIndex(), last = stopToken.getIndex(); i < last; i++) {
			IToken fromToken = fromTokenizer.getTokenAt(i);
			int myEndOffset = min(stopOffset, fromToken.getEndOffset()) + offsetChange;
			if (myEndOffset < oldStartOffset)
				continue;
			IToken toToken = newTokenizer.makeToken(myEndOffset, fromToken.getKind(), isEssentialToken(fromToken));
			assert toToken == null || myEndOffset < fromToken.getEndOffset() + offsetChange
				|| // toToken.toString().equals(fromToken.toString())
				fromToken.toString().equals(newTokenizer.getInput().substring(
						fromToken.getStartOffset() + offsetChange, fromToken.getEndOffset() + offsetChange + 1))
				: "Expected '" + fromToken + "' in copied tokenstream, not '" + toToken + "'";
		}
	}
	
	private static boolean isEssentialToken(IToken token) {
		switch (token.getKind()) {
			case TK_ERROR: case TK_ERROR_EOF_UNEXPECTED: case TK_ERROR_KEYWORD:
				return true;
			default:
				return false;
		}
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
