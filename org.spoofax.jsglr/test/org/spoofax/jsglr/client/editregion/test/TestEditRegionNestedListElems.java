package org.spoofax.jsglr.client.editregion.test;


import java.io.IOException;

import org.junit.Test;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParseException;
import org.spoofax.jsglr.shared.BadTokenException;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr.shared.TokenExpectedException;

public class TestEditRegionNestedListElems extends AbstractTestRegionDetection {
	
	public TestEditRegionNestedListElems() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException, InterruptedException{
		pathToJavaTestInputs = "tests-editregions/java/nested-list-elems";
		setJavaParser();
	}

	@Test
	public void testDeletionInParentElem1() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException {
		String expectedConcatenatedDiscards = "while(i > 5)\n\t\t\t\n\t\t}";
		String pathToErroneousFile = pathToJavaTestInputs + "/deletion-in-parent-elem1.java";
		testDiscardedCharacters(expectedConcatenatedDiscards, pathToErroneousFile);
		super.parseString(editRegionRecovery.getRecoveredInput());
	}

	@Test
	public void testDeletionInParentElem2() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException {
		String expectedConcatenatedDiscards = "while(i > 5\n\t\t\t\n\t\t}";
		String pathToErroneousFile = pathToJavaTestInputs + "/deletion-in-parent-elem2.java";
		testDiscardedCharacters(expectedConcatenatedDiscards, pathToErroneousFile);
		super.parseString(editRegionRecovery.getRecoveredInput());
	}

	@Test
	public void testDeletionInParentElem3() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException {
		String expectedConcatenatedDiscards = "while(){\n\t\t\t\n\t\t}";
		String pathToErroneousFile = pathToJavaTestInputs + "/deletion-in-parent-elem3.java";
		testDiscardedCharacters(expectedConcatenatedDiscards, pathToErroneousFile);
		super.parseString(editRegionRecovery.getRecoveredInput());
	}

	@Test
	public void testDeletionInParentElem4() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException {
		String expectedConcatenatedDiscards = "while(i > 5 {\n\t\t\t\n\t\t}";
		String pathToErroneousFile = pathToJavaTestInputs + "/deletion-in-parent-elem4.java";
		testDiscardedCharacters(expectedConcatenatedDiscards, pathToErroneousFile);
		super.parseString(editRegionRecovery.getRecoveredInput());
	}

	@Test
	public void testDeletionInParentAndChild() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException {
		String expectedConcatenatedDiscards = "while(i > 5 {\n\t\t\t\n\t\t\ti = i + \n\t\t\t\t\n\t\t\t\n\t\t}";
		String pathToErroneousFile = pathToJavaTestInputs + "/deletion-in-parent-and-child-elem.java";
		testDiscardedCharacters(expectedConcatenatedDiscards, pathToErroneousFile);
		super.parseString(editRegionRecovery.getRecoveredInput());
	}
}
