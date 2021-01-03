package org.spoofax.jsglr2.inputstack;

import org.metaborg.parsetable.query.IActionQuery;
import org.spoofax.jsglr2.parser.Position;

public interface IInputStack extends IActionQuery {
    String inputString();

    boolean hasNext();

    void next();

    default void consumed() {};

    int getChar();

    int getChar(int offset);

    int offset();

    int length();

    IInputStack clone();

    default Position safePosition() {
        return Position.atOffset(inputString(), Math.max(Math.min(offset(), length() - 1), 0));
    }

    default Integer safeCharacter() {
        return offset() <= inputString().length() - 1 ? inputString().codePointAt(offset()) : null;
    }
}
