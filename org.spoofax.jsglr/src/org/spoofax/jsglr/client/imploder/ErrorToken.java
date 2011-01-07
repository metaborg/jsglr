package org.spoofax.jsglr.client.imploder;

/** 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class ErrorToken extends Token {
	
	private final String error;

	public ErrorToken(ITokenizer tokenizer, int index, int line, int column, int startOffset, int endOffset, int kind, String error) {
		super(tokenizer, index, line, column, startOffset, endOffset, kind);
		this.error = error;
	}

	@Override
	public String getError() {
		return error;
	}

}
