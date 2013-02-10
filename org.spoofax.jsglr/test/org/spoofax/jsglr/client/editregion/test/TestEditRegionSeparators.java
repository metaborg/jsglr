package org.spoofax.jsglr.client.editregion.test;

import java.io.IOException;
import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Test;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParseException;
import org.spoofax.jsglr.shared.BadTokenException;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr.shared.TokenExpectedException;

public class TestEditRegionSeparators extends AbstractTestRegionDetection {
	public TestEditRegionSeparators() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException, InterruptedException{
		pathToJavaTestInputs = "tests-editregions/java/separators";
		setJavaParser();
	}

	@Test
	public void testDelSeparator() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException, InterruptedException {
		String pathToErroneousFile = pathToJavaTestInputs + "/del-separator.java";
		String erroneousInput = loadAsString(pathToErroneousFile);
		ArrayList<Integer> discardOffsets = super.getDiscardOffsets(lastErr0AST, erroneousInput);
		String concatenated = concatenatedDiscardChars(erroneousInput, discardOffsets);
		super.parseString(editRegionRecovery.getRecoveredInput());
		Assert.assertEquals(" int z", concatenated);
	}
	
	@Test
	public void testDelSeparatedElemFirst() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException, InterruptedException {
		String pathToErroneousFile = pathToJavaTestInputs + "/del-separated-elem-first.java";
		String erroneousInput = loadAsString(pathToErroneousFile);
		ArrayList<Integer> discardOffsets = super.getDiscardOffsets(lastErr0AST, erroneousInput);
		String concatenated = concatenatedDiscardChars(erroneousInput, discardOffsets);
		super.parseString(editRegionRecovery.getRecoveredInput());
		Assert.assertEquals(", ", concatenated);
	}
	
	@Test
	public void testDelSeparatedElemMid() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException, InterruptedException {
		String pathToErroneousFile = pathToJavaTestInputs + "/del-separated-elem-mid.java";
		String erroneousInput = loadAsString(pathToErroneousFile);
		ArrayList<Integer> discardOffsets = super.getDiscardOffsets(lastErr0AST, erroneousInput);
		String concatenated = concatenatedDiscardChars(erroneousInput, discardOffsets);
		super.parseString(editRegionRecovery.getRecoveredInput());
		Assert.assertEquals(", ", concatenated);
	}

	@Test
	public void testDelSeparatedElemLast() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException, InterruptedException {
		String pathToErroneousFile = pathToJavaTestInputs + "/del-separated-elem-last.java";
		String erroneousInput = loadAsString(pathToErroneousFile);
		ArrayList<Integer> discardOffsets = super.getDiscardOffsets(lastErr0AST, erroneousInput);
		String concatenated = concatenatedDiscardChars(erroneousInput, discardOffsets);
		super.parseString(editRegionRecovery.getRecoveredInput());
		Assert.assertEquals(", z", concatenated);
	}

	@Test
	public void testDelSeparatorAndNextElem() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException, InterruptedException {
		String pathToErroneousFile = pathToJavaTestInputs + "/del-separator-and-next-elem.java";
		String erroneousInput = loadAsString(pathToErroneousFile);
		ArrayList<Integer> discardOffsets = super.getDiscardOffsets(lastErr0AST, erroneousInput);
		String concatenated = concatenatedDiscardChars(erroneousInput, discardOffsets);
		super.parseString(editRegionRecovery.getRecoveredInput());
		Assert.assertEquals(" z", concatenated); //TODO: not needed to remove 'int y' if ', int z' is affected.
		System.out.println(editRegionRecovery.getRecoveredInput());
	}

}
