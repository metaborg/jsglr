package org.spoofax.jsglr.client.imploder;

import org.spoofax.interpreter.terms.ISimpleTerm;
import org.spoofax.terms.attachments.AbstractTermAttachment;
import org.spoofax.terms.attachments.TermAttachmentType;

/** 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class ImploderAttachment extends AbstractTermAttachment {
	
	public static final TermAttachmentType<ImploderAttachment> TYPE =
		TermAttachmentType.create(ImploderAttachment.class);
	
	private final IToken leftToken, rightToken;
	
	private final String sort;
	
	public ImploderAttachment(String sort, IToken leftToken, IToken rightToken) {
		this.sort = sort;
		this.leftToken = leftToken;
		this.rightToken = rightToken;
	}
	
	public TermAttachmentType<ImploderAttachment> getAttachmentType() {
		return TYPE;
	}
	
	public static ImploderAttachment get(ISimpleTerm term) {
		return term.getAttachment(TYPE);
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
	 * The element sort for list terms.
	 * 
	 * @throws UnsupportedOperationException
	 *             If the node is not a list.
	 */
	public String getElementSort() {
		throw new UnsupportedOperationException();
	}

	public static IToken getLeftToken(ISimpleTerm term) {
		ImploderAttachment attachment = term.getAttachment(TYPE);
		return attachment == null ? null : attachment.getLeftToken();
	}

	public static IToken getRightToken(ISimpleTerm term) {
		ImploderAttachment attachment = term.getAttachment(TYPE);
		return attachment == null ? null : attachment.getRightToken();
	}

	public static String getSort(ISimpleTerm term) {
		ImploderAttachment attachment = term.getAttachment(TYPE);
		return attachment == null ? null : attachment.getSort();
	}
	
	/**
	 * The element sort for lists and tuples.
	 * 
	 * @throws UnsupportedOperationException
	 *             If the node is not a list or tuple.
	 */
	public static String getElementSort(ISimpleTerm term) {
		ImploderAttachment attachment = term.getAttachment(TYPE);
		return attachment == null ? null : attachment.getElementSort();
	}
	
	public static String getFilename(ISimpleTerm term) {
		IToken token = getLeftToken(term);
		return token == null ? null : token.getTokenizer().getFilename();
	}
	
	public static ITokenizer getTokenizer(ISimpleTerm term) {
		IToken token = getLeftToken(term);
		return token == null ? null : token.getTokenizer();
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
