package org.spoofax.jsglr2.inputstack;

import org.metaborg.parsetable.query.IActionQuery;

public interface IInputStack extends IActionQuery {
    String inputString();

    String fileName();

    boolean hasNext();

    void next();

    int getChar();

    int getChar(int offset);

    int offset();

    int length();

    IInputStack clone();
}
