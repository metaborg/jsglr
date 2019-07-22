package org.spoofax.jsglr2.integrationtest.grammars;

import org.junit.Test;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class NestedListsTest extends BaseTestWithSdf3ParseTables {

    public NestedListsTest() {
        super("nestedlists.sdf3");
    }

    @Test public void testX() throws ParseError {
        testSuccessByExpansions("x", "X()");
    }

    @Test public void testMX() throws ParseError {
        testSuccessByExpansions("lx", "L([X()])");
    }

    @Test public void testMXX() throws ParseError {
        testSuccessByExpansions("lxx", "L([X(),X()])");
    }

    @Test public void testMMXX() throws ParseError {
        String list1 = "[L([X()]),X()]"; // One in inner list, one in outer list
        String list2 = "[L([X(),X()])]"; // Both in inner list

        testSuccessByExpansions("llxx", "L(amb([" + list1 + "," + list2 + "]))");
    }

    @Test public void testMMXXX() throws ParseError {
        String list1 = "[L([X()]),X(),X()]"; // One in inner list, two in outer list
        String list2 = "[L([X(),X()]),X()]"; // Two in inner list, one in outer list
        String list3 = "[L([X(),X(),X()])]"; // All in inner list

        testSuccessByExpansions("llxxx", "L(amb([" + list1 + "," + list2 + "," + list3 + "]))");
    }

}