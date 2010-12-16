/*
 * Created on 13.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 *
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr.tests;

import static org.spoofax.jsglr.client.incremental.CommentDamageExpander.C_STYLE;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.spoofax.jsglr.FileTools;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.client.ParserException;
import org.spoofax.jsglr.client.PathListPool;
import org.spoofax.jsglr.client.PooledPathList;
import org.spoofax.jsglr.client.SGLR;
import org.spoofax.jsglr.client.imploder.ATermTreeFactory;
import org.spoofax.jsglr.client.imploder.TreeBuilder;
import org.spoofax.jsglr.client.incremental.IncrementalSGLR;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr.shared.Tools;
import org.spoofax.jsglr.shared.terms.ATerm;
import org.spoofax.jsglr.shared.terms.ATermFactory;

public abstract class ParseTestCase extends TestCase {

	protected SGLR sglr;
	
	protected String suffix;
	
	protected ParseTable table;

	protected IncrementalSGLR<ATerm> incrementalSGLR;

	// shared by all tests
	static final ATermFactory pf = new ATermFactory();
	//RemoteParseTableServiceAsync parseTableService = GWT.create(RemoteParseTableService.class);

	@Override
	protected void setUp() throws Exception {
		gwtSetUp();
	}

	protected abstract void gwtSetUp() throws Exception;
	
	public void gwtSetUp(String grammar, String suffix, String... incrementalSorts) throws ParserException, InvalidParseTableException {
		this.suffix = suffix;
		Tools.setDebug(false);
		Tools.setLogging(false);
		final String fn = "tests/grammars/" + grammar + ".tbl";

		final ATerm result = pf.parseFromString(FileTools.loadFileAsString(fn));
		table = new ParseTable(result);
		sglr = new SGLR(pf, table);
		//        parseTableService.fetchParseTable("tests/grammars/" + grammar + ".tbl",
		//        		new AsyncCallback<ATerm>() {
		//
		//					@Override
		//					public void onFailure(Throwable caught) {
		//						// TODO Auto-generated method stub
		//
		//					}
		//
		//					@Override
		//					public void onSuccess(ATerm result) {
		//				        try {
		//							sglr = new SGLR(pf, new ParseTable(result));
		//						} catch (InvalidParseTableException e) {
		//							throw new RuntimeException(e);
		//						}
		//					}
		//				});

		if (incrementalSorts.length > 0) {
			ATermTreeFactory factory = new ATermTreeFactory(sglr.getFactory());
			TreeBuilder builder = new TreeBuilder(factory);
			sglr.setTreeBuilder(builder);
			Set<String> sorts = new HashSet<String>();
	    	for (String sort : incrementalSorts)
	    		sorts.add(sort);
	    	incrementalSGLR = new IncrementalSGLR<ATerm>(sglr, C_STYLE, factory, sorts, false);
	        IncrementalSGLR.DEBUG = true;
		}
	}


	@Override
	protected void tearDown() throws Exception {
		//super.gwtTearDown();

		sglr.clear();
	}

	boolean doCompare = true;
	public ATerm doParseTest(final String s) {

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
		ATerm parsed = null;
		try {
			parsed = (ATerm) sglr.parse(result);
		} catch (SGLRException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		parseTime = System.nanoTime() - parseTime;
		System.out.println("Parsing " + s + " took " + parseTime/1000/1000 + " millis.");
		assertNotNull(parsed);
		if (doCompare) {
			doCompare(s, parsed);
		} else {
			if (parsed.getLeftToken() != null)
				System.out.println(parsed.getLeftToken().getTokenizer());
			System.out.println(parsed.toString(8));
		}

		System.out.println(PathListPool.cacheMisses);
		System.out.println(PooledPathList.maxRemembered);
		System.out.println(PooledPathList.maxAllocated);
		return parsed;
	}
	
	public ATerm doParseIncrementalTest(ATerm oldTree, String newFile) throws Exception {
		String contents = loadAsString(newFile);
		assertNotNull(contents);
		long parseTime = System.nanoTime();
		System.out.println("------------------------");
		System.out.println("Parsing " + newFile);
    	ATerm newTree = (ATerm) incrementalSGLR.parseIncremental(contents, newFile, null, oldTree);
		parseTime = System.nanoTime() - parseTime;
		System.out.println("Incremental parsing " + newFile + " took " + parseTime/1000/1000 + " millis" + (IncrementalSGLR.DEBUG ? " including debug printing" : ""));
		String extension =
			table.getTreeBuilder() instanceof TreeBuilder ? ".itrm" : ".trm";
		if (doCompare) {
			final String x = FileTools.loadFileAsString("tests/data/" + newFile + extension);
			assertNotNull("Data file is missing: " + newFile + extension, x);
			final ATerm wanted = newTree.getFactory().parse(x);
			System.out.println(newTree.toString(8));
			System.out.println(wanted.toString(8));
	    	if (!newTree.simpleMatch(wanted))
	    		fail();
	    	doTokenStreamEqualityTest(oldTree, newTree);
		} else {
			System.out.println(newTree.toString(8));
		}
    	return newTree;
	}
	
	private void doTokenStreamEqualityTest(ATerm oldTree, ATerm newTree) {
		// Actual token equality test is now performed
		// using assertions in IncrementalTreeBuilder
		String tokens = newTree.getLeftToken().getTokenizer().toString();
		if (tokens.length() > 300)
			tokens = tokens.substring(0, 300) + "...";
		System.out.println(tokens);
	}

	protected String loadAsString(final String testFile) {
		return FileTools.loadFileAsString("tests/data/" + testFile + "." + suffix);
	}

	private void doCompare(String s, final ATerm parsed) {
		//parseTableService.readTermFromFile("tests/data/" + s + ".trm", new AsyncCallback<ATerm>() {
		String extension =
			table.getTreeBuilder() instanceof TreeBuilder ? ".itrm" : ".trm";
		final String x = FileTools.loadFileAsString("tests/data/" + s + extension);
		final ATerm wanted = parsed.getFactory().parse(x);
		//			@Override
		//			public void onFailure(Throwable caught) {
		//				fail();
		//			}
		//
		//			@Override
		//			public void onSuccess(ATerm loaded) {
		assertNotNull(x);

		System.out.println(parsed.toString(8));
		System.out.println(wanted.toString(8));
		if(!parsed.simpleMatch(wanted)) {
			fail();
		}
		//			}
		//		});

	}

	public String getModuleName() {
		return "org.spoofax.JsglrGWT";
	}
}
