/**
 * @author Richard Vogelij
 * The CopyOnWriteArraySet<T> does not work in GWT.
 * Therefore this replacement class which implements the same methods.
 */

package java.util.concurrent;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CopyOnWriteArraySet<E> implements Set<E> {
	
	private final Set<E> al;

	public CopyOnWriteArraySet() {
		al = new HashSet<E>();
	}

	public CopyOnWriteArraySet(Collection<? extends E> c) {
		al = new HashSet<E>();
		al.addAll(c);
	}

	public int size() {
		return al.size();
	}

	public boolean isEmpty() {
		return al.isEmpty();
	}

	public boolean contains(Object o) {
		return al.contains(o);
	}
	public Object[] toArray() {
		return al.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return al.toArray(a);
	}

	public void clear() {
		al.clear();
	}

	public boolean remove(Object o) {
		return al.remove(o);
	}

	public boolean add(E e) {
		return al.add(e);
	}

	public boolean containsAll(Collection<?> c) {
		return al.containsAll(c);
	}

	
	public boolean addAll(Collection<? extends E> c) {
		return al.addAll(c);
	}

	
	public boolean removeAll(Collection<?> c) {
		return al.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return al.retainAll(c);
	}

	public Iterator<E> iterator() {
		return al.iterator();
	}
	/*
	@Override
	public boolean equals(Object o) {
		
		if (o == this)
			return true;
		if (!(o instanceof Set))
			return false;
		Set<?> set = (Set<?>) (o);
		Iterator<?> it = set.iterator();

		// Uses O(n^2) algorithm that is only appropriate
		// for small sets, which CopyOnWriteArraySets should be.

		// Use a single snapshot of underlying array
		Object[] elements = al.toArray();
		int len = elements.length;
		// Mark matched elements to avoid re-checking
		boolean[] matched = new boolean[len];
		int k = 0;
		outer: while (it.hasNext()) {
			if (++k > len)
				return false;
			Object x = it.next();
			for (int i = 0; i < len; ++i) {
				if (!matched[i] && eq(x, elements[i])) {
					matched[i] = true;
					continue outer;
				}
			}
			return false;
		}
		return k == len;

	}
		*/
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	private static boolean eq(Object o1, Object o2) {
		return (o1 == null ? o2 == null : o1.equals(o2));
	}
}
