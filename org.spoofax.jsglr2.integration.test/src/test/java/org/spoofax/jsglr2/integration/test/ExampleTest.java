package org.spoofax.jsglr2.integration.test;

import org.junit.Test;

public class ExampleTest extends BaseTestWithSpoofaxCoreSdf3 {

    public ExampleTest() {
        super("test.sdf3");
    }
    
    @Test
    public void test() throws Exception {
        testSuccessByExpansions("1+2", "Add(Int(\"1\"),Int(\"2\"))");
    }

}