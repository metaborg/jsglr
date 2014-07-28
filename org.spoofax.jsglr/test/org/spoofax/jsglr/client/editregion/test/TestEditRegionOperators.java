package org.spoofax.jsglr.client.editregion.test;


import java.io.IOException;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParseException;
import org.spoofax.jsglr.shared.BadTokenException;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr.shared.TokenExpectedException;

public class TestEditRegionOperators extends AbstractTestRegionDetection {
	
	public TestEditRegionOperators() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException, InterruptedException{
		pathToJavaTestInputs = "tests-editregions/java/operators";
		setJavaParser();
	}

	@Test
	public void testDeletedOperator1() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException {
		String expectedConcatenatedDiscards = "3  ";
		String pathToErroneousFile = pathToJavaTestInputs + "/delete-operator-1.java";
		testDiscardedCharacters(expectedConcatenatedDiscards, pathToErroneousFile);
		super.parseString(editRegionRecovery.getRecoveredInput());
	}

	@Test
	public void testDeletedOperator2() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException {
		String expectedConcatenatedDiscards = "  5";
		String pathToErroneousFile = pathToJavaTestInputs + "/delete-operator-2.java";
		testDiscardedCharacters(expectedConcatenatedDiscards, pathToErroneousFile);
		super.parseString(editRegionRecovery.getRecoveredInput());
	}

	@Test
	public void testDeletedOperator3() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException {
		String expectedConcatenatedDiscards = "  8";
		String pathToErroneousFile = pathToJavaTestInputs + "/delete-operator-3.java";
		testDiscardedCharacters(expectedConcatenatedDiscards, pathToErroneousFile);
		super.parseString(editRegionRecovery.getRecoveredInput());
	}

	@Test
	public void testDeletedOperand1() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException {
		String expectedConcatenatedDiscards = " + ";
		String pathToErroneousFile = pathToJavaTestInputs + "/delete-operand-1.java";
		testDiscardedCharacters(expectedConcatenatedDiscards, pathToErroneousFile);
		super.parseString(editRegionRecovery.getRecoveredInput());
	}

	@Test
	public void testDeletedOperand2() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException {
		String expectedConcatenatedDiscards = " * ";
		String pathToErroneousFile = pathToJavaTestInputs + "/delete-operand-2.java";
		testDiscardedCharacters(expectedConcatenatedDiscards, pathToErroneousFile);
		super.parseString(editRegionRecovery.getRecoveredInput());
	}
	
	@Test
	public void testDeletedMultipleOperators1() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException {
		String expectedConcatenatedDiscards = "  4  5";
		String pathToErroneousFile = pathToJavaTestInputs + "/delete-multiple-operators1.java";
		testDiscardedCharacters(expectedConcatenatedDiscards, pathToErroneousFile);
		super.parseString(editRegionRecovery.getRecoveredInput());
	}

	@Test
	public void testDeletedMultipleOperators2() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException {
		String expectedConcatenatedDiscards = "  7  8";
		String pathToErroneousFile = pathToJavaTestInputs + "/delete-multiple-operators2.java";
		testDiscardedCharacters(expectedConcatenatedDiscards, pathToErroneousFile);
		super.parseString(editRegionRecovery.getRecoveredInput());
	}

	@Test
	public void testDeletedMultipleOperators3() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException {
		String pathToErroneousFile = pathToJavaTestInputs + "/delete-multiple-operators3.java";
		String erroneousInput = loadAsString(pathToErroneousFile);
		ArrayList<Integer> discardOffsets = getDiscardOffsets(lastErr0AST, erroneousInput);
		Assert.assertTrue(discardOffsets.size() < 20);
		super.parseString(editRegionRecovery.getRecoveredInput());
	}

	@Test
	public void testDeletedMultipleOperands() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException, InterruptedException {
		String pathToErroneousFile = pathToJavaTestInputs + "/delete-multiple-operands.java";
		String erroneousInput = loadAsString(pathToErroneousFile);
		ArrayList<Integer> discardOffsets = getDiscardOffsets(lastErr0AST, erroneousInput);
		super.parseString(editRegionRecovery.getRecoveredInput());
		Assert.assertTrue(discardOffsets.size() < 20);
	}

}
