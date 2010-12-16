package org.spoofax.jsglr.client;


public class PathPool {

	int rememberIndex;
	int allocIndex;
	Path[] pool;
	int usage;
	
	public static int maxRemembered;
	public static int maxAllocated;
	
	public PathPool(int capacity) {
		allocIndex = 1;
		rememberIndex = 0;
		usage = 0;
		pool = new Path[capacity];
	}
	
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
			throw new IllegalStateException("Must always end() the PathPool after use");
		maxRemembered = Math.max(maxRemembered, rememberIndex);
		maxAllocated = Math.max(maxAllocated, allocIndex);
	}
	
	public static int poolMisses = 0;
	
	private static PathPool level2 = null;
	
	public PathPool start() {
		if(usage == 0) {
			usage++;
			rememberIndex = 0;
			allocIndex = 1;
			return this;
		} else if (level2 != null && level2.usage == 0) {
			return level2.start();
		} else {
			// FIXME (KTK) count misses
			poolMisses++;
			
			PathPool n = new PathPool(pool.length);
			level2 = n;
			return n.start();
		}
	}
}
