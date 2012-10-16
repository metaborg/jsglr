package org.spoofax.jsglr.client.editregion.test;


import java.io.IOException;

import org.junit.Test;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParseException;
import org.spoofax.jsglr.shared.BadTokenException;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr.shared.TokenExpectedException;

public class TestEditRegionLayout extends AbstractTestRegionDetection {
	
	public TestEditRegionLayout() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException, InterruptedException{
		pathToJavaTestInputs = "tests-editregions/java/layout";
		setJavaParser();
	}

	@Test
	public void testDeletedBlockCommentEnding1() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException, InterruptedException {
		String expectedConcatenatedDiscards = "/*vv";
		String pathToErroneousFile = pathToJavaTestInputs + "/delete-block-comment-ending1.java";
		testDiscardedCharacters(expectedConcatenatedDiscards, pathToErroneousFile);
		super.parseString(editRegionRecovery.getRecoveredInput());
	}

	@Test
	public void testDeletedBlockCommentEnding2() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException, InterruptedException {
		String expectedConcatenatedDiscards = "/*vv";
		String pathToErroneousFile = pathToJavaTestInputs + "/delete-block-comment-ending2.java";
		testDiscardedCharacters(expectedConcatenatedDiscards, pathToErroneousFile);
		super.parseString(editRegionRecovery.getRecoveredInput());
	}

	@Test
	public void testDeletedBlockCommentEnding3() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException, InterruptedException {
		String expectedConcatenatedDiscards = "/*uu";
		String pathToErroneousFile = pathToJavaTestInputs + "/delete-block-comment-ending3.java";
		testDiscardedCharacters(expectedConcatenatedDiscards, pathToErroneousFile);
		super.parseString(editRegionRecovery.getRecoveredInput());
	}

	@Test
	public void testDeletedLayoutInbetween() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException, InterruptedException {
		String expectedConcatenatedDiscards = "inti;\n\t\t";
		String pathToErroneousFile = pathToJavaTestInputs + "/delete-layout-inbetween.java";
		testDiscardedCharacters(expectedConcatenatedDiscards, pathToErroneousFile);
		super.parseString(editRegionRecovery.getRecoveredInput());
	}

	@Test
	public void testDeletedLineCommentEnding() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException, InterruptedException {
		String expectedConcatenatedDiscards = "//www"; 
				//"private void m(int x, int y, int z){\n\t\tint v = 10;\n\t\t/*uuu*/\n\t\tv+= 10;\n\t\tint      i;\n\t\tint/*vvv*/  j;\n\t\tSystem.out.println(v); //www	}";
		String pathToErroneousFile = pathToJavaTestInputs + "/delete-line-comment-ending.java";
		testDiscardedCharacters(expectedConcatenatedDiscards, pathToErroneousFile);
		super.parseString(editRegionRecovery.getRecoveredInput());
	}

	@Test
	public void testDeletedSomeLOInbetween1() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException, InterruptedException {
		String expectedConcatenatedDiscards = "";
		String pathToErroneousFile = pathToJavaTestInputs + "/delete-some-layout-inbetween1.java";
		testDiscardedCharacters(expectedConcatenatedDiscards, pathToErroneousFile);
		super.parseString(editRegionRecovery.getRecoveredInput());
	}

	@Test
	public void testDeletedSomeLOInbetween2() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException, InterruptedException {
		String expectedConcatenatedDiscards = "";
		String pathToErroneousFile = pathToJavaTestInputs + "/delete-some-layout-inbetween2.java";
		testDiscardedCharacters(expectedConcatenatedDiscards, pathToErroneousFile);
		super.parseString(editRegionRecovery.getRecoveredInput());
	}

	@Test 
	public void testDeletedBlockComment1() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException, InterruptedException {
		String expectedConcatenatedDiscards = "";
		String pathToErroneousFile = pathToJavaTestInputs + "/delete-block-comment1.java";
		testDiscardedCharacters(expectedConcatenatedDiscards, pathToErroneousFile);
		super.parseString(editRegionRecovery.getRecoveredInput());
	}

	@Test
	public void testDeletedBlockComment2() throws IOException, InvalidParseTableException, TokenExpectedException, BadTokenException, ParseException, SGLRException, InterruptedException {
		String expectedConcatenatedDiscards = "intj;\n\t\t";
		String pathToErroneousFile = pathToJavaTestInputs + "/delete-block-comment2.java";
		testDiscardedCharacters(expectedConcatenatedDiscards, pathToErroneousFile);
		super.parseString(editRegionRecovery.getRecoveredInput());
	}

}
