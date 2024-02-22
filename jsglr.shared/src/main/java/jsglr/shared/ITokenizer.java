package jsglr.shared;

import org.spoofax.interpreter.terms.ISimpleTerm;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public interface ITokenizer extends ITokens {

	String ERROR_SKIPPED_REGION = "Syntax error, unexpected construct(s)";
	String ERROR_UNEXPECTED_EOF = "Syntax error, unexpected end of input";
	String ERROR_WATER_PREFIX = "Syntax error, not expected here";
	String ERROR_INSERT_PREFIX = "Syntax error, expected";
	String ERROR_INSERT_END_PREFIX = "Syntax error, unterminated construct";
	String ERROR_INCOMPLETE_PREFIX = "Syntax error, incomplete construct";
	String ERROR_GENERIC_PREFIX = "Syntax error";
	String ERROR_WARNING_PREFIX = "Warning";

	int getStartOffset();

	void setStartOffset(int startOffset);

	IToken currentToken();

	int getEndLine();

	int getEndColumn();

	int getLineAtOffset(int offset);

	IToken makeToken(int endOffset, LabelInfo label, boolean allowEmptyToken);

	IToken makeToken(int endOffset, IToken.Kind kind, boolean allowEmptyToken);

	Token getTokenAt(int index);

	/**
	 * Gets a token at the given offset, or creates an adjunct
	 * token with that offset, used for error reporting.
	 */
	IToken getErrorTokenOrAdjunct(int offset);

	/**
	 * Creates artificial token at keyword boundaries
	 * inside skipped regions of code when
	 * invoked for each character in a skipped/erroneous region of code.
	 * Required for keyword highlighting with {@link org.spoofax.jsglr.client.IKeywordRecognizer}.
	 *
	 * Additionally, ensures that {@link #isSyntaxCorrect()} returns false.
	 *
	 * @param offset
	 *           The offset of the
	 */
	void tryMakeSkippedRegionToken(int endOffset);

	/**
	 * Creates an artificial token for every water-based recovery
	 * and for comments within layout.
	 */
	void tryMakeLayoutToken(int endOffset, int lastOffset, LabelInfo label);

	/**
	 * Marks a possible syntax error (if indicated by the given label),
	 * starting *after* the given token, ending at the given offset.
	 */
	void markPossibleSyntaxError(LabelInfo label, IToken firstToken, int endOffset, ProductionAttributeReader prodReader);

	boolean isSyntaxCorrect();

	void setSyntaxCorrect(boolean syntaxCorrect);

	boolean isAmbiguous();
	void setAmbiguous(boolean isAmbiguous);

	void setAst(ISimpleTerm ast);

	/**
	 * Initializes the {@link IToken#getAstNode()} method of each
	 * token in this token stream.
	 */
	void initAstNodeBinding();
}
