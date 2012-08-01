package org.spoofax.jsglr.client.editregion.test;


import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParseException;
import org.spoofax.jsglr.shared.BadTokenException;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr.shared.TokenExpectedException;

public class TestEditRegionNestedListElems extends AbstractTestRegionDetection {
	
	public TestEditRegionNestedListElems() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException{
		pathToJavaTestInputs = "tests-editregions/java/nested-list-elems";
		setJavaParser();
	}

	@Ignore @Test
	public void testDeletionInParentElem1() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException {
		String expectedConcatenatedDiscards = "@@";
		String pathToErroneousFile = pathToJavaTestInputs + "/deletion-in-parent-elem1.java";
		testDiscardedCharacters(expectedConcatenatedDiscards, pathToErroneousFile);
		super.parseString(editRegionRecovery.getRecoveredInput());
	}

	@Ignore @Test
	public void testDeletionInParentElem2() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException {
		String expectedConcatenatedDiscards = "@@";
		String pathToErroneousFile = pathToJavaTestInputs + "/deletion-in-parent-elem2.java";
		testDiscardedCharacters(expectedConcatenatedDiscards, pathToErroneousFile);
		super.parseString(editRegionRecovery.getRecoveredInput());
	}

	@Ignore @Test
	public void testDeletionInParentElem3() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException {
		String expectedConcatenatedDiscards = "@@";
		String pathToErroneousFile = pathToJavaTestInputs + "/deletion-in-parent-elem3.java";
		testDiscardedCharacters(expectedConcatenatedDiscards, pathToErroneousFile);
		super.parseString(editRegionRecovery.getRecoveredInput());
	}

	@Ignore @Test
	public void testDeletionInParentElem4() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException {
		String expectedConcatenatedDiscards = "@@";
		String pathToErroneousFile = pathToJavaTestInputs + "/deletion-in-parent-elem4.java";
		testDiscardedCharacters(expectedConcatenatedDiscards, pathToErroneousFile);
		super.parseString(editRegionRecovery.getRecoveredInput());
	}
}
