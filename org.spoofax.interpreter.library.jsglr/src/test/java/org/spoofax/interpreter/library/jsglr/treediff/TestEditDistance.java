package org.spoofax.interpreter.library.jsglr.treediff;

import static org.junit.Assert.*;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParseException;
import org.spoofax.jsglr.shared.BadTokenException;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr.shared.TokenExpectedException;

public class TestEditDistance extends AbstractTestTreeDiff {

	public TestEditDistance() throws IOException, InvalidParseTableException {
		super();
	}

	@Test
	public void testInsertion() throws TokenExpectedException, FileNotFoundException, BadTokenException, ParseException, IOException, SGLRException, InterruptedException, InterruptedException
	{
		String fname1 = "tests/test-inputs/test-insertion1-001.java";
		String fname2 = "tests/test-inputs/test-insertion2-001.java";
		IStrategoTerm trm1 = parseFile(fname1);
		IStrategoTerm trm2 = parseFile(fname2);
		
		AbstractTreeMatcher treeMatcher = new HeuristicTreeMatcher(false, false, false);
		TreeEditDistance editDistance = new TreeEditDistance();
		editDistance.detectTreeEditActions(trm1, trm2, treeMatcher);
		Assert.assertEquals(68, countMatches(trm2));	
		assertEquals(0, editDistance.getDeletionCount());
		assertEquals(15, editDistance.getInsertionCount());
		assertEquals(0, editDistance.getMovedCount());
		assertEquals(0, editDistance.getRelabeledCount());
		assertEquals(0, editDistance.getValueChangeCount());

		//With support for matching moved terms
		AbstractTreeMatcher treeMatcher_move = new HeuristicTreeMatcher(false, false, true);
		TreeEditDistance editDistance2 = new TreeEditDistance();
		IStrategoTerm trm_1 = parseFile(fname1);
		IStrategoTerm trm_2 = parseFile(fname2);
		editDistance2.detectTreeEditActions(trm_1, trm_2, treeMatcher_move);
		TreeEditDistance editDistance_move = editDistance2;
		Assert.assertEquals(68, countMatches(trm_2));	
		assertEquals(0, editDistance_move.getDeletionCount());
		assertEquals(15, editDistance_move.getInsertionCount());
		assertEquals(0, editDistance_move.getMovedCount());
		assertEquals(0, editDistance_move.getRelabeledCount());
		assertEquals(0, editDistance_move.getValueChangeCount());
	}

	@Test
	public void testDeletion() throws TokenExpectedException, FileNotFoundException, BadTokenException, ParseException, IOException, SGLRException, InterruptedException
	{
		String fname1 = "tests/test-inputs/test-insertion2-001.java";
		String fname2 = "tests/test-inputs/test-insertion1-001.java";
		IStrategoTerm trm1 = parseFile(fname1);
		IStrategoTerm trm2 = parseFile(fname2);
		
		AbstractTreeMatcher treeMatcher = new HeuristicTreeMatcher(false, false, false);
		TreeEditDistance editDistance = new TreeEditDistance();
		editDistance.detectTreeEditActions(trm1, trm2, treeMatcher);
		Assert.assertEquals(68, countMatches(trm2));	
		assertEquals(15, editDistance.getDeletionCount());
		assertEquals(0, editDistance.getInsertionCount());
		assertEquals(0, editDistance.getMovedCount());
		assertEquals(0, editDistance.getRelabeledCount());
		assertEquals(0, editDistance.getValueChangeCount());

		//With support for matching moved terms
		AbstractTreeMatcher treeMatcher_move = new HeuristicTreeMatcher(false, false, true);
		TreeEditDistance editDistance2 = new TreeEditDistance();
		IStrategoTerm trm_1 = parseFile(fname1);
		IStrategoTerm trm_2 = parseFile(fname2);
		editDistance2.detectTreeEditActions(trm_1, trm_2, treeMatcher_move);
		TreeEditDistance editDistance_move = editDistance2;
		Assert.assertEquals(68, countMatches(trm_2));	
		assertEquals(15, editDistance_move.getDeletionCount());
		assertEquals(0, editDistance_move.getInsertionCount());
		assertEquals(0, editDistance_move.getMovedCount());
		assertEquals(0, editDistance_move.getRelabeledCount());
		assertEquals(0, editDistance_move.getValueChangeCount());
	}

