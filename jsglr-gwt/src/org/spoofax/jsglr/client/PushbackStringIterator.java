package org.spoofax.jsglr.client;

public class PushbackStringIterator {

	private final String data;
	private int position;

	public PushbackStringIterator(String data) {
		this.data = data;
		position = 0;
	}
	
	public int read() {
		if(position >= data.length())
			return -1;
		return data.charAt(position++);
	}
	
	public void unread(int c) {
		position--;
	}

	public int getOffset() {
		return position;
	}
}
