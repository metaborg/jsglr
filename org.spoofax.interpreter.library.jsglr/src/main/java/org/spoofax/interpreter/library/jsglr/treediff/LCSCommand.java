package org.spoofax.interpreter.library.jsglr.treediff;

public interface LCSCommand<T> {
	/**
	 * Returns true in case t1 and t2 can be matched.
	 * This function implements the criteria for matching leaf nodes in a Longest Common Subsequence procedure. 
	 * @param t1 Leaf node in AST_1
	 * @param t2 Leaf node in AST_2
	 * @return True in case t1 and t2 can be matched
	 */
	abstract boolean isMatch(T t1, T t2);

}