	@Test
	public void testUpdateValue() throws TokenExpectedException, FileNotFoundException, BadTokenException, ParseException, IOException, SGLRException, InterruptedException
	{
		String fname1 = "tests/test-inputs/test-updateValue1-001.java";
		String fname2 = "tests/test-inputs/test-updateValue2-001.java";
		IStrategoTerm trm1 = parseFile(fname1);
		IStrategoTerm trm2 = parseFile(fname2);
		
		AbstractTreeMatcher treeMatcher = new HeuristicTreeMatcher(false, false, false);
		TreeEditDistance editDistance = new TreeEditDistance();
		editDistance.detectTreeEditActions(trm1, trm2, treeMatcher);
		Assert.assertEquals(48, countMatches(trm2));	
		assertEquals(0, editDistance.getDeletionCount());
		assertEquals(0, editDistance.getInsertionCount());
		assertEquals(0, editDistance.getMovedCount());
		assertEquals(0, editDistance.getRelabeledCount());
		assertEquals(2, editDistance.getValueChangeCount());

		//With support for matching value updates
		AbstractTreeMatcher treeMatcher_move = new HeuristicTreeMatcher(false, true, false);
		TreeEditDistance editDistance2 = new TreeEditDistance();
		IStrategoTerm trm_1 = parseFile(fname1);
		IStrategoTerm trm_2 = parseFile(fname2);
		editDistance2.detectTreeEditActions(trm_1, trm_2, treeMatcher_move);
		//super.topdownPrintMatching(trm_2);
		TreeEditDistance editDistance_move = editDistance2;
		Assert.assertEquals(46, countMatches(trm_2));	
		assertEquals(2, editDistance_move.getDeletionCount());
		assertEquals(2, editDistance_move.getInsertionCount());
		assertEquals(0, editDistance_move.getMovedCount());
		assertEquals(0, editDistance_move.getRelabeledCount());
		assertEquals(0, editDistance_move.getValueChangeCount());
	}

	@Test
	public void testRelabel() throws TokenExpectedException, FileNotFoundException, BadTokenException, ParseException, IOException, SGLRException, InterruptedException
	{
		String fname1 = "tests/test-inputs/test-relabel1-001.java";
		String fname2 = "tests/test-inputs/test-relabel2-001.java";
		IStrategoTerm trm1 = parseFile(fname1);
		IStrategoTerm trm2 = parseFile(fname2);
		
		AbstractTreeMatcher treeMatcher = new HeuristicTreeMatcher(false, false, false);
		TreeEditDistance editDistance = new TreeEditDistance();
		editDistance.detectTreeEditActions(trm1, trm2, treeMatcher);
		Assert.assertEquals(48, countMatches(trm2));	
		assertEquals(0, editDistance.getDeletionCount());
		assertEquals(0, editDistance.getInsertionCount());
		assertEquals(0, editDistance.getMovedCount());
		assertEquals(1, editDistance.getRelabeledCount());
		assertEquals(0, editDistance.getValueChangeCount());

		//With support for matching value updates
		AbstractTreeMatcher treeMatcher_move = new HeuristicTreeMatcher(true, false, false);
		TreeEditDistance editDistance2 = new TreeEditDistance();
		IStrategoTerm trm_1 = parseFile(fname1);
		IStrategoTerm trm_2 = parseFile(fname2);
		editDistance2.detectTreeEditActions(trm_1, trm_2, treeMatcher_move);
		//super.topdownPrintMatching(trm_2);
		TreeEditDistance editDistance_move = editDistance2;
		Assert.assertEquals(47, countMatches(trm_2));	
		assertEquals(1, editDistance_move.getDeletionCount());
		assertEquals(1, editDistance_move.getInsertionCount());
		assertEquals(2, editDistance_move.getMovedCount());
		assertEquals(0, editDistance_move.getRelabeledCount());
		assertEquals(0, editDistance_move.getValueChangeCount());
	}


