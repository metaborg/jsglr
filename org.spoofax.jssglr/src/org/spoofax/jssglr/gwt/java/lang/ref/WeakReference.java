package java.lang.ref;


//HACKY class to make Weakreferences compilable under GWT.
public class WeakReference<T extends java.lang.Object>
{
	private T obj;
	public WeakReference(T param) {
		obj = param;
	}
	public T get()
	{
		return obj;
	}
	

}
