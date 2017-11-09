package org.spoofax.jsglr2.tests.parser;

import java.io.IOException;
import java.net.URISyntaxException;

import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.tests.util.BaseTest;
import org.spoofax.jsglr2.util.WithGrammar;
import org.spoofax.terms.ParseError;

public class IntersectionTest extends BaseTest implements WithGrammar {

    public IntersectionTest() throws ParseError, ParseTableReadException, IOException, InvalidParseTableException,
        InterruptedException, URISyntaxException {
        setupParseTableFromDefFile("intersection");
    }

    /*
     * TODO: implement stack priorities during reducing to fix intersection problem (see P9707 Section 8.4) to make this
     * test pass
     * 
     * @Test public void testOneNotInIntersection() throws ParseError, ParseTableReadException, IOException {
     * testParseFailure("1"); }
     * 
     * @Test public void testTwoInIntersection() throws ParseError, ParseTableReadException, IOException {
     * testParseSuccessByAstString("2", "Two"); }
     * 
     * @Test public void testThreeNotInIntersecton() throws ParseError, ParseTableReadException, IOException {
     * testParseFailure("3"); }
     */

}