	@Test
	public void testMove001() throws TokenExpectedException, FileNotFoundException, BadTokenException, ParseException, IOException, SGLRException, InterruptedException
	{
		String fname1 = "tests/test-inputs/test-move1-001.java";
		String fname2 = "tests/test-inputs/test-move2-001.java";
		IStrategoTerm trm1 = parseFile(fname1);
		IStrategoTerm trm2 = parseFile(fname2);
		
		AbstractTreeMatcher treeMatcher = new HeuristicTreeMatcher(false, false, true);
		TreeEditDistance editDistance = new TreeEditDistance();
		editDistance.detectTreeEditActions(trm1, trm2, treeMatcher);
		Assert.assertEquals(83, countMatches(trm2));	
		assertEquals(0, editDistance.getDeletionCount());
		assertEquals(0, editDistance.getInsertionCount());
		assertEquals(1, editDistance.getMovedCount());
		assertEquals(0, editDistance.getRelabeledCount());
		assertEquals(0, editDistance.getValueChangeCount());
	}

	
	@Test
	public void testMove002() throws TokenExpectedException, FileNotFoundException, BadTokenException, ParseException, IOException, SGLRException, InterruptedException
	{
		String fname1 = "tests/test-inputs/test-move1-002.java";
		String fname2 = "tests/test-inputs/test-move2-002.java";
		IStrategoTerm trm1 = parseFile(fname1);
		IStrategoTerm trm2 = parseFile(fname2);
		
		AbstractTreeMatcher treeMatcher = new HeuristicTreeMatcher(false, false, true);
		TreeEditDistance editDistance = new TreeEditDistance();
		editDistance.detectTreeEditActions(trm1, trm2, treeMatcher);
		Assert.assertEquals(83, countMatches(trm2));	
		assertEquals(0, editDistance.getDeletionCount());
		assertEquals(0, editDistance.getInsertionCount());
		assertEquals(1, editDistance.getMovedCount());
		assertEquals(0, editDistance.getRelabeledCount());
		assertEquals(0, editDistance.getValueChangeCount());

		IStrategoTerm trm_1 = parseFile(fname1);
		IStrategoTerm trm_2 = parseFile(fname2);
		AbstractTreeMatcher treeMatcher2 = new HeuristicTreeMatcher(false, false, false);
		TreeEditDistance editDistance2 = new TreeEditDistance();
		editDistance2.detectTreeEditActions(trm_1, trm_2, treeMatcher2);
		Assert.assertEquals(67, countMatches(trm_2));	
		assertEquals(16, editDistance2.getDeletionCount());
		assertEquals(16, editDistance2.getInsertionCount());
		assertEquals(0, editDistance2.getMovedCount());
		assertEquals(0, editDistance2.getRelabeledCount());
		assertEquals(0, editDistance2.getValueChangeCount());

	}

	
	@Test
	public void testMove003() throws TokenExpectedException, FileNotFoundException, BadTokenException, ParseException, IOException, SGLRException, InterruptedException
	{
		String fname1 = "tests/test-inputs/test-move1-003.java";
		String fname2 = "tests/test-inputs/test-move2-003.java";
		IStrategoTerm trm1 = parseFile(fname1);
		IStrategoTerm trm2 = parseFile(fname2);
		
		AbstractTreeMatcher treeMatcher = new HeuristicTreeMatcher(false, false, false);
		TreeEditDistance editDistance = new TreeEditDistance();
		editDistance.detectTreeEditActions(trm1, trm2, treeMatcher);
		Assert.assertEquals(76, countMatches(trm2));	
		assertEquals(7, editDistance.getDeletionCount());
		assertEquals(23, editDistance.getInsertionCount());
		assertEquals(3, editDistance.getMovedCount());
		assertEquals(0, editDistance.getRelabeledCount());
		assertEquals(1, editDistance.getValueChangeCount());

		//With support for matching moved terms
		AbstractTreeMatcher treeMatcher_move = new HeuristicTreeMatcher(false, false, true);
		TreeEditDistance editDistance2 = new TreeEditDistance();
		IStrategoTerm trm_1 = parseFile(fname1);
		IStrategoTerm trm_2 = parseFile(fname2);
		editDistance2.detectTreeEditActions(trm_1, trm_2, treeMatcher_move);
		TreeEditDistance editDistance_move = editDistance2;
		Assert.assertEquals(76, countMatches(trm_2));	
		assertEquals(7, editDistance_move.getDeletionCount());
		assertEquals(23, editDistance_move.getInsertionCount());
		assertEquals(3, editDistance_move.getMovedCount());
		assertEquals(0, editDistance_move.getRelabeledCount());
		assertEquals(1, editDistance_move.getValueChangeCount());
	}


