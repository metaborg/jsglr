package org.spoofax.jsglr.client.incremental;

import static java.lang.Math.min;
import static org.spoofax.jsglr.client.incremental.IncrementalSGLR.DEBUG;
import static org.spoofax.jsglr.client.incremental.IncrementalSGLR.isRangeOverlap;

import java.util.Set;

import org.spoofax.jsglr.client.imploder.IAstNode;
import org.spoofax.jsglr.client.imploder.IToken;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class IncrementalInputBuilder {
	
	private final StringBuilder result = new StringBuilder();
	
	private final Set<String> incrementalSorts;
	
	private final String input;
	
	private final int damageStart;
	
	private final int damageEnd;

	private final int afterDamageOffset;
	
	private boolean isDamagePrinted;

	/**
	 * @param incrementalSorts
	 *            Sorts that can be incrementally parsed (e.g., MethodDec, ImportDec).
	 *            *Must* be sorts that only occur in lists (such as MethodDec*).
	 */
	public IncrementalInputBuilder(Set<String> incrementalSorts, String input,
			int damageStart, int damageEnd, int afterDamageOffset) {
		this.incrementalSorts = incrementalSorts;
		this.input = input;
		this.damageEnd = damageEnd;
		this.damageStart = damageStart;
		this.afterDamageOffset = afterDamageOffset;
	}

	public String buildPartialInput(IAstNode oldTree) throws IncrementalSGLRException {
		buildPartialInput(oldTree, 0);
		return result.toString();
	}

	private int buildPartialInput(IAstNode oldTree, int offset) throws IncrementalSGLRException {
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
					if (!isDamagePrinted) {
						isDamagePrinted = true;
						// append(input, offset, offset + afterDamageOffset + 1);
						if (DEBUG) System.out.print('|');
						append(input, offset, damageStart);
						if (DEBUG) System.out.print('|');
						append(input, damageStart, damageEnd + 1);
						// if (DEBUG) System.out.print('|');
						// append(input, damageEnd + 1, offset + afterDamageOffset + 1);
						// so maybe: offset + afterDamageOffset + 1 - (offset + afterDamageOffset - damageEnd)
						//         = damageEnd + 1
						if (DEBUG) System.out.print('|');
						// append(input, offset + afterDamageOffset + 1, endOffset + afterDamageOffset + 1);
						append(input, damageEnd + 1, endOffset + afterDamageOffset + 1);
						if (DEBUG) System.out.print('|');
					}
					// possible: appendWhitespace(input, startOffset, endOffset);
				} else {
					appendWhitespace(input, offset, endOffset);
				}
				return endOffset + 1;
			}
		}
		
		// Recurse
		if (oldTree.isList() && oldTree instanceof Iterable) { // likely a linked list
			for (Object o : (Iterable<?>) oldTree) {
				IAstNode child = (IAstNode) o;
				offset = buildPartialInput(child, offset);
			}
		} else {
			for (int i = 0, count = oldTree.getChildCount(); i < count; i++) {
				IAstNode child = oldTree.getChildAt(i);
				offset = buildPartialInput(child, offset);
			}
		}

		// Print original text
		if (left != null && right != null) {
			return appendPartialInput(oldTree, offset, endOffset);
		} else {
			return offset;
		}
	}

	private void appendWhitespace(String input, int offset, int endOffset) {
		for (int i = offset; i <= endOffset; i++) {
			// if (DEBUG) System.out.print(input.charAt(i) == '\n' ? "\n" : "-" + input.charAt(i));
			result.append(input.charAt(i) == '\n' ? '\n' : ' ');
		}
	}

	private int appendPartialInput(IAstNode oldTree, int offset, int endOffset) {
		if (offset >= damageStart) {
			if (!isDamagePrinted) {
				append(input, damageStart, damageEnd + 1);
				isDamagePrinted = true;
			}
			append(input, offset + afterDamageOffset, endOffset + afterDamageOffset + 1);
		} else {
			append(input, offset, min(endOffset + 1, damageStart));
		}
		return endOffset + 1;
	}
	
	private void append(String input, int offset, int endOffset) {
		if (DEBUG) System.out.print(input.substring(offset, endOffset));
		result.append(input, offset, endOffset);
 	}
}
