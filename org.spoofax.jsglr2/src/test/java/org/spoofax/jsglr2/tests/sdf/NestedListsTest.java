package org.spoofax.jsglr2.tests.sdf;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.tests.util.BaseTest;
import org.spoofax.jsglr2.util.WithGrammar;
import org.spoofax.jsglr2.util.WithJSGLR1;
import org.spoofax.terms.ParseError;

public class NestedListsTest extends BaseTest implements WithJSGLR1, WithGrammar {

    public NestedListsTest() throws ParseError, ParseTableReadException, IOException, InvalidParseTableException,
        InterruptedException, URISyntaxException {
        setupParseTableFromDefFile("nested-lists");
    }

    @Test
    public void testX() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("x", "X()");
    }

    @Test
    public void testMX() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("m x", "M([X()])");
    }

    @Test
    public void testMXX() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("m x,x", "M([X(),X()])");
    }

    @Test
    public void testMMXX() throws ParseError, ParseTableReadException, IOException {
        String list1 = "[M([X()]),X()]"; // One in inner list, one in outer list
        String list2 = "[M([X(),X()])]"; // Both in inner list
        
        testSuccessByExpansions("m m x,x", "M(amb([" + list1 + "," + list2 + "]))");
    }

    @Test
    public void testMMXXX1() throws ParseError, ParseTableReadException, IOException {
        String list1 = "[M([X()]),X(),X()]"; // One in inner list, two in outer list
        String list2 = "[M([X(),X()]),X()]"; // Two in inner list, one in outer list
        String list3 = "[M([X(),X(),X()])]"; // All in inner list 
        
        testSuccessByExpansions("m m x,x,x", "M(amb([" + list1 + "," + list2 + "," + list3 + "]))");
    }

}