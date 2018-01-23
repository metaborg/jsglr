package org.spoofax.jsglr2.tests.util;

import org.metaborg.parsetable.IParseInput;

public class MockParseInput implements IParseInput {

    private final int character;

    public MockParseInput(int character) {
        this.character = character;
    }

    @Override
    public int getCurrentChar() {
        return character;
    }

    @Override
    public String getLookahead(int length) {
        return "";
    }

}
