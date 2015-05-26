/*
 * Created on 13.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 *
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr.tests;

import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getLeftToken;
import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getSort;
import static org.spoofax.jsglr.client.incremental.CommentDamageExpander.C_STYLE;

import java.io.IOException;
import java.util.Arrays;

import junit.framework.TestCase;

import org.spoofax.interpreter.terms.ISimpleTerm;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.jsglr.client.Asfix2TreeBuilder;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.client.ParserException;
import org.spoofax.jsglr.client.PooledPathList;
import org.spoofax.jsglr.client.SGLR;
import org.spoofax.jsglr.client.imploder.TermTreeFactory;
import org.spoofax.jsglr.client.imploder.TreeBuilder;
import org.spoofax.jsglr.client.incremental.IncrementalSGLR;
import org.spoofax.jsglr.client.incremental.IncrementalSortSet;
import org.spoofax.jsglr.io.FileTools;
import org.spoofax.jsglr.io.ParseTableManager;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.terms.ParseError;
import org.spoofax.terms.TermFactory;
import org.spoofax.terms.attachments.ParentTermFactory;
import org.spoofax.terms.io.binary.TermReader;

public abstract class ParseTestCase extends TestCase {
	
	private static final ParseTableManager parseTables = new ParseTableManager();

	protected SGLR sglr;
	
	protected String suffix;
	
	protected ParseTable table;

	protected IncrementalSGLR<IStrategoTerm> incrementalSGLR;

	// shared by all tests
	static final TermFactory pf = new TermFactory();
	//RemoteParseTableServiceAsync parseTableService = GWT.create(RemoteParseTableService.class);

	@Override
	protected final void setUp() throws Exception {
		gwtSetUp();
	}

	protected void gwtSetUp() throws Exception {
		// no default setup
	}
	
	public void gwtSetUp(String grammar, String suffix, String... incrementalSorts) throws ParserException, InvalidParseTableException {
		this.suffix = suffix;
		final String fn = "tests/grammars/" + grammar + ".tbl";

		try {
			table = parseTables.loadFromFile(fn);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		sglr = new SGLR(new Asfix2TreeBuilder(table.getFactory()), table);
		//        parseTableService.fetchParseTable("tests/grammars/" + grammar + ".tbl",
		//        		new AsyncCallback<IStrategoTerm>() {
		//
		//					@Override
		//					public void onFailure(Throwable caught) {
		//						// TODO Auto-generated method stub
		//
		//					}
		//
		//					@Override
		//					public void onSuccess(IStrategoTerm result) {
		//				        try {
		//							sglr = new SGLR(pf, new ParseTable(result));
		//						} catch (InvalidParseTableException e) {
		//							throw new RuntimeException(e);
		//						}
		//					}
		//				});

		if (incrementalSorts.length > 0) {
			ITermFactory termFactory = new ParentTermFactory(sglr.getParseTable().getFactory());
			TermTreeFactory factory = new TermTreeFactory(termFactory);
			TreeBuilder builder = new TreeBuilder(factory);
			sglr.setTreeBuilder(builder);
			IncrementalSortSet sorts = IncrementalSortSet.create(table, true, incrementalSorts);
			incrementalSGLR = new IncrementalSGLR<IStrategoTerm>(sglr, C_STYLE, factory, sorts);
	        IncrementalSGLR.DEBUG = true;
		}
	}

	private IStrategoTerm tryReadTermFromFile(String fn) throws ParseError {
		try {
			return new TermReader(pf).parseFromFile(fn);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	protected void tearDown() throws Exception {
		//super.gwtTearDown();

		//sglr.clear();
	}

	boolean doCompare = true;
	public IStrategoTerm doParseTest(final String s) {

		//		parseTableService.fetchText("tests/data/" + s + "." + suffix,
		//				new AsyncCallback<String>() {
		//
		//			@Override
		//			public void onFailure(Throwable caught) {
		//				fail();
		//			}
		//
		//			@Override
		//			public void onSuccess(String result) {
		final String result = loadAsString(s);
		assertNotNull("Data file is missing: " + s, result);
		long parseTime = System.nanoTime();
		IStrategoTerm parsed = null;
		try {
			parsed = (IStrategoTerm) sglr.parse(result, null, null).output;
		} catch (SGLRException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		parseTime = System.nanoTime() - parseTime;
		System.out.println("Parsing " + s + " took " + parseTime/1000/1000 + " millis.");
		assertNotNull(parsed);
		if (doCompare) {
			doCompare(s, parsed);
		} else {
			if (getLeftToken(parsed) != null)
				System.out.println(getLeftToken(parsed).getTokenizer());
			System.out.println(toCompactString(parsed));
		}

//		System.out.println(PathListPool.asyncCacheMisses);
		System.out.println(PooledPathList.maxRemembered);
		System.out.println(PooledPathList.maxAllocated);
		return parsed;
	}
	
	public IStrategoTerm doParseIncrementalTest(IStrategoTerm oldTree, String newFile, String... repairTypes) throws Exception {
		String contents = loadAsString(newFile);
		assertNotNull(contents);
		long parseTime = System.nanoTime();
		System.out.println("------------------------");
		System.out.println("Parsing " + newFile);
		incrementalSGLR.setLastAst(oldTree);
    	IStrategoTerm newTree = incrementalSGLR.parseIncremental(contents, newFile, null);
		parseTime = System.nanoTime() - parseTime;
		System.out.println("Incremental parsing " + newFile + " took " + parseTime/1000/1000 + " millis" + (IncrementalSGLR.DEBUG ? " including debug printing" : ""));
		String extension =
			sglr.getTreeBuilder() instanceof TreeBuilder ? ".itrm" : ".trm";
		if (doCompare) {
			final IStrategoTerm wanted = tryReadTermFromFile("tests/data/" + newFile + extension);
			System.out.println(toCompactString(newTree));
			System.out.println(toCompactString(wanted));
	    	if (!newTree.match(wanted))
	    		fail("Incremental result not same as non-incremental result");
	    	doTokenStreamEqualityTest(oldTree, newTree);
		} else {
			System.out.println(toCompactString(newTree));
		}
		if (repairTypes.length > 0) {
			for (ISimpleTerm repaired : incrementalSGLR.getLastReconstructedNodes()) {
				if (!Arrays.asList(repairTypes).contains(getSort(repaired)))
					fail("Unexpected repaired tree node of type " + getSort(repaired) + ": " + repaired);
			}
		}
    	return newTree;
	}
	
	private void doTokenStreamEqualityTest(IStrategoTerm oldTree, IStrategoTerm newTree) {
		// Actual token equality test is now performed
		// using assertions in IncrementalTreeBuilder
		String tokens = getLeftToken(newTree).getTokenizer().toString();
		if (tokens.length() > 300)
			tokens = tokens.substring(0, 300) + "...";
		System.out.println(tokens);
	}

	protected String loadAsString(final String testFile) {
		return FileTools.tryLoadFileAsString("tests/data/" + testFile + "." + suffix);
	}

	private void doCompare(String s, final IStrategoTerm parsed) {
		//parseTableService.readTermFromFile("tests/data/" + s + ".trm", new AsyncCallback<IStrategoTerm>() {
		String extension =
			sglr.getTreeBuilder() instanceof TreeBuilder ? ".itrm" : ".trm";
		IStrategoTerm wanted;
		try {
			long parseTime = System.nanoTime();
			wanted = new TermReader(pf).parseFromFile("tests/data/" + s + extension);
			parseTime = System.nanoTime() - parseTime;
			System.out.println("reading term " + s + extension + " took " + parseTime/1000/1000 + " millis");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		//			@Override
		//			public void onFailure(Throwable caught) {
		//				fail();
		//			}
		//
		//			@Override
		//			public void onSuccess(IStrategoTerm loaded) {
		assertNotNull(wanted);

		System.out.println(toCompactString(parsed));
		System.out.println(toCompactString(wanted));
		if(!parsed.match(wanted)) {
			fail();
		}
		//			}
		//		});

	}
	
	private static String toCompactString(IStrategoTerm term) {
		return term.toString(8);
	}

	public String getModuleName() {
		return "org.spoofax.JsglrGWT";
	}
}
