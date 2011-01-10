package org.spoofax.jsglr.client.imploder;

import static org.spoofax.jsglr.client.imploder.IToken.TK_ERROR;

/** 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public abstract class AbstractTokenizer implements ITokenizer {
	
	private final TokenKindManager manager =
		new TokenKindManager();
	
	private boolean isAmbiguous;

	public IToken makeToken(int endOffset, LabelInfo label, boolean allowEmptyToken) {
		return makeToken(endOffset, manager.getTokenKind(label), allowEmptyToken);
	}

	public boolean isAmbigous() {
		return isAmbiguous;
	}

	public void setAmbiguous(boolean isAmbiguous) {
		this.isAmbiguous = isAmbiguous;
	}

	public void tryMarkSyntaxError(LabelInfo label, IToken firstToken, int endOffset, ProductionAttributeReader prodReader) {
		if (label.isRecover() || label.isReject() || label.getDeprecationMessage() != null) {
			if (firstToken == currentToken())
				firstToken = makeToken(endOffset, TK_ERROR, false);
			IToken lastToken = currentToken();
			String tokenText = toString(firstToken, lastToken);
			if (tokenText.length() > 40)
				tokenText = tokenText.substring(0, 40) + "...";
			
			if (label.isReject() || prodReader.isWaterConstructor(label.getConstructor())) {
				setErrorMessage(firstToken, lastToken, ERROR_WATER_PREFIX
						+ ": '" + tokenText + "'");
			} else if (prodReader.isInsertEndConstructor(label.getConstructor())) {
				setErrorMessage(firstToken, lastToken, ERROR_INSERT_END_PREFIX
						+ ": '" + tokenText + "'");
			} else if (prodReader.isInsertConstructor(label.getConstructor())) {
				firstToken = Tokenizer.findLeftMostLayoutToken(firstToken);
				setErrorMessage(firstToken, lastToken, ERROR_INSERT_PREFIX
						+ ": " + prodReader.getSyntaxErrorExpectedInsertion(label.getRHS()));
			} else if (label.getDeprecationMessage() != null) {
				setErrorMessage(firstToken, lastToken, ERROR_WARNING_PREFIX
						+ ": '" + label.getDeprecationMessage());
			} else if (!prodReader.isIgnoredUnspecifiedRecoverySort(label.getSort())) {
				setErrorMessage(firstToken, lastToken, ERROR_GENERIC_PREFIX
						+ ": '" + tokenText + "'");
			}
		}
	}

}