	@Test
	public void testMove004() throws TokenExpectedException, FileNotFoundException, BadTokenException, ParseException, IOException, SGLRException, InterruptedException
	{
		String fname1 = "tests/test-inputs/test-move1-004.java";
		String fname2 = "tests/test-inputs/test-move2-004.java";
		IStrategoTerm trm1 = parseFile(fname1);
		IStrategoTerm trm2 = parseFile(fname2);
		
		AbstractTreeMatcher treeMatcher = new HeuristicTreeMatcher(false, false, false);
		TreeEditDistance editDistance = new TreeEditDistance();
		editDistance.detectTreeEditActions(trm1, trm2, treeMatcher);
		Assert.assertEquals(121, countMatches(trm2));	
		assertEquals(0, editDistance.getDeletionCount());
		assertEquals(0, editDistance.getInsertionCount());
		assertEquals(4, editDistance.getMovedCount());
		assertEquals(0, editDistance.getRelabeledCount());
		assertEquals(0, editDistance.getValueChangeCount());
	}

	@Test
	public void testMove005() throws TokenExpectedException, FileNotFoundException, BadTokenException, ParseException, IOException, SGLRException, InterruptedException
	{
		//fail();//TODO: check results manually
		String fname1 = "tests/test-inputs/test-move1-005.java";
		String fname2 = "tests/test-inputs/test-move2-005.java";

		IStrategoTerm trm1 = parseFile(fname1);
		IStrategoTerm trm2 = parseFile(fname2);		
		AbstractTreeMatcher treeMatcher = new HeuristicTreeMatcher(false, false, false);
		TreeEditDistance editDistance = new TreeEditDistance();
		editDistance.detectTreeEditActions(trm1, trm2, treeMatcher);
		Assert.assertEquals(79, countMatches(trm2));	
		assertEquals(12, editDistance.getDeletionCount());
		assertEquals(39, editDistance.getInsertionCount());
		assertEquals(1, editDistance.getMovedCount());
		assertEquals(0, editDistance.getRelabeledCount());
		assertEquals(0, editDistance.getValueChangeCount());

		//With support for matching moved terms
		AbstractTreeMatcher treeMatcher_move = new HeuristicTreeMatcher(false, false, true);
		TreeEditDistance editDistance2 = new TreeEditDistance();
		IStrategoTerm trm_1 = parseFile(fname1);
		IStrategoTerm trm_2 = parseFile(fname2);
		editDistance2.detectTreeEditActions(trm_1, trm_2, treeMatcher_move);
		TreeEditDistance editDistance_move = editDistance2;
		Assert.assertEquals(91, countMatches(trm_2));	
		assertEquals(0, editDistance_move.getDeletionCount());
		assertEquals(27, editDistance_move.getInsertionCount());
		assertEquals(1, editDistance_move.getMovedCount());
		assertEquals(0, editDistance_move.getRelabeledCount());
		assertEquals(0, editDistance_move.getValueChangeCount());
	}
}
