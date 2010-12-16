package org.spoofax.jsglr.client.imploder;

import static org.spoofax.jsglr.shared.terms.ATerm.APPL;
import static org.spoofax.jsglr.shared.terms.ATerm.INT;
import static org.spoofax.jsglr.shared.terms.ATerm.LIST;
import static org.spoofax.jsglr.shared.terms.ATerm.STRING;
import static org.spoofax.jsglr.shared.terms.ATerm.TUPLE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.spoofax.jsglr.client.NotImplementedException;
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
public class ATermTreeFactory implements ITreeFactory<ATerm> {
	
	private final ATermFactory factory;
	
	public ATermTreeFactory() {
		this(new ATermFactory());
	}
	
	public ATermTreeFactory(ATermFactory factory) {
		this.factory = factory;
	}
	
	public ATermFactory getTermFactory() {
		return factory;
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
		result.internalSetSort(sort);
		return result;
	}

	public ATermInt createIntTerminal(String sort, IToken token, int value) {
		ATermInt result = factory.makeInt(value);
		result.internalSetTokens(token, token);
		result.internalSetSort(sort);
		return result;
	}

	public ATerm createRealTerminal(String sort, IToken token, double value) {
		throw new UnsupportedOperationException("ATermReal not supported");
	}

	public ATermString createStringTerminal(String sort, String value, IToken token) {
		ATermString result = factory.makeString(value);
		result.internalSetTokens(token, token);
		result.internalSetSort(sort);
		return result;
	}

	public ATermTuple createTuple(String elementSort, IToken leftToken,
			IToken rightToken, List<ATerm> children) {
		
		ATermTuple result = factory.makeTuple(toArray(children));
		result.internalSetTokens(leftToken, rightToken);
		result.internalSetSort(elementSort);
		return result;
	}

	public ATermAppl createAmb(List<ATerm> alternatives) {
		IToken leftToken = null; 
		IToken rightToken = null;
		if (alternatives.size() > 0) {
			leftToken = alternatives.get(0).getLeftToken();
			rightToken = alternatives.get(alternatives.size() - 1).getRightToken();
		}
		
		List<ATerm> alternativesInList = new ArrayList<ATerm>();
		alternativesInList.add(createList(null, leftToken, rightToken, alternatives));
		
		return createNonTerminal(null, "amb", leftToken, rightToken, alternativesInList);
	}

	public ATermList createList(String elementSort, IToken leftToken,
			IToken rightToken, List<ATerm> children) {
		
		ATermList result = factory.makeList(toArray(children));
		result.internalSetTokens(leftToken, rightToken);
		result.internalSetSort(elementSort);
		return result;
	}
	
	public ATerm recreateNode(ATerm oldNode, IToken leftToken, IToken rightToken, List<ATerm> children) {
		switch (oldNode.getType()) {
			case INT:
				return createIntTerminal(null, leftToken, ((ATermInt) oldNode).getInt());
			case APPL:
				return createNonTerminal(null, ((ATermAppl) oldNode).getName(), leftToken, rightToken, children);
			case LIST:
				return createList(null, leftToken, rightToken, children);
			case STRING:
				return createStringTerminal(null, ((ATermString) oldNode).getString(), leftToken);
			case TUPLE:
			default:
				throw new NotImplementedException("Recreating term of type " + oldNode.getType() + ": " + oldNode); 
		}
	}

	public String tryGetStringValue(ATerm node) {
		return node.getType() == ATerm.STRING ? ((ATermString) node).getString() : null;
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
			if (node.getChildCount() == 0)
				return Collections.emptyList();
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

	public IToken getLeftToken(ATerm node) {
		return node.getLeftToken();
	}

	public IToken getRightToken(ATerm node) {
		return node.getRightToken();
	}
	
	public Iterable<ATerm> tryGetAmbChildren(ATerm node) {
		if (node.getType() == APPL && "amb".equals(((ATermAppl) node).getName())) {
			return (ATermList) node.getChildAt(0);
		} else {
			return null;
		}
	}
}
