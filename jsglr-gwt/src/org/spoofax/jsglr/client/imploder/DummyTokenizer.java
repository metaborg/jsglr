package org.spoofax.jsglr.client.imploder;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 * @author Karl Trygve Kalleberg <karltk near strategoxt dot org>
 */
public class DummyTokenizer implements ITokenizer {
	
	private final IToken onlyToken = new Token(this, 0, 0, 0, 0, 0, IToken.TK_UNKNOWN);

	private final String filename;
	
	private final String input;
	
	public DummyTokenizer(String filename, String input) {
		this.filename = filename;
		this.input = input;
	}
	
	public String getFilename() {
		return filename;
	}

	public String getInput() {
		return input;
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
		return 0;
	}

	public IToken getTokenAt(int i) {
		return onlyToken;
	}

	public IToken makeToken(int endOffset, LabelInfo label) {
		return onlyToken;
	}

	public IToken makeToken(int endOffset, LabelInfo label, boolean allowEmptyToken) {
		return onlyToken;
	}

	public IToken makeToken(int endOffset, int kind, boolean allowEmptyToken) {
		return onlyToken;
	}

	public void makeErrorToken(int endOffset) {
		// Do nothing
	}

	public void makeLayoutToken(int endOffset, int lastOffset, LabelInfo label) {
		// Do nothing
	}
	
	public String toString(IToken left, IToken right) {
		return "";
	}

	public Iterator<IToken> iterator() {
		ArrayList<IToken> result = new ArrayList<IToken>(1);
		result.add(onlyToken);
		return result.iterator();
	}
}
