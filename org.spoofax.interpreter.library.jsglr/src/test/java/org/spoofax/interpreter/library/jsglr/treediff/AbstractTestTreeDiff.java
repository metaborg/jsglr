package org.spoofax.interpreter.library.jsglr.treediff;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParseException;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.client.SGLR;
import org.spoofax.jsglr.client.imploder.TermTreeFactory;
import org.spoofax.jsglr.client.imploder.TreeBuilder;
import org.spoofax.jsglr.io.FileTools;
import org.spoofax.jsglr.shared.BadTokenException;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr.shared.TokenExpectedException;
import org.spoofax.terms.TermFactory;
import org.spoofax.terms.attachments.ParentTermFactory;
import org.spoofax.terms.io.binary.TermReader;

public abstract class AbstractTestTreeDiff {

	private SGLR sglr;

	public AbstractTestTreeDiff() throws IOException, InvalidParseTableException {
		super();
		String pathToParseTable = "tests/parsetables/Java.tbl";
		setSGLR(pathToParseTable);
	}

	protected IStrategoTerm parseFile(String fname) throws IOException,
			FileNotFoundException, BadTokenException, TokenExpectedException,
			ParseException, SGLRException, InterruptedException {
				String inputChars = FileTools.loadFileAsString(new BufferedReader(new FileReader(fname)));
				IStrategoTerm trm1 = (IStrategoTerm) sglr.parse(inputChars, fname, null).output;
				return trm1;
			}

	protected void setSGLR(String pathToParseTable) throws IOException,
			InvalidParseTableException {
				final TermFactory factory = new TermFactory();
				final IStrategoTerm tableTerm = new TermReader(factory).parseFromFile(pathToParseTable);
				final ParseTable pt = new ParseTable(tableTerm, factory);		
				TermTreeFactory parentFactory = new TermTreeFactory(new ParentTermFactory(new TermFactory()));
				sglr = new SGLR(new TreeBuilder(parentFactory), pt);
				//sglr = new SGLR(new TreeBuilder(true), pt);
				sglr.getDisambiguator().setDefaultFilters();
				sglr.getDisambiguator().setHeuristicFilters(true);
				sglr.setUseStructureRecovery(false);
			}

	protected int countMatches(IStrategoTerm trm) {
		int nrOfMatches = 0;
		if(TermMatchAttachment.getMatchedTerm(trm) != null)
			nrOfMatches += 1;
		for (int i = 0; i < trm.getSubtermCount(); i++) {
			nrOfMatches += countMatches(trm.getSubterm(i));
		}
		return nrOfMatches;
	}

	protected void topdownPrintMatching(IStrategoTerm trm) {
		printMatching(trm);
		for (int i = 0; i < trm.getSubtermCount(); i++) {
			topdownPrintMatching(trm.getSubterm(i));
		}
			
	}

	protected void printMatching(IStrategoTerm trm) {
		System.out.println("M: ");
		System.out.println("   - " + trm);
		System.out.println("   - " + TermMatchAttachment.getMatchedTerm(trm));
	}

}