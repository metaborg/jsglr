package org.spoofax.jsglr2.testset;

public class TestSetParseTableFromGrammarDef extends TestSetParseTable {

    public final String name; // Path of file (without .def extension) in org.spoofax.jsglr2/src/test/resources/grammars

    protected TestSetParseTableFromGrammarDef(String name) {
        super(Source.GRAMMAR_DEF);

        this.name = name;
    }

}
