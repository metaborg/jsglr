package mb.jsglr.shared;

import java.util.Arrays;
import java.util.Iterator;

import org.spoofax.interpreter.terms.ISimpleTerm;

/**
 * A special tokenizer that has only one token.
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 * @author Karl Trygve Kalleberg <karltk near strategoxt dot org>
 */
public class NullTokenizer extends AbstractTokenizer {

	private static final long serialVersionUID = -6653567639280036480L;

    private final Token onlyToken;

	public NullTokenizer(String input, String filename, Token onlyToken) {
		super(input, filename);
		this.onlyToken = onlyToken;
		assert onlyToken.getTokenizer() == null || onlyToken.getTokenizer() == this;
		onlyToken.setTokenizer(this);
	}

	public NullTokenizer(String input, String filename) {
		super(input, filename);
		onlyToken = new Token(this, filename, 0, 0, 0, 0,
				input == null ? 0 : input.length() - 1, IToken.Kind.TK_UNKNOWN);
	}

	public int getStartOffset() {
		return 0;
	}

	public void setStartOffset(int startOffset) {
		// Do nothing
	}

	public IToken currentToken() {
		return onlyToken;
	}

	public int getTokenCount() {
		return 1;
	}

	public Token getTokenAt(int i) {
		return onlyToken;
	}

	public IToken getTokenAtOffset(int o) {
		return onlyToken;
	}

	@Override
	public IToken makeToken(int endOffset, LabelInfo label, boolean allowEmptyToken) {
		return onlyToken;
	}

	public IToken makeToken(int endOffset, IToken.Kind kind, boolean allowEmptyToken) {
		return onlyToken;
	}

	@Override
	protected void setErrorMessage(IToken leftToken, IToken rightToken, String message) {
		if (leftToken != onlyToken || rightToken != onlyToken)
			throw new IllegalArgumentException("Argument tokens do not belong to this NullTokenizer");
	}

	public void tryMakeSkippedRegionToken(int endOffset) {
		setSyntaxCorrect(false);
	}

	@Override
	public void tryMakeLayoutToken(int endOffset, int lastOffset, LabelInfo label) {
		// Do nothing
	}

	@Override
	public void markPossibleSyntaxError(LabelInfo label, IToken firstToken,
			int endOffset, ProductionAttributeReader prodReader) {

		if (label.isRecover() || label.isReject() || label.isCompletion()) {
			setSyntaxCorrect(false);
		}
	}

	@Override public Iterator<IToken> iterator() {
		return allTokens().iterator();
	}

	@Override public Iterable<IToken> allTokens() {
		return Arrays.asList(onlyToken);
	}

	public void setAst(ISimpleTerm ast) {
		// no tokens, no ast-token binding
	}

	public void initAstNodeBinding() {
		// no tokens, no ast-token binding
	}
}
