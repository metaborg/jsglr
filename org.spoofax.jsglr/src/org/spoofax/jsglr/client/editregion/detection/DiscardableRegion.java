package org.spoofax.jsglr.client.editregion.detection;

import org.spoofax.interpreter.terms.IStrategoTerm;

/**
 * Fragment in the source code that represents a (possibly broken) construct
 * that (presumably) can be discarded safely, e.g., without invalidating the parse input.
 * @author maartje
 */
public class DiscardableRegion{
	private final int startOffset;
	private final int endOffset;
	private final IStrategoTerm affectedTerm;
	

	/**
	 * Fragment in the source code that represents a (possibly broken) construct
	 * that can be discarded safely, e.g., without invalidating the parse input.
	 */
	public DiscardableRegion(int startOffset, int endOffset, IStrategoTerm affectedTerm){
		this.startOffset = startOffset;
		this.endOffset = endOffset;
		this.affectedTerm = affectedTerm;
		assert startOffset <= endOffset;
	}

	/**
	 * Returns the start offset of the discardable code fragment
	 * @return start offset
	 */
	public int getStartOffset() {
		return startOffset;
	}

	/**
	 * Returns the end offset of the discardable code fragment
	 * @return end offset
	 */
	public int getEndOffset() {
		return endOffset;
	}

	/**
	 * Returns the term associated to the discardable code fragment.
	 * Can be null in case the discarded region is a comment, or in case the abstract representation is not known.
	 * @return term
	 */
	public IStrategoTerm getAffectedTerm() {
		assert(!affectedTerm.isList()): "Individual list elements are prefered over full lists";
		return affectedTerm;
	}
}
