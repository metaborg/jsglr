package org.spoofax.interpreter.library.jsglr.origin;

import static jsglr.shared.ImploderAttachment.getImploderOrigin;
import static jsglr.shared.ImploderAttachment.hasImploderOrigin;
import static org.spoofax.terms.Term.isTermList;
import static org.spoofax.terms.attachments.OriginAttachment.tryGetOrigin;
import static org.spoofax.terms.attachments.ParentAttachment.getParent;
import static org.spoofax.terms.attachments.ParentAttachment.putParent;

import java.util.Arrays;
import java.util.List;

import org.spoofax.interpreter.core.IContext;
import org.spoofax.interpreter.library.AbstractPrimitive;
import org.spoofax.interpreter.stratego.Strategy;
import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.TermTreeFactory;
import org.spoofax.terms.attachments.ParentAttachment;


/**
 * Returns the (sub)list with origin nodes by mapping all subterms of a list one by one.
 * It is checked that all origin nodes have the same list-parent and are sequential.
 * @author Maartje de Jonge
 */
public class OriginSublistTermPrimitive extends AbstractPrimitive {
	
	private static final String NAME = "SSL_EXT_origin_sublist_term";

	public OriginSublistTermPrimitive() {
		super(NAME, 0, 1);
	}
	
	@Override
	public final boolean call(IContext env, Strategy[] svars, IStrategoTerm[] tvars) {
		if (!isTermList(tvars[0]))
			return false;
		if(tvars[0].getTermType() != IStrategoTerm.LIST)
			return false;
		IStrategoList list=(IStrategoList)tvars[0];
		if(list.isEmpty())
			return false;
		for (IStrategoTerm child : list.getAllSubterms()) {
			if(!hasImploderOrigin(child))
				return false;
		}
		IStrategoTerm firstChildNode= tryGetOrigin(list.getSubterm(0));
		IStrategoTerm commonParentList=getParent(firstChildNode);
		if(commonParentList == null || !(isTermList(commonParentList)))
			return false;
		List<IStrategoTerm> childNodes= Arrays.asList(commonParentList.getAllSubterms());
		int startIndex=-1;
		for (int i = 0; i < childNodes.size(); i++) {
			if(childNodes.get(i)==firstChildNode){
				startIndex=i;
				break;
			}
		}
		if (startIndex < 0) {
			return false;
		}
		for (int i = 0; i < list.size(); i++) {
			if(childNodes.size()<=i+startIndex)
				return false;
			IStrategoTerm childNode=getImploderOrigin(list.getSubterm(i));
			if(childNodes.get(i+startIndex)!=childNode)
				return false;
		}
		IStrategoTerm lastChildNode= getImploderOrigin(list.getSubterm(list.size()-1));
		if(ParentAttachment.getParent(commonParentList) == null && ParentAttachment.getParent(list) != null)
			putParent(commonParentList, ParentAttachment.get(list));		
		IStrategoTerm result = new TermTreeFactory(env.getFactory()).createSublist((IStrategoList) commonParentList, firstChildNode, lastChildNode);
		if (result == null) 
			return false;
		env.setCurrent(result);
		return true;
	}
	
}
