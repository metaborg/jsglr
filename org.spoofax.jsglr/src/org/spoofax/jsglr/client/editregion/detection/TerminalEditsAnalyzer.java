package org.spoofax.jsglr.client.editregion.detection;

import java.util.ArrayList;
import java.util.Collections;

import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr.client.imploder.Token;

/**
 * Adds to the deletion offsets the (start)offset of non-layout tokens that are damaged by an insertion only 
 * (and therefore not already covered by the deletion offsets).
 * The reason is that a "parse-as-whitespace" recovery may not repair the problem.
 * Example damaged token: 'priva@te'
 * @author Maartje de Jonge
 */
public class TerminalEditsAnalyzer {
		
	private final DamagedTokenAnalyzer tokenEdits;

	//filled in the analysis
	private final ArrayList<Integer> startOffsetsTokensDamagedByInsertions;
	
	/**
	 * Extends the list of deletion-offsets,
	 * adding all start offsets of damaged tokens that were not yet covered (e.g. only affected by insertion)
	 */
	public void addDamagedTokensStartOffsets(ArrayList<Integer> deletedOffsets) {
		deletedOffsets.addAll(startOffsetsTokensDamagedByInsertions);
		Collections.sort(deletedOffsets);
	}
	
	/**
	 * Adds to the deletion offsets the (start)offset of non-layout tokens that are damaged by an insertion only 
	 * (and therefore not already covered by the deletion offsets).
	 * The reason is that a "parse-as-whitespace" recovery may not repair the problem.
	 * Example damaged token: 'priva@te'
	 */
	public TerminalEditsAnalyzer(DamagedTokenAnalyzer tokenEdits){
		this.startOffsetsTokensDamagedByInsertions = new ArrayList<Integer>();
		this.tokenEdits = tokenEdits;
		analyze();
	}
	
	private void analyze() {
		for (IToken tokenWithInsertions : tokenEdits.getTokensDamagedByInsertion()) {
			if(tokenWithInsertions.getKind() != Token.TK_LAYOUT && !tokenEdits.isDamagedByDeletion(tokenWithInsertions)){ //non layout, insertions only, damaged token
				this.startOffsetsTokensDamagedByInsertions.add(tokenWithInsertions.getStartOffset());
			}
		}
	}
}
