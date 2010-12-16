package org.spoofax.jsglr.client.imploder;

import org.spoofax.jsglr.client.NotImplementedException;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class Token implements IToken {

	private final ITokenizer tokenizer;
	
	private final int index, startOffset, endOffset, kind;

	public Token(ITokenizer tokenizer, int index, int startOffset, int endOffset, int kind) {
		this.tokenizer = tokenizer;
		this.index = index;
		this.startOffset = startOffset;
		this.endOffset = endOffset;
		this.kind = kind;
	}
	
	public int getKind() {
		return kind;
	}

	public int getIndex() {
		return index;
	}

	public int getStartOffset() {
		return startOffset;
	}

	public int getEndOffset() {
		return endOffset;
	}

	public int getLine() {
		// TODO
		throw new NotImplementedException();
	}
	
	public int getColumn() {
		// TODO
		throw new NotImplementedException();
	}

}
