/*
 * Created on 13.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 *
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr.tests;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.client.ParserException;
import org.spoofax.jsglr.client.SGLR;
import org.spoofax.jsglr.shared.RemoteParseTableService;
import org.spoofax.jsglr.shared.RemoteParseTableServiceAsync;
import org.spoofax.jsglr.shared.SGLRException;
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
        parseTableService.findParseTable("tests/grammars/" + grammar + ".tbl",
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
    public void doParseTest(String s) throws FileNotFoundException, IOException {

        long parseTime = System.nanoTime();
        ATerm parsed = null;
        try {
             parsed = sglr.parse(new FileInputStream("tests/data/" + s + "." + suffix));
        } catch(SGLRException e) {
            e.printStackTrace();
        }
        parseTime = System.nanoTime() - parseTime;
        Tools.logger("Parsing ", s, " took " + parseTime/1000/1000, " millis.");
        System.out.println("Parsing " + s + " took " + parseTime/1000/1000 + " millis.");
        assertNotNull(parsed);

        // When running performance this is in the way due to the extra garbage created.
        if(doCompare) {
            ATerm loaded = sglr.getFactory().readFromFile("tests/data/" + s + ".trm");

            assertNotNull(loaded);

            if(parsed.match(loaded) == null) {
                PrintWriter printWriter = new PrintWriter(new FileOutputStream("tests/data/" + s + ".trm.parsed"));
                printWriter.print(parsed.toString());
                printWriter.flush();
                System.err.println("Saw    : " + parsed);
                System.err.println("Wanted : " + loaded);
                System.err.println("Trying to compare to the alternative file.");

                loaded = sglr.getFactory().readFromFile("tests/data/" + s + "-bis.trm");

                assertNotNull(loaded);

                if(parsed.match(loaded) == null) {
                    printWriter = new PrintWriter(new FileOutputStream("tests/data/" + s + ".trm.parsed"));
                    printWriter.print(parsed.toString());
                    printWriter.flush();
                    System.err.println("Saw    : " + parsed);
                    System.err.println("Wanted : " + loaded);
                    assertTrue(false);
                }
            }
        }
    }

    @Override
    public String getModuleName() {
    	return "org.spoofax.JsglrGWT";
    }
}
