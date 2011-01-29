/*
 * Created on 13.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 *
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr.tests;

import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getLeftToken;
import static org.spoofax.jsglr.client.incremental.CommentDamageExpander.C_STYLE;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.client.ParserException;
import org.spoofax.jsglr.client.PathListPool;
import org.spoofax.jsglr.client.PooledPathList;
import org.spoofax.jsglr.client.SGLR;
import org.spoofax.jsglr.client.imploder.TermTreeFactory;
import org.spoofax.jsglr.client.imploder.TreeBuilder;
import org.spoofax.jsglr.client.incremental.IncrementalSGLR;
import org.spoofax.jsglr.io.FileTools;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr.shared.Tools;
import org.spoofax.terms.ParseError;
import org.spoofax.terms.TermFactory;
import org.spoofax.terms.io.binary.TermReader;

public abstract class ParseTestCase extends TestCase {

	protected SGLR sglr;
	
	protected String suffix;
	
	protected ParseTable table;

	protected IncrementalSGLR<IStrategoTerm> incrementalSGLR;

	// shared by all tests
	static final TermFactory pf = new TermFactory();
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

		final IStrategoTerm result = tryReadTermFromFile(fn);
		table = new ParseTable(result, pf);
		sglr = new SGLR(pf, table);
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
			TermTreeFactory factory = new TermTreeFactory(sglr.getParseTable().getFactory());
			TreeBuilder builder = new TreeBuilder(factory);
			sglr.setTreeBuilder(builder);
			//Set<String> sorts = new SortAnalyzer(table).getInjectionsTo(incrementalSorts);
			Set<String> sorts = new HashSet<String>();
	    	for (String sort : incrementalSorts)
	    		sorts.add(sort);
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

		sglr.clear();
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
			parsed = (IStrategoTerm) sglr.parse(result, null, null);
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
			if (getLeftToken(parsed) != null)
				System.out.println(getLeftToken(parsed).getTokenizer());
			System.out.println(toCompactString(parsed));
		}

		System.out.println(PathListPool.cacheMisses);
		System.out.println(PooledPathList.maxRemembered);
		System.out.println(PooledPathList.maxAllocated);
		return parsed;
	}
	
	public IStrategoTerm doParseIncrementalTest(IStrategoTerm oldTree, String newFile) throws Exception {
		String contents = loadAsString(newFile);
		assertNotNull(contents);
		long parseTime = System.nanoTime();
		System.out.println("------------------------");
		System.out.println("Parsing " + newFile);
    	IStrategoTerm newTree = incrementalSGLR.parseIncremental(contents, newFile, null, oldTree);
		parseTime = System.nanoTime() - parseTime;
		System.out.println("Incremental parsing " + newFile + " took " + parseTime/1000/1000 + " millis" + (IncrementalSGLR.DEBUG ? " including debug printing" : ""));
		String extension =
			table.getTreeBuilder() instanceof TreeBuilder ? ".itrm" : ".trm";
		if (doCompare) {
			final IStrategoTerm wanted = tryReadTermFromFile("tests/data/" + newFile + extension);
			System.out.println(toCompactString(newTree));
			System.out.println(toCompactString(wanted));
	    	if (!newTree.match(wanted))
	    		fail();
	    	doTokenStreamEqualityTest(oldTree, newTree);
		} else {
			System.out.println(toCompactString(newTree));
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
			table.getTreeBuilder() instanceof TreeBuilder ? ".itrm" : ".trm";
		final String x = FileTools.tryLoadFileAsString("tests/data/" + s + extension);
		final IStrategoTerm wanted = pf.parseFromString(x);
		//			@Override
		//			public void onFailure(Throwable caught) {
		//				fail();
		//			}
		//
		//			@Override
		//			public void onSuccess(IStrategoTerm loaded) {
		assertNotNull(x);

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
