package org.spoofax.jsglr.client;


public class PooledPathList {

	int rememberIndex;
	int allocIndex;
	Path[] pool;
	int usage;
	
	public static int maxRemembered;
	public static int maxAllocated;
	
	public PooledPathList(int capacity, boolean deepInit) {
		allocIndex = 1;
		rememberIndex = 0;
		usage = 0;
		pool = new Path[capacity];
		for(int i = 0; i < capacity; i++) {
			pool[i] = new Path();
		}
	}
	
	/**
	 * Store a path for later retrieval.
	 * 
	 * @see #
	 * 
	 */
	public Path rememberPath(Path parent, Link link, Frame frame, int length, int parentCount) {
		Path p;
		if(pool[rememberIndex] == null) {
			p = new Path();
			pool[rememberIndex] = p;
		} else {
			p = pool[rememberIndex];
		}
		rememberIndex++;
		return p.reuse(parent, link, frame, length, parentCount);
    }
	
	public Path makePath(Path parent, Link link, Frame frame, int length, int parentCount) {
		Path p;
		int index = pool.length - allocIndex; 
		if(pool[index] == null) {
			p = new Path();
			pool[index] = p;
		} else {
			p = pool[index];
		}
		allocIndex++;
		return p.reuse(parent, link, frame, length, parentCount);
    }

	public int size() {
		return rememberIndex;
	}
	
	public Path get(int index) {
		return pool[index];
	}

	public void end() {
		usage--;
		if(usage != 0)
			throw new IllegalStateException("Must always end() the PooledPathList after use");
		maxRemembered = Math.max(maxRemembered, rememberIndex);
		maxAllocated = Math.max(maxAllocated, allocIndex);
	}
	
	
	public PooledPathList start() {
		if(usage == 0) {
			usage++;
			rememberIndex = 0;
			allocIndex = 1;
			return this;
		}
		// FIXME: return unpooled pathlist?
		throw new IllegalStateException("PooledPathList may not be used recursively -- TODO: if this happens, and it shouldn't, just return an unpooled pathlist?");
	}
	
	public static void resetPerformanceCounters() {
		maxRemembered = 0;
		maxAllocated = 0;
	}

	public void reset() {
		usage = 0;
	}
}
