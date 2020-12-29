package org.spoofax.jsglr2.integrationtest;

public class ParseTableConsistencyTest extends BaseTestWithRecoverySdf3ParseTables {

    public ParseTableConsistencyTest() {
        // Tests are triggered in BaseTestWithSdf3ParseTables::testParseTableConsistency
        super("parsetable-consistency.sdf3", true, true);
    }

}
