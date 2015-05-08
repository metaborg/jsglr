package org.spoofax.jsglr.client.editregion.test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import junit.framework.Assert;

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

import org.spoofax.jsglr.client.editregion.detection.EditRegionDetector;

public class AbstractTestRegionDetection {

	private SGLR sglr;
	protected EditRegionDetector editRegionRecovery;
	protected String pathToCorrectFile;
	protected IStrategoTerm lastErr0AST;
	protected String pathToJavaTestInputs;

	public AbstractTestRegionDetection() {
		super();
	}

	protected IStrategoTerm parseFile(String fname) throws IOException, FileNotFoundException, BadTokenException, TokenExpectedException, ParseException,
			SGLRException, InterruptedException {
				String inputChars = loadAsString(fname);
				IStrategoTerm trm1 = parseString(inputChars);
				return trm1;
			}

	public IStrategoTerm parseString(String inputChars) throws BadTokenException, TokenExpectedException, ParseException, SGLRException {
		IStrategoTerm trm1 = null;
		try {
			trm1 = (IStrategoTerm) sglr.parse(inputChars, null, null).output;
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return trm1;
	}

	protected String loadAsString(String fname) throws IOException, FileNotFoundException {
		String inputChars = FileTools.loadFileAsString(new BufferedReader(new FileReader(fname)));
		return inputChars;
	}

	protected void setSGLR(String pathToParseTable) throws IOException, InvalidParseTableException {
		final TermFactory factory = new TermFactory();
		final IStrategoTerm tableTerm = new TermReader(factory).parseFromFile(pathToParseTable);
		final ParseTable pt = new ParseTable(tableTerm, factory);
		TermTreeFactory parentFactory = new TermTreeFactory(new ParentTermFactory(new TermFactory()));
		sglr = new SGLR(new TreeBuilder(parentFactory), pt);
		// sglr = new SGLR(new TreeBuilder(true), pt);
		sglr.getDisambiguator().setDefaultFilters();
		sglr.getDisambiguator().setHeuristicFilters(true);
		sglr.setUseStructureRecovery(false);
	}

	protected String concatenatedDiscardChars(String erroneousInput, ArrayList<Integer> discards_err) {
	//		System.out.println("discards_erroneous: " + discards_err);
			char[] discards_err_char = new char[discards_err.size()];
			for (int i = 0; i < discards_err_char.length; i++) {
				discards_err_char[i] = erroneousInput.charAt(discards_err.get(i));
			}
			System.out.println("discards_erroneous: " + String.copyValueOf(discards_err_char));
			return String.copyValueOf(discards_err_char);
		}

	protected void testDiscardedCharacters(String expectedConcatenatedDiscards, String pathToErroneousFile) throws IOException, FileNotFoundException {
		String erroneousInput = loadAsString(pathToErroneousFile);
		ArrayList<Integer> discardOffsets = getDiscardOffsets(lastErr0AST, erroneousInput);
		String concatenated = concatenatedDiscardChars(erroneousInput, discardOffsets);
		Assert.assertEquals(expectedConcatenatedDiscards, concatenated);
	}

	protected ArrayList<Integer> getDiscardOffsets(IStrategoTerm lastErr0AST, String erroneousInput) {
		editRegionRecovery = new EditRegionDetector(lastErr0AST, erroneousInput);		
		return editRegionRecovery.getDiscardOffsetsErroneousInput();
	}

	protected void setJavaParser() throws IOException,
			InvalidParseTableException, FileNotFoundException,
			BadTokenException, TokenExpectedException, ParseException,
			SGLRException, InterruptedException {
				String pathToParseTable = "tests/grammars/Java-15.tbl";
				setSGLR(pathToParseTable);
				pathToCorrectFile = pathToJavaTestInputs + "/base.java";
				lastErr0AST = parseFile(pathToCorrectFile);
			}

	protected void setStrategoParser() throws IOException,
	InvalidParseTableException, FileNotFoundException,
	BadTokenException, TokenExpectedException, ParseException,
	SGLRException {
		String pathToParseTable = "tests/grammars/Stratego.tbl";
		setSGLR(pathToParseTable);
	}

}