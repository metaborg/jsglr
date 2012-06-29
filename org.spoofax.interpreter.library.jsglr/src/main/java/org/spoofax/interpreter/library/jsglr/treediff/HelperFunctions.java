package org.spoofax.interpreter.library.jsglr.treediff;

import java.util.ArrayList;

import org.spoofax.interpreter.core.Tools;
import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.terms.StrategoInt;
import org.spoofax.terms.StrategoReal;
import org.spoofax.terms.StrategoString;
import org.spoofax.terms.StrategoTerm;
import org.spoofax.terms.attachments.ParentAttachment;

public class HelperFunctions {
	
	public static ArrayList<IStrategoTerm> collectLeafnodes(IStrategoTerm trm){
		ArrayList<IStrategoTerm> leafnodes = new ArrayList<IStrategoTerm>();
		IStrategoTerm[] subterms = trm.getAllSubterms();
		if(subterms.length == 0){
			leafnodes.add(trm);
		}
		else {
			for (int i = 0; i < subterms.length; i++) {
				leafnodes.addAll(collectLeafnodes(subterms[i]));
			}
		}
		return leafnodes;			
	}
	
	/**
	 * Sets the parent attachment for each term in the AST
	 * @param trm
	 */
	static void setParentAttachments(IStrategoTerm trm){
		assert trm.getStorageType() == IStrategoTerm.MUTABLE
	    	: "Parent attachments only work for mutable, non-shared terms; failed for " + trm;
		IStrategoTerm[] subterms = trm.getAllSubterms();
		for (int i = 0; i < subterms.length; i++) {
			ParentAttachment.putParent(subterms[i], trm, null);
			setParentAttachments(subterms[i]);
		}
	}
	
	static boolean isPrimitiveType(IStrategoTerm trm) {
		return trm.getTermType() == StrategoTerm.INT ||
		trm.getTermType() == StrategoTerm.REAL ||
		trm.getTermType() == StrategoTerm.STRING;
	}

	static boolean isPrimitiveWithSameValue(IStrategoTerm t1, IStrategoTerm t2) {
		return 
			isIntWithSameValue(t1, t2) ||
			isRealWithSameValue(t1, t2) ||
			isSringWithSameValue(t1, t2);
	}

	private static boolean isIntWithSameValue(IStrategoTerm t1, IStrategoTerm t2) {
		return 
			t1.getTermType() == StrategoTerm.INT && 
			t2.getTermType() == StrategoTerm.INT && 
			((StrategoInt)t1).intValue() == ((StrategoInt)t2).intValue();
	}

	private static boolean isSringWithSameValue(IStrategoTerm t1, IStrategoTerm t2) {
		return 
			t1.getTermType() == StrategoTerm.STRING && 
			t2.getTermType() == StrategoTerm.STRING && 
			((StrategoString)t1).stringValue() == ((StrategoString)t2).stringValue();
	}

	private static boolean isRealWithSameValue(IStrategoTerm t1, IStrategoTerm t2) {
		return 
			t1.getTermType() == StrategoTerm.REAL && 
			t2.getTermType() == StrategoTerm.REAL && 
			((StrategoReal)t1).realValue() == ((StrategoReal)t2).realValue();
	}

	static boolean haveSameSignature(IStrategoTerm t1, IStrategoTerm t2){
		if(t1 == null || t2 == null)
			return false;
		if(t1.getTermType() == StrategoTerm.APPL && t2.getTermType() == StrategoTerm.APPL){
			boolean sameConstructorName = haveSameConstructorName(t1, t2);
			return sameConstructorName && t1.getSubtermCount() == t2.getSubtermCount();
		}
		if (Tools.isTermList(t1) && Tools.isTermList(t2))
			return true;
		if (Tools.isTermTuple(t1) && Tools.isTermTuple(t2))
			return true;
		else
			return false;
	}

	static boolean haveSameConstructorName(IStrategoTerm t1,
			IStrategoTerm t2) {
		String cons1 = ((IStrategoAppl) t1).getConstructor().getName();
		String cons2 = ((IStrategoAppl) t2).getConstructor().getName();
		boolean sameConstructorName = cons1.equals(cons2);
		return sameConstructorName;
	}
}
