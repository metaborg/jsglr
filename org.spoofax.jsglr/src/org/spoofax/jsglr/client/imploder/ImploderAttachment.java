package org.spoofax.jsglr.client.imploder;

import org.spoofax.interpreter.terms.AbstractTermAttachment;
import org.spoofax.interpreter.terms.ISimpleTerm;
import org.spoofax.interpreter.terms.ITermAttachment;

/** 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class ImploderAttachment extends AbstractTermAttachment {
	
	private final IToken leftToken, rightToken;
	
	private final String sort;
	
	public ImploderAttachment(String sort, IToken leftToken, IToken rightToken) {
		this.sort = sort;
		this.leftToken = leftToken;
		this.rightToken = rightToken;
	}
	
	public Class<? extends ITermAttachment> getAttachmentType() {
		return ImploderAttachment.class;
	}
	
	public IToken getLeftToken() {
		return leftToken;
	}
	
	public IToken getRightToken() {
		return rightToken;
	}
	
	public String getSort() {
		return sort;
	}
	
	/**
	 * The element sort for lists and tuples.
	 * 
	 * @throws UnsupportedOperationException
	 *             If the node is not a list or tuple.
	 */
	public String getElementSort() {
		throw new UnsupportedOperationException();
	}

	public static IToken getLeftToken(ISimpleTerm term) {
		ImploderAttachment attachment = term.getAttachment(ImploderAttachment.class);
		if (attachment == null) {
			return null;
		} else {
			return attachment.getLeftToken();
		}
	}

	public static IToken getRightToken(ISimpleTerm term) {
		ImploderAttachment attachment = term.getAttachment(ImploderAttachment.class);
		if (attachment == null) {
			return null;
		} else {
			return attachment.getRightToken();
		}
	}

	public static String getSort(ISimpleTerm term) {
		ImploderAttachment attachment = term.getAttachment(ImploderAttachment.class);
		if (attachment == null) {
			return null;
		} else {
			return attachment.getSort();
		}
	}
	
	/**
	 * The element sort for lists and tuples.
	 * 
	 * @throws UnsupportedOperationException
	 *             If the node is not a list or tuple.
	 */
	public static String getElementSort(ISimpleTerm term) {
		ImploderAttachment attachment = term.getAttachment(ImploderAttachment.class);
		if (attachment == null) {
			return null;
		} else {
			return attachment.getElementSort();
		}
	}
	
	@Override
	public String toString() {
		if (getLeftToken() != null) {
			return "(" + sort + ",\"" + getLeftToken().getTokenizer().toString(getLeftToken(), getRightToken()) + "\")";
		} else {
			return "(" + sort + ",null)";
		}
	}
}
