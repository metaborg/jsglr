package org.spoofax.jsglr2.testset;

import org.spoofax.jsglr2.testset.testinput.TestInput;

public class TestSetWithParseTable<ContentType, Input extends TestInput<ContentType>>
    extends TestSet<ContentType, Input> {

    public final TestSetParseTable parseTable;

    public TestSetWithParseTable(String name, TestSetParseTable parseTable, TestSetInput<ContentType, Input> input) {
        super(name, input);
        this.parseTable = parseTable;
    }

}
