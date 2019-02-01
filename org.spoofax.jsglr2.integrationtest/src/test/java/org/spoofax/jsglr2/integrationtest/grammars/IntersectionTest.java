package org.spoofax.jsglr2.integrationtest.grammars;

import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;

public class IntersectionTest extends BaseTestWithSdf3ParseTables {

    public IntersectionTest() {
        super("intersection.sdf3");
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