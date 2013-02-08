package java.lang;

public class Runtime
{
	static Runtime r = new Runtime();
	public static Runtime getRuntime()
	{
		return r;
	}
	
	public long totalMemory()
	{
		return (long)totalMemory_();
	}
	
	public long freeMemory()
	{
		return (long)freeMemory_();
	}
	
	public native double totalMemory_() /*-{
		return process.memoryUsage().heapTotal;
	}-*/;
	

	public native double freeMemory_() /*-{
		return (process.memoryUsage().heapTotal - process.memoryUsage().heapUsed);
	}-*/;
	
}