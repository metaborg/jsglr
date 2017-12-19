package org.spoofax.jsglr2.parser;

public interface IParseInput {

    int getCurrentChar();

    String getLookahead(int length);

}
