/*
 * Created on 13.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 *
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr.tests;

import junit.framework.TestCase;

import org.spoofax.jsglr.FileTools;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.client.ParserException;
import org.spoofax.jsglr.client.PathListPool;
import org.spoofax.jsglr.client.PooledPathList;
import org.spoofax.jsglr.client.SGLR;
import org.spoofax.jsglr.client.imploder.Token;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr.shared.Tools;
import org.spoofax.jsglr.shared.terms.ATerm;
import org.spoofax.jsglr.shared.terms.ATermFactory;

public abstract class ParseTestCase extends TestCase {

	protected SGLR sglr;
	protected String suffix;

	// shared by all tests
	static final ATermFactory pf = new ATermFactory();
	//RemoteParseTableServiceAsync parseTableService = GWT.create(RemoteParseTableService.class);

	@Override
	protected void setUp() throws Exception {
		gwtSetUp();
	}

	protected abstract void gwtSetUp() throws Exception;

	public void gwtSetUp(String grammar, String suffix) throws ParserException, InvalidParseTableException {
		this.suffix = suffix;
		Tools.setDebug(false);
		Tools.setLogging(false);
		final String fn = "tests/grammars/" + grammar + ".tbl";

		final ATerm result = pf.parseFromString(FileTools.loadFileAsString(fn));
		final ParseTable pt = new ParseTable(result);
		sglr = new SGLR(pf, pt);
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

	}


	@Override
	protected void tearDown() throws Exception {
		//super.gwtTearDown();

		sglr.clear();
	}

	boolean doCompare = true;
	public void doParseTest(final String s) {

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
		final String result = FileTools.loadFileAsString("tests/data/" + s + "." + suffix);
		assertNotNull("Data file is missing", result);
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
			System.out.println(parsed);
			if (parsed.getLeftToken() != null) {
				System.out.println(((Token) parsed.getLeftToken()).getTokenizer());
			}
		}

		System.out.println(PathListPool.cacheMisses);
		System.out.println(PooledPathList.maxRemembered);
		System.out.println(PooledPathList.maxAllocated);
	}

	private void doCompare(String s, final ATerm parsed) {
		//parseTableService.readTermFromFile("tests/data/" + s + ".trm", new AsyncCallback<ATerm>() {
		final String x = FileTools.loadFileAsString("tests/data/" + s + ".trm");
		final ATerm wanted = parsed.getFactory().parse(x);
		//			@Override
		//			public void onFailure(Throwable caught) {
		//				fail();
		//			}
		//
		//			@Override
		//			public void onSuccess(ATerm loaded) {
		assertNotNull(x);

		System.out.println(parsed);
		System.out.println(wanted);
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
