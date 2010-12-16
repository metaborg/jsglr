package org.spoofax.jsglr.client;

import static java.lang.Math.max;

import java.util.Set;

import org.spoofax.jsglr.client.imploder.IAstNode;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr.client.imploder.ITreeFactory;
import org.spoofax.jsglr.client.imploder.TreeBuilder;
import org.spoofax.jsglr.shared.BadTokenException;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr.shared.TokenExpectedException;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class IncrementalSGLR<TNode extends IAstNode> {
	
	private final SGLR parser;

	private final ITreeFactory<TNode> factory;
	
	private final Set<String> incrementalSorts;

	public IncrementalSGLR(SGLR parser, ITreeBuilder builder, ITreeFactory<TNode> factory, Set<String> incrementalSorts) {
		this.parser = parser;
		this.factory = factory;
		this.incrementalSorts = incrementalSorts;
		parser.setTreeBuilder(builder);
		assert !(builder instanceof TreeBuilder)
			|| ((TreeBuilder) builder).getFactory().getClass() == factory.getClass();
	}
	
	public IAstNode parseIncremental(String input, String filename, String startSymbol, TNode oldTree)
			throws TokenExpectedException, BadTokenException, ParseException, SGLRException {
		
		String oldInput = oldTree.getLeftToken().getTokenizer().getInput();
		int damageStart = getDamageStart(input, oldInput);
		int damageEndOffset = oldInput.length() - input.length();
		int damageEnd = getDamageEnd(input, oldInput, damageEndOffset);
		if (damageEnd == 0) return oldTree;
		
		StringBuilder partialInput = new StringBuilder(input.length());
		buildPartialInput(input, partialInput, damageStart, damageEnd, damageEndOffset, oldTree, 0);
		System.out.println(partialInput);
		
		IAstNode partialTree = (IAstNode) parser.parse(partialInput.toString(), startSymbol);
		// TODO: some sanity check
		
		return buildOutputTree(partialTree, oldTree, damageStart, damageEnd);
	}

	private int buildPartialInput(String input, StringBuilder partialInput, int damageStart, 
			int damageEnd, int damageEndOffset, IAstNode oldTree, int offset) {
		
		IToken left = oldTree.getLeftToken();
		IToken right = oldTree.getRightToken();
		int startOffset = 0;
		int endOffset = 0;
		
		if (left != null && right != null) {
			startOffset = left.getStartOffset();
			endOffset = right.getEndOffset();
			if (incrementalSorts.contains(oldTree.getSort())
					&& !isRangeOverlap(damageStart, damageEnd, startOffset, endOffset)) {
				for (int i = offset; i < endOffset; i++) {
					partialInput.append(input.charAt(i) == '\n' ? '\n' : ' ');
				}
				return endOffset;
			}
		}
		for (int i = 0, max = oldTree.getChildCount(); i < max; i++) {
			IAstNode child = oldTree.getChildAt(i);
			offset = buildPartialInput(input, partialInput, damageStart, damageEnd, damageEndOffset, child, offset);
		}

		if (left != null && right != null) {
			int extraOffset = offset >= damageStart ? damageEndOffset : 0;
			partialInput.append(input, offset + extraOffset, endOffset + extraOffset); 
			return endOffset;
		} else {
			return offset;
		}
	}

	private static boolean isRangeOverlap(int start1, int end1, int start2, int end2) {
		return ((start2 >= start1 && start2 <= end1)
				|| (end2 >= start1 && end2 <= end1));
	}


	private int getDamageStart(String input, String oldInput) {
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) != oldInput.charAt(i)) return i;
		}
		return input.length() - 1;
	}

	private int getDamageEnd(String input, String oldInput, int offset) {
		int boundOffset = max(offset, 0);
		for (int i = input.length() - 1; i + boundOffset >= 0; i--) {
			if (input.charAt(i) != oldInput.charAt(i + offset)) return i;
		}
		return 0;
	}
	
	private IAstNode buildOutputTree(IAstNode partialTree, TNode oldTree, int damageStart, int damageEnd) {
		// TODO
		throw new NotImplementedException();
	}
}
