package org.spoofax.jsglr.client;

import java.util.ArrayList;
import java.util.List;

import org.spoofax.jsglr.shared.terms.ATerm;
import org.spoofax.jsglr.shared.terms.ATermAppl;
import org.spoofax.jsglr.shared.terms.ATermFactory;
import org.spoofax.jsglr.shared.terms.ATermInt;
import org.spoofax.jsglr.shared.terms.ATermList;
import org.spoofax.jsglr.shared.terms.ATermPlaceholder;

import static org.spoofax.jsglr.shared.Tools.*;


/**
 * Implodes {ast} annotations in asfix trees.
 * 
 * Note that this class assigns a null sort to all children
 * of the constructed TNode.
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class AstAnnoImploder<TNode> {

	private final IImplodedTreeFactory<TNode> factory;
	
	private final List<TNode> placeholderValues;
	
	private final IToken leftToken, rightToken;
	
	private final ATermFactory termFactory;
	
	public AstAnnoImploder(IImplodedTreeFactory<TNode> factory, ATermFactory termFactory, List<TNode> placeholderValues, IToken leftToken, IToken rightToken) {
		this.factory = factory;
		this.termFactory = termFactory;
		this.placeholderValues = placeholderValues;
		this.leftToken = leftToken;
		this.rightToken = rightToken;
	}
	
	public TNode implode(ATerm ast, String sort) {
		// Placeholder terms are represented as strings; must parse them and fill in their arguments
		String astString = ast.toString();
		if (astString.startsWith("\"") && astString.endsWith("\"")) {
			astString = astString.substring(1, astString.length() - 1);
			astString = astString.replace("\\\\", "\\").replace("\\\"", "\"");
			ast = termFactory.parse(astString);
		}
		
		return toNode(ast, sort);
	}
	
	private TNode toNode(ATerm term, String sort) {
		switch (term.getType()) {
			case ATerm.PLACEHOLDER:
				return placeholderToNode(term, sort);
				
			case ATerm.APPL:
				return applToNode(term, sort);
				
			case ATerm.LIST:
				return listToNode(term, sort);
				
			case ATerm.INT:
				ATermInt i = (ATermInt) term;
				return factory.createIntTerminal(sort, leftToken, i.getInt());
				
			/*
			case ATerm.REAL:
				ATermInt i = (ATermReal) term;
				return factory.createRealTerminal(sort, leftToken, i.getReal());
			*/
				
			default:
				throw new IllegalStateException("Unexpected term type encountered in {ast} attribute");
		}
	}
	
	private TNode placeholderToNode(ATerm placeholder, String sort) {
		ATerm term = ((ATermPlaceholder) placeholder).getPlaceholder();
		if (term.getType() == ATerm.INT) {
			int id = ((ATermInt) term).getInt();
			if (1 <= id && id <= placeholderValues.size()) {
				return placeholderValues.get(id - 1);
			}
		} else if (term.getType() == ATerm.APPL) {
			String type = ((ATermAppl) term).getName();
			if ("conc".equals(type) && term.getChildCount() == 2) {
				TNode left = toNode(termAt(term, 0), null);
				TNode right = toNode(termAt(term, 1), null);
				List<TNode> children = new ArrayList<TNode>();
				for (TNode node : factory.getChildren(left))
					children.add(node);
				for (TNode node : factory.getChildren(right))
					children.add(node);
				return factory.createList(sort, leftToken, rightToken, children);
			} else if ("yield".equals(type) && term.getChildCount() == 1) {
				throw new NotImplementedException("not implemented: yield in {ast} attribute");
			}
		}
			
		throw new IllegalStateException("Error in syntax definition: illegal placeholder in {ast} attribute: " + placeholder);
	}
	
	private TNode applToNode(ATerm term, String sort) {
		ATermAppl appl = (ATermAppl) term;
		ArrayList<TNode> children = new ArrayList<TNode>(appl.getChildCount());
		for (int i = 0; i < appl.getChildCount(); i++) {
			children.add(toNode(termAt(appl, i), null));
		}
		if (appl.getType() == ATerm.STRING) {
			return factory.createStringTerminal(sort, appl.getName(), leftToken);
		} else {
			return factory.createNonTerminal(sort, appl.getName(), leftToken, rightToken, children);
		}
	}
	
	private TNode listToNode(ATerm term, String sort) {
		// TODO: Fishy (Spoofax/49)
		ATermList list = (ATermList) term;
		ArrayList<TNode> children = new ArrayList<TNode>(list.getChildCount());
		for (int i = 0; i < term.getChildCount(); i++) {
			children.add(toNode(termAt(term, i), null));
		}
		return factory.createList(sort, leftToken, rightToken, children);
	}
}
