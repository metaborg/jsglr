package org.spoofax.jsglr.client;

import static org.spoofax.jsglr.shared.Tools.termAt;

import java.util.HashSet;
import java.util.Set;

import org.spoofax.jsglr.shared.terms.AFun;
import org.spoofax.jsglr.shared.terms.ATerm;
import org.spoofax.jsglr.shared.terms.ATermAppl;

/**
 * Recognizes keywords in a language without considering their context.
 * 
 * @see ParseTable#getKeywordRecognizer()
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class KeywordRecognizer {
	
	private final AFun litFun;
	
	private final Set<String> keywords = new HashSet<String>();
	
	protected KeywordRecognizer(ParseTable table) {
		litFun = table.getFactory().makeAFun("lit", 1, false);
		if (table != null) {
			for (Label l : table.getLabels()) {
				if (l != null) {
					ATerm rhs = termAt(l.getProduction(), 1);
					if (rhs instanceof ATermAppl && ((ATermAppl) rhs).getAFun() == litFun) {
						ATermAppl lit = termAt(rhs, 0);
						String litString = lit.getName();
						if (isPotentialKeyword(litString))
							keywords.add(litString);
					}
				}
			}
		}
	}
	
	public boolean isKeyword(String literal) {
		return keywords.contains(literal);
	}
	
	/**
	 * Determines whether the given string could possibly 
	 * be a keyword (as opposed to an operator).
	 * 
	 * @see #isKeyword(String)
	 */
	public static boolean isPotentialKeyword(String literal) {
		for (int i = 0, end = literal.length(); i < end; i++) {
			char c = literal.charAt(i);
			if (!isPotentialKeywordChar(c))
				return false;
		}
		return true;
	}
	
	/**
	 * Determines whether the given character could possibly 
	 * be part of a keyword (as opposed to an operator).
	 */
	public static boolean isPotentialKeywordChar(char c) {
		return Character.isLetterOrDigit(c) || c == '_';
	}
}
