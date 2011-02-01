package org.spoofax.jsglr.client;


public class PathListPool {

	private PooledPathList p0 = new PooledPathList(512, false);
	private PooledPathList p1 = new PooledPathList(512, false);
	private PooledPathList p2 = new PooledPathList(512, false);
	private PooledPathList p3 = new PooledPathList(512, false);
	
	public static int cacheMisses = 0;
	
	public PooledPathList create() {
		if(p0.usage == 0)
			return p0.start();
		if(p1.usage == 0)
			return p1.start();
		if(p2.usage == 0)
			return p2.start();
		if(p3.usage == 0)
			return p3.start();
	
		cacheMisses++;
		p3 = p2;
		p2 = p1;
		p1 = p0;
		p0 = new PooledPathList(512, false);
		return p0.start();
	}
	
	public static void resetPerformanceCounters() {
		cacheMisses = 0;
	}

	public void reset() {
		p0.reset();
		p1.reset();
		p2.reset();
		p3.reset();
	}
}
