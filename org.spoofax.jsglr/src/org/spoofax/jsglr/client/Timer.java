package org.spoofax.jsglr.client;

public class Timer {
	/** Last starting time since start was called. **/
	private long startTime;

	public void start() {
		startTime = time();
	}

	public long peek() {
		return time() - startTime;
	}
	
	public void reset() {
		startTime = 0;
	}

	private long time() {
		return System.currentTimeMillis();
	}
}
