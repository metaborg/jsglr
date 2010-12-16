package org.spoofax.jsglr.client.incremental;

import static java.lang.Math.max;
import static org.spoofax.jsglr.client.incremental.IncrementalSGLR.DEBUG;

/**
 * A helper class that expands the damage region
 * for incremental parsing if block or line comments
 * are involved.
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class CommentDamageHandler {
	
	public static final CommentDamageHandler C_STYLE = new CommentDamageHandler("/*", "*/");
	
	public static final CommentDamageHandler ONLY_LINE_COMMENTS = new CommentDamageHandler(null, null);

	private final String commentStart;
	
	private final String commentEnd;

	public CommentDamageHandler(String commentStart, String commentEnd) {
		this.commentStart = commentStart;
		this.commentEnd = commentEnd;
	}
	
	public int getExpandedDamageRegionEnd(String input, int damageStart, int damageEnd) {
		// Move back to '/' if user just entered '*'
		damageStart = max(0, damageStart - commentStart.length() + 1);
		// Move to following newline, in case line comments are involved
		damageEnd = max(damageEnd, input.indexOf('\n', damageEnd));
		
		if (commentStart == null) return damageEnd; 
		
		int commentStartOffset = input.lastIndexOf(commentStart, damageEnd);
		if (commentStartOffset >= damageStart) {
			int result = input.indexOf(commentEnd, commentStartOffset + commentStart.length() + 1);
			if (result == -1) {
				if (DEBUG) System.out.println("Unterminated comment ignored"); // try to recover locally
				return damageEnd;
			} else {
				return result;
			}
		} else {
			return damageEnd;
		}
			
	}
}
