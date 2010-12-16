/*
 * Created on 13.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 *
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr.tests;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import junit.framework.TestCase;

import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.client.ParserException;
import org.spoofax.jsglr.client.SGLR;
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
		String fn = "tests/grammars/" + grammar + ".tbl";

		ATerm result = pf.parseFromString(loadFileAsString(fn));
		sglr = new SGLR(pf, new ParseTable(result));
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

	private String loadFileAsString(String fn) {
		char[] cbuf = new char[1024*1024*12];
		try {
			BufferedReader br = new BufferedReader(new FileReader(fn));
			int len = br.read(cbuf);
			return new String(cbuf, 0, len);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void tearDown() throws Exception {
		//super.gwtTearDown();

		sglr.clear();
	}

	final static boolean doCompare = true;
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
		String result = loadFileAsString("tests/data/" + s + "." + suffix);
		assertNotNull("Data file is missing", result);
		long parseTime = System.nanoTime();
		ATerm parsed = null;
		try {
			parsed = sglr.parse(result);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
		parseTime = System.nanoTime() - parseTime;
		System.out.println("Parsing " + s + " took " + parseTime/1000/1000 + " millis.");
		assertNotNull(parsed);
		if(doCompare)
			doCompare(s, parsed);
		//			}
		//
		//		});

	}

	private void doCompare(String s, final ATerm parsed) {
		//parseTableService.readTermFromFile("tests/data/" + s + ".trm", new AsyncCallback<ATerm>() {
		String loaded = loadFileAsString("tests/data/" + s + ".trm");

		//			@Override
		//			public void onFailure(Throwable caught) {
		//				fail();
		//			}
		//
		//			@Override
		//			public void onSuccess(ATerm loaded) {
		assertNotNull(loaded);

		if(parsed.match(loaded) == null) {
			fail();
		}
		//			}
		//		});

	}

	public String getModuleName() {
		return "org.spoofax.JsglrGWT";
	}
}
