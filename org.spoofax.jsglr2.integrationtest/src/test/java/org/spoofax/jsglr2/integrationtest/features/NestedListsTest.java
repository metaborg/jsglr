package org.spoofax.jsglr2.integrationtest.features;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

import java.util.stream.Stream;

public class NestedListsTest extends BaseTestWithSdf3ParseTables {

    public NestedListsTest() {
        super("nestedlists.sdf3");
    }

    @TestFactory public Stream<DynamicTest> testX() throws ParseError {
        return testSuccessByExpansions("x", "X()");
    }

    @TestFactory public Stream<DynamicTest> testMX() throws ParseError {
        return testSuccessByExpansions("lx", "L([X()])");
    }

    @TestFactory public Stream<DynamicTest> testMXX() throws ParseError {
        return testSuccessByExpansions("lxx", "L([X(),X()])");
    }

    @TestFactory public Stream<DynamicTest> testMMXX() throws ParseError {
        String list1 = "[L([X()]),X()]"; // One in inner list, one in outer list
        String list2 = "[L([X(),X()])]"; // Both in inner list

        return testSuccessByExpansions("llxx", "L(amb([" + list1 + "," + list2 + "]))");
    }

    @TestFactory public Stream<DynamicTest> testMMXXX() throws ParseError {
        String list1 = "[L([X()]),X(),X()]"; // One in inner list, two in outer list
        String list2 = "[L([X(),X()]),X()]"; // Two in inner list, one in outer list
        String list3 = "[L([X(),X(),X()])]"; // All in inner list

        return testSuccessByExpansions("llxxx", "L(amb([" + list1 + "," + list2 + "," + list3 + "]))");
    }

}