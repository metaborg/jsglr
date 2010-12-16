package org.spoofax.jsglr.client.incremental;

import static java.lang.Math.min;
import static org.spoofax.jsglr.client.incremental.IncrementalSGLR.DEBUG;

import java.util.Set;

import org.spoofax.jsglr.client.imploder.IAstNode;
import org.spoofax.jsglr.client.imploder.IToken;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class IncrementalInputBuilder {
	
	private static final boolean INSERT_WHITESPACE = true;

	private final StringBuilder result = new StringBuilder();
	
	private final Set<String> incrementalSorts;
	
	private final String input;
	
	private final int damageStart;
	
	@SuppressWarnings("unused")
	private final int damageEnd;

	private final int damageSizeChange;
	
	private boolean isDamagePrinted;

	/**
	 * @param incrementalSorts
	 *            Sorts that can be incrementally parsed (e.g., MethodDec, ImportDec).
	 *            *Must* be sorts that only occur in lists (such as MethodDec*).
	 */
	public IncrementalInputBuilder(Set<String> incrementalSorts, String input,
			int damageStart, int damageEnd, int damageSizeChange) {
		this.incrementalSorts = incrementalSorts;
		this.input = input;
		this.damageEnd = damageEnd;
		this.damageStart = damageStart;
		this.damageSizeChange = damageSizeChange;
	}

	public String buildPartialInput(IAstNode oldTree) throws IncrementalSGLRException {
		buildPartialInput(oldTree, 0);
		return result.toString();
	}

	private int buildPartialInput(IAstNode oldTree, int offset) throws IncrementalSGLRException {
		IToken right = oldTree.getRightToken();
		int endOffset = 0;
		
		// Print incrementalSorts nodes
		if (right != null) {
			endOffset = right.getEndOffset();
			if (!oldTree.isList() && incrementalSorts.contains(oldTree.getSort())) {
				if (endOffset >= damageStart /*isRangeOverlap(damageStart, damageEnd, startOffset, endOffset)*/) {
					printDamagedNode(offset, endOffset);
					// possible: appendWhitespace(input, startOffset, endOffset);
				} else {
					appendWhitespace(input, offset, endOffset);
				}
				return endOffset + 1;
			}
		}
		
		offset = buildPartialInputRecurse(oldTree, offset);

		// Print original text
		if (right != null) {
			return appendPartialInput(oldTree, offset, endOffset);
		} else {
			return offset;
		}
	}

	private int buildPartialInputRecurse(IAstNode oldTree, int offset) throws IncrementalSGLRException {
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
		return offset;
	}

	private void printDamagedNode(int offset, int endOffset) {
		if (!isDamagePrinted) {
			append(input, offset, endOffset + damageSizeChange + 1);
			isDamagePrinted = true;
			/*
			if (DEBUG) System.err.print('|');
			append(input, offset, damageStart);
			if (DEBUG) System.err.print('|');
			append(input, damageStart, damageEnd + 1);
			if (DEBUG) System.err.print('|');
			// TODO: don't use endOffset here but use the last token's end offset
			append(input, damageEnd + 1, endOffset + damageSizeChange + 1);
			if (DEBUG) System.err.print('|');
			*/
		}
	}

	private void appendWhitespace(String input, int offset, int endOffset) {
		if (INSERT_WHITESPACE) {
			for (int i = offset; i <= endOffset; i++) {
				// if (DEBUG) System.err.print(input.charAt(i) == '\n' ? "\n" : "-" + input.charAt(i));
				result.append(input.charAt(i) == '\n' ? '\n' : ' ');
			}
		}
	}

	private int appendPartialInput(IAstNode oldTree, int offset, int endOffset) {
		if (offset >= damageStart) {
			if (!isDamagePrinted) {
				// append(input, damageStart, damageEnd + 1);
				append(input, damageStart, damageStart + damageSizeChange + 1);
				// append(input, damageStart + damageSizeChange, damageEnd + damageSizeChange + 1);
				isDamagePrinted = true;
			}
			append(input, offset + damageSizeChange, endOffset + damageSizeChange + 1);
		} else {
			append(input, offset, min(endOffset + 1, damageStart));
		}
		return endOffset + 1;
	}
	
	private void append(String input, int offset, int endOffset) {
		if (DEBUG) System.err.print(input.substring(offset, endOffset));
		result.append(input, offset, endOffset);
 	}
}
