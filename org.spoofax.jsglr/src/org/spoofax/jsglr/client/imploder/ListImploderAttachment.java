package org.spoofax.jsglr.client.imploder;

/** 
 * An imploder attachment for a list node,
 * which has an additional {@link #getElementSort()} operation.
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class ListImploderAttachment extends ImploderAttachment {
	
	private static final long serialVersionUID = -4439626940512753484L;

	/**
	 * Creates a new imploder attachment.
	 * 
	 * Note that attachment instances should not be shared.
	 */
	protected ListImploderAttachment(String sort, IToken leftToken, IToken rightToken) {
		super(sort, leftToken, rightToken);
	}

	@Override
	public String getSort() {
		return super.getSort() + "*";
	}
	
	@Override
	public String getElementSort() {
		return super.getSort();
	}
}
