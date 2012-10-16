package org.spoofax.jsglr.client.editregion.test;

import java.io.IOException;
import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Test;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParseException;
import org.spoofax.jsglr.shared.BadTokenException;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr.shared.TokenExpectedException;

public class TestEditRegionMisc extends AbstractTestRegionDetection {
	public TestEditRegionMisc() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException, InterruptedException{
		pathToJavaTestInputs = "tests-editregions/java/misc";
		setJavaParser();
	}

	@Test
	public void testCopyPaste() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException {
		String pathToErroneousFile = pathToJavaTestInputs + "/copy-paste.java";
		String erroneousInput = loadAsString(pathToErroneousFile);
		ArrayList<Integer> discardOffsets = super.getDiscardOffsets(lastErr0AST, erroneousInput);
		String concatenated = concatenatedDiscardChars(erroneousInput, discardOffsets);
		super.parseString(editRegionRecovery.getRecoveredInput());
		Assert.assertEquals(88, concatenated.length());
	}

	@Test
	public void testRequiredListElem() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException {
		String pathToErroneousFile = pathToJavaTestInputs + "/del-required-list-elem.java";
		String erroneousInput = loadAsString(pathToErroneousFile);
		ArrayList<Integer> discardOffsets = super.getDiscardOffsets(lastErr0AST, erroneousInput);
		String concatenated = concatenatedDiscardChars(erroneousInput, discardOffsets);
		Assert.assertEquals("tx", concatenated); //REMARk: does not solve the syntax error
		//super.parseString(editRegionRecovery.getRecoveredInput());
	}

	@Test
	public void testLargeRegion() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException, InterruptedException {

		String pathToCorrectFile = pathToJavaTestInputs + "/tricky.base.java";
		IStrategoTerm lastErr0AST = parseFile(pathToCorrectFile);

		String pathToErroneousFile = pathToJavaTestInputs + "/tricky.java";
		String erroneousInput = loadAsString(pathToErroneousFile);
		ArrayList<Integer> discardOffsets = getDiscardOffsets(lastErr0AST, erroneousInput);
		String concatenated = concatenatedDiscardChars(erroneousInput, discardOffsets);
		Assert.assertEquals(4142, concatenated.length()); 
		//System.out.println(editRegionRecovery.getRecoveredInput());
		//super.parseString(editRegionRecovery.getRecoveredInput());
		
		//REMARk: probably better to return a small erroneous region that does not contain all errors
		//than a very big unuseful region
		//REMARK: lcs (current implementation) takes too much time for large regions
		//TODOT
	}

}
