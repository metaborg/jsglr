package java.util.concurrent.atomic; 



public class AtomicBoolean implements java.io.Serializable {
	private boolean b;
    public AtomicBoolean(boolean initialValue) {
    	b=initialValue;
    }
    public AtomicBoolean() {
    }
    public final boolean get() {
        return b;
    }
    public final boolean compareAndSet(boolean expect, boolean update) {
        b = update;
        return b;
    }
    
    public boolean weakCompareAndSet(boolean expect, boolean update) {
        b = update;
        return b;
    }
    public final void set(boolean newValue) {
    	b = newValue;
    }
    public final void lazySet(boolean newValue) {
    	b = newValue;
    }
    public final boolean getAndSet(boolean newValue) {
    	b = newValue;
    	return b;
    }
    public String toString() {
        return Boolean.toString(get());
    }
}
