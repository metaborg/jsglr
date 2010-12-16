package org.spoofax.jsglr.client;

import java.util.ArrayList;
import java.util.List;

import org.spoofax.jsglr.shared.terms.AFun;
import org.spoofax.jsglr.shared.terms.ATerm;
import org.spoofax.jsglr.shared.terms.ATermAppl;
import org.spoofax.jsglr.shared.terms.ATermFactory;
import org.spoofax.jsglr.shared.terms.ATermInt;
import org.spoofax.jsglr.shared.terms.ATermList;
import org.spoofax.jsglr.shared.terms.ATermString;
import org.spoofax.jsglr.shared.terms.ATermTuple;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class ATermImplodedTreeFactory implements IImplodedTreeFactory<ATerm> {
	
	private final ATermFactory factory;
	
	public ATermImplodedTreeFactory() {
		this(new ATermFactory());
	}
	
	public ATermImplodedTreeFactory(ATermFactory factory) {
		this.factory = factory;
	}
	
	public AFun createConstructor(String name, int childCount) {
		return factory.makeAFun(name, childCount, false);
	}

	public ATermAppl createNonTerminal(String sort, String constructor,
			IToken leftToken, IToken rightToken, List<ATerm> children) {
		
		// TODO: Optimize - cache afuns? hard to do up front, messy to do now in the LabelInfo objects 
		AFun afun = factory.makeAFun(constructor, children.size(), false);
		ATermAppl result = factory.makeAppl(afun, toArray(children));
		result.internalSetTokens(leftToken, rightToken);
		return result;
	}

	public ATermInt createIntTerminal(String sort, IToken token, int value) {
		ATermInt result = factory.makeInt(value);
		result.internalSetTokens(token, token);
		return result;
	}

	public ATerm createRealTerminal(String sort, IToken token, double value) {
		throw new UnsupportedOperationException("ATermReal not supported");
	}

	public ATermString createStringTerminal(String sort, String value, IToken token) {
		ATermString result = factory.makeString(value);
		result.internalSetTokens(token, token);
		return result;
	}

	public ATermTuple createTuple(String elementSort, IToken leftToken,
			IToken rightToken, List<ATerm> children) {
		
		ATermTuple result = factory.makeTuple(toArray(children));
		result.internalSetTokens(leftToken, rightToken);
		return result;
	}

	public ATermAppl createAmb(List<ATerm> alternatives) {
		IToken leftToken = alternatives.get(0).getLeftToken();
		IToken rightToken = alternatives.get(alternatives.size() - 1).getRightToken();
		return createNonTerminal(null, "amb", leftToken, rightToken, alternatives);
	}

	public ATermList createList(String elementSort, IToken leftToken,
			IToken rightToken, List<ATerm> children) {
		
		ATermList result = factory.makeList(toArray(children));
		result.internalSetTokens(leftToken, rightToken);
		return result;
	}

	public boolean isStringTerminal(ATerm node) {
		return node.getType() == ATerm.STRING;
	}

	public ATerm createInjection(String sort, List<ATerm> children) {
		return children.get(0);
	}

	public Iterable<ATerm> getChildren(ATerm node) {
		if (node instanceof Iterable<?>) {
			@SuppressWarnings("unchecked")
			Iterable<ATerm> result = (Iterable<ATerm>) node;
			return result;
		} else {
			ArrayList<ATerm> children = new ArrayList<ATerm>(node.getChildCount());
			for (int i = 0, max = node.getChildCount(); i < max; i++) {
				children.add(node.getChildAt(i));
			}
			return children;
		}
	}

	private static ATerm[] toArray(List<ATerm> children) {
		return children.toArray(new ATerm[children.size()]);
	}
}
