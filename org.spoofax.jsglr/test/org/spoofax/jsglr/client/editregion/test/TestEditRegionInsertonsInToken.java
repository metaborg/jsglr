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

public class TestEditRegionInsertonsInToken extends AbstractTestRegionDetection {
	public TestEditRegionInsertonsInToken() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException, InterruptedException{
		pathToJavaTestInputs = "tests-editregions/java/insertions";
		setJavaParser();
	}

	@Test
	public void testInsertionInTerminal1() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException, InterruptedException {
		String pathToErroneousFile = pathToJavaTestInputs + "/ins-in-terminal1.java";
		String erroneousInput = loadAsString(pathToErroneousFile);
		ArrayList<Integer> discardOffsets = super.getDiscardOffsets(lastErr0AST, erroneousInput);
		String concatenated = concatenatedDiscardChars(erroneousInput, discardOffsets);
		super.parseString(editRegionRecovery.getRecoveredInput());
		Assert.assertEquals("Sys tem.out.println()", concatenated); 
	}

	@Test
	public void testInsertionInTerminal2() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException, InterruptedException {
		String pathToErroneousFile = pathToJavaTestInputs + "/ins-in-terminal2.java";
		String erroneousInput = loadAsString(pathToErroneousFile);
		ArrayList<Integer> discardOffsets = super.getDiscardOffsets(lastErr0AST, erroneousInput);
		String concatenated = concatenatedDiscardChars(erroneousInput, discardOffsets);
		super.parseString(editRegionRecovery.getRecoveredInput());
		Assert.assertEquals("Systtem.out.println()", concatenated); 
	}

	@Test
	public void testInsertionInTerminal3() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException, InterruptedException {
		String pathToErroneousFile = pathToJavaTestInputs + "/ins-in-terminal3.java";
		String erroneousInput = loadAsString(pathToErroneousFile);
		ArrayList<Integer> discardOffsets = super.getDiscardOffsets(lastErr0AST, erroneousInput);
		String concatenated = concatenatedDiscardChars(erroneousInput, discardOffsets);
		super.parseString(editRegionRecovery.getRecoveredInput());
		Assert.assertEquals("x", concatenated); 
	}

	@Test
	public void testInsertionInTerminal4() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException, InterruptedException {
		String pathToErroneousFile = pathToJavaTestInputs + "/ins-in-terminal4.java";
		String erroneousInput = loadAsString(pathToErroneousFile);
		ArrayList<Integer> discardOffsets = super.getDiscardOffsets(lastErr0AST, erroneousInput);
		String concatenated = concatenatedDiscardChars(erroneousInput, discardOffsets);
		super.parseString(editRegionRecovery.getRecoveredInput());
		Assert.assertEquals("Sys-tem.out.println()", concatenated); 
	}

	@Test
	public void testInsertionInCommentOpening() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException, InterruptedException {
		String pathToErroneousFile = pathToJavaTestInputs + "/ins-in-comment-opening.java";
		String erroneousInput = loadAsString(pathToErroneousFile);
		ArrayList<Integer> discardOffsets = super.getDiscardOffsets(lastErr0AST, erroneousInput);
		String concatenated = concatenatedDiscardChars(erroneousInput, discardOffsets);
		super.parseString(editRegionRecovery.getRecoveredInput());
		Assert.assertEquals("/ *mmmm*/", concatenated); 
	}
}
