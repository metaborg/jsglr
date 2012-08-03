package org.spoofax.jsglr.client.editregion.detection;

import java.util.ArrayList;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.ITokenizer;
import org.spoofax.jsglr.client.imploder.ImploderAttachment;
import org.spoofax.jsglr.client.imploder.Token;
import org.spoofax.terms.attachments.AbstractTermAttachment;
import org.spoofax.terms.attachments.TermAttachmentType;
import org.spoofax.terms.attachments.VolatileTermAttachmentType;

/**
 * @author maartje
 */
public class RecoverAttachment extends AbstractTermAttachment {

//private fields
	
	private final ArrayList<IStrategoTerm> recoveredSubterms;
	private final ArrayList<Integer> coveredDeletionOffsets;

//creation of recover attachment 
	
	public static void putUndamagedRecoverAttachment(IStrategoTerm term, ArrayList<Integer> deletions){
		ArrayList<IStrategoTerm> recoveredSubterms = new ArrayList<IStrategoTerm>();
		recoveredSubterms.add(term); //term is its own recovery
		ArrayList<Integer> coveredOffsets = getCoveredOffsets(term, deletions);
		RecoverAttachment recovery = new RecoverAttachment(recoveredSubterms, coveredOffsets);
		term.putAttachment(recovery);
	}

	public static void putFailedRecoverAttachment(IStrategoTerm term, ArrayList<Integer> deletions){
		ArrayList<Integer> coveredOffsets = getCoveredOffsets(term, deletions);
		RecoverAttachment recovery = new RecoverAttachment(null, coveredOffsets);
		term.putAttachment(recovery);
	}

	public static void putDiscardRecoverAttachment(IStrategoTerm term, ArrayList<Integer> deletions){
		ArrayList<Integer> coveredOffsets = getCoveredOffsets(term, deletions);
		RecoverAttachment recovery = new RecoverAttachment(new ArrayList<IStrategoTerm>(), coveredOffsets);
		term.putAttachment(recovery);
	}

	public static void putReplaceRecoverAttachment(IStrategoTerm term, IStrategoTerm recoveredSubterm, ArrayList<Integer> deletions){
		ArrayList<IStrategoTerm> recoveredSubterms = new ArrayList<IStrategoTerm>();
		recoveredSubterms.add(recoveredSubterm);
		ArrayList<Integer> coveredOffsets = getCoveredOffsets(term, deletions);
		RecoverAttachment recovery = new RecoverAttachment(recoveredSubterms, coveredOffsets);
		term.putAttachment(recovery);
	}

	public static void putListReplaceRecoverAttachment(IStrategoTerm term, ArrayList<IStrategoTerm> recoveredSubterms, ArrayList<Integer> deletions){
		ArrayList<Integer> coveredOffsets = getCoveredOffsets(term, deletions);
		RecoverAttachment recovery = new RecoverAttachment(recoveredSubterms, coveredOffsets);
		term.putAttachment(recovery);
	}
	
	private RecoverAttachment(ArrayList<IStrategoTerm> recoveredSubterms, ArrayList<Integer> coveredDeletionOffsets) {
		this.coveredDeletionOffsets = coveredDeletionOffsets;
		this.recoveredSubterms = recoveredSubterms;
	}
	
//access recover information 

	public static boolean hasRecoverAttachment(IStrategoTerm term) {
		return getRecoverAttachment(term) == null ? false : true;
	}

	public static ArrayList<IStrategoTerm> getRecoveredSubterms(IStrategoTerm term) {
		RecoverAttachment ra = getRecoverAttachment(term);
		return ra == null ? null : ra.recoveredSubterms;
	}

	public static int getNumberOfPreservedNonLayoutTokens(IStrategoTerm term){
		RecoverAttachment ra = getRecoverAttachment(term);
		return ra == null ? -1 : ra.getNumberOfPreservedNonLayoutTokens();		
	}

	public static ArrayList<Integer> getCoveredDeletionOffsets(IStrategoTerm term){
		RecoverAttachment ra = getRecoverAttachment(term);
		return ra == null ? new ArrayList<Integer>() : ra.coveredDeletionOffsets;		
	}

//private functions
				
	private static ArrayList<Integer> getCoveredOffsets(IStrategoTerm term, ArrayList<Integer> offsets) {
		ArrayList<Integer> coveredOffsets = new ArrayList<Integer>();
		int startOffset = ImploderAttachment.getLeftToken(term).getStartOffset();
		int endOffset = ImploderAttachment.getRightToken(term).getEndOffset();
		for (int i = 0; i < offsets.size(); i++) {
			int offset = offsets.get(i); 
			if(startOffset <= offset && offset <= endOffset){
				//covered
				coveredOffsets.add(offset);
			}
		}
		return coveredOffsets;
	}

	private int getNumberOfPreservedNonLayoutTokens(){
		int result = 0;
		for (IStrategoTerm recoveredSubterm : this.recoveredSubterms) {
			result += numberOfNonLayoutTokens(recoveredSubterm);
		}
		return result;
	}

	private static int numberOfNonLayoutTokens(IStrategoTerm term) {
		ITokenizer tokens = ImploderAttachment.getTokenizer(term);
		int leftTokenIndex = ImploderAttachment.getLeftToken(term).getIndex();
		int rightTokenIndex = ImploderAttachment.getRightToken(term).getIndex();
		int result = 0;
		for (int i = leftTokenIndex; i <= rightTokenIndex; i++) {
			if(tokens.getTokenAt(i).getKind() != Token.TK_LAYOUT){
				result ++;
			}				
		}
		return result;
	}

//standard attachment functions 
	
	private static final long serialVersionUID = -1224391165614342863L;

	public static final TermAttachmentType<RecoverAttachment> TYPE =
		new VolatileTermAttachmentType<RecoverAttachment>(RecoverAttachment.class);

	public TermAttachmentType<RecoverAttachment> getAttachmentType() {
		return TYPE;
	}
		
	private static RecoverAttachment getRecoverAttachment(IStrategoTerm term) {
		if(term == null)
			return null;
		return term.getAttachment(TYPE);
	}
}
