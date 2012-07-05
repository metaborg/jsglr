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

/**
 * Helper functions for tree matching
 * @author maartje
 *
 */
public class HelperFunctions {
	
	/**
	 * Collects the leaf nodes
	 * @param trm
	 * @return
	 */
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
	public static void setParentAttachments(IStrategoTerm trm){
		assert trm.getStorageType() == IStrategoTerm.MUTABLE
	    	: "Parent attachments only work for mutable, non-shared terms; failed for " + trm;
		IStrategoTerm[] subterms = trm.getAllSubterms();
		for (int i = 0; i < subterms.length; i++) {
			ParentAttachment.putParent(subterms[i], trm, null);
			setParentAttachments(subterms[i]);
		}
	}
	
	/**
	 * Says whether the term is a primitive type, e.g. String, Int, or Real 
	 * @param trm
	 * @return
	 */
	public static boolean isPrimitiveType(IStrategoTerm trm) {
		if(trm == null)
			return false;
		return trm.getTermType() == StrategoTerm.INT ||
		trm.getTermType() == StrategoTerm.REAL ||
		trm.getTermType() == StrategoTerm.STRING;
	}

	/**
	 * True if both terms are Int, Real or String and have the same value  
	 * @param t1
	 * @param t2
	 * @return
	 */
	public static boolean isPrimitiveWithSameValue(IStrategoTerm t1, IStrategoTerm t2) {
		return 
			isIntWithSameValue(t1, t2) ||
			isRealWithSameValue(t1, t2) ||
			isSringWithSameValue(t1, t2);
	}

	public static boolean isIntWithSameValue(IStrategoTerm t1, IStrategoTerm t2) {
		return 
			t1.getTermType() == StrategoTerm.INT && 
			t2.getTermType() == StrategoTerm.INT && 
			((StrategoInt)t1).intValue() == ((StrategoInt)t2).intValue();
	}

	public static boolean isSringWithSameValue(IStrategoTerm t1, IStrategoTerm t2) {
		return 
			t1.getTermType() == StrategoTerm.STRING && 
			t2.getTermType() == StrategoTerm.STRING && 
			((StrategoString)t1).stringValue().equals(((StrategoString)t2).stringValue());
	}

	public static boolean isRealWithSameValue(IStrategoTerm t1, IStrategoTerm t2) {
		return 
			t1.getTermType() == StrategoTerm.REAL && 
			t2.getTermType() == StrategoTerm.REAL && 
			((StrategoReal)t1).realValue() == ((StrategoReal)t2).realValue();
	}

	/**
	 * True iff one of these conditions hold:
	 * 1. both are Appl with same constructor name and same number of subterms
	 * 2. both are lists
	 * 3. both are tuples
	 * @param t1
	 * @param t2
	 * @return
	 */
	public static boolean haveSameSignature(IStrategoTerm t1, IStrategoTerm t2){
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

	static boolean haveSameConstructorName(IStrategoTerm t1, IStrategoTerm t2) {
		String cons1 = ((IStrategoAppl) t1).getConstructor().getName();
		String cons2 = ((IStrategoAppl) t2).getConstructor().getName();
		boolean sameConstructorName = cons1.equals(cons2);
		return sameConstructorName;
	}

	static boolean isPrimitiveWithDifferentValues(IStrategoTerm ln, IStrategoTerm lnPartner) {
		return lnPartner != null && ln != null && isPrimitiveType(ln) && isPrimitiveType(lnPartner) && !isPrimitiveWithSameValue(ln, lnPartner);
	}

	public static boolean isSameTermType(IStrategoTerm t1, IStrategoTerm t2) {
		return
			(Tools.isTermAppl(t1) && Tools.isTermAppl(t2)) 
		||  (Tools.isTermInt(t1) && Tools.isTermInt(t2))
		||  (Tools.isTermList(t1) && Tools.isTermList(t2))
		||  (Tools.isTermReal(t1) && Tools.isTermReal(t2))
		||  (Tools.isTermString(t1) && Tools.isTermString(t2))
		||  (Tools.isTermTuple(t1) && Tools.isTermTuple(t2));
	}
}
