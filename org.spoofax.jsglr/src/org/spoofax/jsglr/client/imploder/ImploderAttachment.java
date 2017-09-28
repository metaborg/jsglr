package org.spoofax.jsglr.client.imploder;

import static org.spoofax.jsglr.client.imploder.IToken.TK_UNKNOWN;
import static org.spoofax.terms.Term.asJavaInt;
import static org.spoofax.terms.Term.asJavaString;

import org.spoofax.interpreter.terms.ISimpleTerm;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.terms.TermVisitor;
import org.spoofax.terms.attachments.AbstractTermAttachment;
import org.spoofax.terms.attachments.OriginAttachment;
import org.spoofax.terms.attachments.TermAttachmentType;

/** 
 * A term attachment for an imploded term,
 * with some information from the parser.
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class ImploderAttachment extends AbstractTermAttachment {
	
	private static final long serialVersionUID = -578795745164445689L;

	public static final TermAttachmentType<ImploderAttachment> TYPE =
		new TermAttachmentType<ImploderAttachment>(ImploderAttachment.class, "ImploderAttachment", 6) {

			@Override
			protected IStrategoTerm[] toSubterms(ITermFactory f, ImploderAttachment attachment) {
				IToken left = attachment.getLeftToken();
				IToken right = attachment.getRightToken();
	
				String sortType = attachment.getSort() == null ? "" : attachment.getSort() ;
				String fileName = left.getFilename() == null ? "" : left.getFilename();
	
				return new IStrategoTerm[] {
					f.makeString(fileName),
					f.makeInt(left.getLine()),
					f.makeInt(left.getColumn()),
					f.makeInt(left.getStartOffset()),
					f.makeInt(right.getEndOffset()),
					f.makeString( sortType )
				};
			}

			@Override
			protected ImploderAttachment fromSubterms(IStrategoTerm[] subterms) {
				String fileName =  asJavaString(subterms[0]).equals("") ? null :asJavaString(subterms[0]);
				String sortType =  asJavaString(subterms[0]).equals("") ? null :asJavaString(subterms[5]);
				
				return createCompactPositionAttachment(fileName, asJavaInt(subterms[1]),
						asJavaInt(subterms[2]), asJavaInt(subterms[3]), asJavaInt(subterms[4]) , sortType);
				
			}
		
		};
	
	private final IToken leftToken, rightToken;
	
	private boolean isCompletion = false;
	private boolean isNestedCompletion = false;
	private boolean isSinglePlaceholderCompletion = false;
	private boolean isBracket = false;
	
	
	private final String sort;
	
	/**
	 * Creates a new imploder attachment.
	 * 
	 * Note that attachment instances should not be shared.
	 */
	protected ImploderAttachment(String sort, IToken leftToken, IToken rightToken, boolean isBracket, boolean isCompletion, boolean isNestedCompletion, boolean isSinglePlaceholderCompletion) {
        assert leftToken != null && rightToken != null;
		this.sort = sort;
		this.leftToken = leftToken;
		this.rightToken = rightToken;
		this.isCompletion = isCompletion;
		this.isNestedCompletion = isNestedCompletion;
		this.isSinglePlaceholderCompletion = isSinglePlaceholderCompletion;
		this.setBracket(isBracket);
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
	
	public boolean isSequenceAttachment() {
		return false;
	}
	
	/**
	 * The element sort for list terms.
	 * Same as {@link #getSort()} for non-list terms.
	 */
	public String getElementSort() {
		return getSort();
	}

	public static IToken getLeftToken(ISimpleTerm term) {
		ImploderAttachment attachment = term.getAttachment(TYPE);
		if (attachment == null) {
			assert !hasImploderOrigin(term)
				: "Likely error: called getLeftToken() on term with imploder origin, instead of the origin itself";
			return null;
		} else {
			return attachment.getLeftToken();
		}
	}

	public static IToken getRightToken(ISimpleTerm term) {
		ImploderAttachment attachment = term.getAttachment(TYPE);
		if (attachment == null) {
			assert !hasImploderOrigin(term)
				: "Likely error: called getRightToken() on term with imploder origin, instead of the origin itself";
			return null;
		} else {
			return attachment.getRightToken();
		}
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
		return token == null ? null : token.getFilename();
	}
	
	public static ITokens getTokenizer(ISimpleTerm term) {
		IToken token = getLeftToken(term);
		assert token == null || token.getTokenizer() == getRightToken(term).getTokenizer()
			: "Tokenizer of left and right token inconsistent";
		return token == null ? null : token.getTokenizer();
	}
	
	/**
	 * Determines if the current term has an imploder attachment,
	 * or if it has an origin with one.
	 */
	public static boolean hasImploderOrigin(ISimpleTerm term) {
		ISimpleTerm origin = OriginAttachment.getOrigin(term);
		if (origin != null) term = origin;
		return term.getAttachment(TYPE) != null;
	}
	
	/**
	 * Returns the current term if it has an imploder attachment,
	 * or returns the origin term if it has one.
	 */
	public static IStrategoTerm getImploderOrigin(IStrategoTerm term) {
		IStrategoTerm origin = OriginAttachment.getOrigin(term);
		if (origin != null) term = origin;
		return term.getAttachment(TYPE) != null ? term : null;
	}
	
	/**
	 * Creates a compact position information attachment for a term.
	 */
	public static ImploderAttachment getCompactPositionAttachment(IStrategoTerm term, boolean useOnlyFirstAttach) {
		if (useOnlyFirstAttach) {
			FirstAttachFetcher fetcher = new FirstAttachFetcher();
			fetcher.visit(term);
			return getCompactPositionAttachment(fetcher.result, fetcher.result);
		} else {
			FirstLastAttachFetcher fetcher = new FirstLastAttachFetcher();
			fetcher.visit(term);
			return getCompactPositionAttachment(fetcher.first, fetcher.last);
		}
	}
	
	public static ImploderAttachment getCompactPositionAttachment(
			ImploderAttachment first, ImploderAttachment last) {
		if (first == null || last == null) return null;
		
		IToken left = first.getLeftToken();
		IToken right = last.getRightToken();
		String filename = left.getFilename();
		
		return createCompactPositionAttachment(filename, left.getLine(), left.getColumn(), left.getStartOffset(), right.getEndOffset());
	}
	
	
	
	public static ImploderAttachment createCompactPositionAttachment(
			String filename, int line, int column, int startOffset, int endOffset) {
		return createCompactPositionAttachment(filename, line, column, startOffset, endOffset, null);
	}
	
	
	public static ImploderAttachment createCompactPositionAttachment(
			String filename, int line, int column, int startOffset, int endOffset, String sortType) {
		Token token = new Token(null, filename, 0, line, column, startOffset, endOffset, TK_UNKNOWN);
		NullTokenizer newTokenizer = new NullTokenizer(sortType, filename, token);
		token.setTokenizer(newTokenizer);
		return new ImploderAttachment(null, token, token, false, false, false, false);
	}
	

	/**
	 * @param isAnonymousSequence  True if the term is an unnamed sequence like a list or tuple.
	 */
	public static void putImploderAttachment(ISimpleTerm term, boolean isAnonymousSequence, String sort, IToken leftToken, IToken rightToken, boolean isBracket, boolean isCompletion, boolean isNestedCompletion, boolean isSinglePlaceholderCompletion) {
		term.putAttachment(isAnonymousSequence ?
				  new ListImploderAttachment(sort, leftToken, rightToken)
				: new ImploderAttachment(sort, leftToken, rightToken, isBracket, isCompletion, isNestedCompletion, isSinglePlaceholderCompletion));
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((leftToken == null) ? 0 : leftToken.hashCode());
		result = prime * result + ((rightToken == null) ? 0 : rightToken.hashCode());
		result = prime * result + ((sort == null) ? 0 : sort.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		ImploderAttachment other = (ImploderAttachment) obj;
		if(leftToken == null) {
			if(other.leftToken != null)
				return false;
		} else if(!leftToken.equals(other.leftToken))
			return false;
		if(rightToken == null) {
			if(other.rightToken != null)
				return false;
		} else if(!rightToken.equals(other.rightToken))
			return false;
		if(sort == null) {
			if(other.sort != null)
				return false;
		} else if(!sort.equals(other.sort))
			return false;
		return true;
	}

	@Override
	public String toString() {
		if (getLeftToken() != null) {
			return "(" + getSort() + ",\"" + getLeftToken().getTokenizer().toString(getLeftToken(), getRightToken()) + "\")";
		} else {
			return "(" + getSort() + ",null)";
		}
	}

    public boolean isCompletion() {
        return isCompletion;
    }

    public void setCompletion(boolean isCompletion) {
        this.isCompletion = isCompletion;
    }

    public boolean isNestedCompletion() {
        return isNestedCompletion;
    }

    public void setNestedCompletion(boolean isNestedCompletion) {
        this.isNestedCompletion = isNestedCompletion;
    }
    
    

    public boolean isSinglePlaceholderCompletion() {
        return isSinglePlaceholderCompletion;
    }

    public void setSinglePlaceholderCompletion(boolean isSinglePlaceholderCompletion) {
        this.isSinglePlaceholderCompletion = isSinglePlaceholderCompletion;
    }

    public boolean isBracket() {
        return isBracket;
    }

    public void setBracket(boolean isBracket) {
        this.isBracket = isBracket;
    }

    /**
	 * An inner class that fetches the first imploder atachment
	 * in a tree.
	 * 
	 * @author Lennart Kats <lennart add lclnet.nl>
	 */
	static class FirstAttachFetcher extends TermVisitor {
		ImploderAttachment result;
		public void preVisit(IStrategoTerm term) {
			term = OriginAttachment.tryGetOrigin(term);
			ImploderAttachment attach = ImploderAttachment.get(term);
			result = attach;
		}
		@Override
		public boolean isDone(IStrategoTerm term) {
			return result != null;
		}
	}
	
	/**
	 * An inner class that fetches the first and last imploder atachment
	 * in a tree.
	 * 
	 * @author Lennart Kats <lennart add lclnet.nl>
	 */
	static class FirstLastAttachFetcher extends TermVisitor {
		ImploderAttachment first, last;
		public void preVisit(IStrategoTerm term) {
			term = OriginAttachment.tryGetOrigin(term);
			ImploderAttachment attach = term.getAttachment(TYPE);
			if (attach == null) return;
			if (first == null) first = attach;
			last = attach;
		}
	}
}
