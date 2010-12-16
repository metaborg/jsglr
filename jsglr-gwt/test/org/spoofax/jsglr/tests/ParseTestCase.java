/*
 * Created on 13.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 *
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr.tests;

import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.client.ParserException;
import org.spoofax.jsglr.client.SGLR;
import org.spoofax.jsglr.shared.RemoteParseTableService;
import org.spoofax.jsglr.shared.RemoteParseTableServiceAsync;
import org.spoofax.jsglr.shared.Tools;
import org.spoofax.jsglr.shared.terms.ATerm;
import org.spoofax.jsglr.shared.terms.ATermFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class ParseTestCase extends GWTTestCase {

	protected SGLR sglr;
    protected String suffix;

    // shared by all tests
    static final ATermFactory pf = new ATermFactory();
    RemoteParseTableServiceAsync parseTableService = GWT.create(RemoteParseTableService.class);

    public void gwtSetUp(String grammar, String suffix) throws ParserException, InvalidParseTableException {
        this.suffix = suffix;
        Tools.setDebug(false);
        Tools.setLogging(false);
        parseTableService.fetchParseTable("tests/grammars/" + grammar + ".tbl",
        		new AsyncCallback<ATerm>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(ATerm result) {
				        try {
							sglr = new SGLR(pf, new ParseTable(result));
						} catch (InvalidParseTableException e) {
							throw new RuntimeException(e);
						}
					}
				});

    }

    @Override
	protected void gwtTearDown() throws Exception {
        super.gwtTearDown();

        sglr.clear();
    }

    final static boolean doCompare = true;
    public void doParseTest(final String s) {

    	parseTableService.fetchText("tests/data/" + s + "." + suffix,
    			new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						fail();
					}

					@Override
					public void onSuccess(String result) {
				        long parseTime = System.nanoTime();
				        ATerm parsed = null;
				        try {
							parsed = sglr.parse(result);
						} catch (Exception e) {
							fail(e.toString());
						}
				        parseTime = System.nanoTime() - parseTime;
				        System.out.println("Parsing " + s + " took " + parseTime/1000/1000 + " millis.");
				        assertNotNull(parsed);
				        if(doCompare)
				        	doCompare(s, parsed);
					}
    		
    	});
    	
    }
    
    private void doCompare(String s, final ATerm parsed) {
    	parseTableService.readTermFromFile("tests/data/" + s + ".trm", new AsyncCallback<ATerm>() {

			@Override
			public void onFailure(Throwable caught) {
				fail();
			}

			@Override
			public void onSuccess(ATerm loaded) {
	            assertNotNull(loaded);

	            if(parsed.match(loaded) == false) {
	            	fail();
	            }
	        }
		});

    }

    @Override
    public String getModuleName() {
    	return "org.spoofax.JsglrGWT";
    }
}
