package org.spoofax.jsglr.client.imploder;


/**
 * @author Lennart Kats <lennart add lclnet.nl>
 * @author Karl Trygve Kalleberg <karltk near strategoxt dot org>
 */
public class Token implements IToken {

	private final ITokenizer tokenizer;
	
	private final int index, startOffset, endOffset, line, column;
	private int kind;

	public Token(ITokenizer tokenizer, int index, int line, int column, int startOffset, int endOffset, int kind) {
		this.tokenizer = tokenizer;
		this.index = index;
		this.line = line;
		this.column = column;
		this.startOffset = startOffset;
		this.endOffset = endOffset;
		this.kind = kind;
	}
	
	public ITokenizer getTokenizer() {
		return tokenizer;
	}
	
	public int getKind() {
		return kind;
	}
	
	public void setKind(int kind) {
		this.kind = kind;
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
		return line;
	}
	
	public int getColumn() {
		return column;
	}
	
	@Override
	public String toString() {
		return tokenizer.toString(this, this);
	}

	public int compareTo(IToken arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
