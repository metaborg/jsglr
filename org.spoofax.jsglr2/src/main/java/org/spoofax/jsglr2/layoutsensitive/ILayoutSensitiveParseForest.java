package org.spoofax.jsglr2.layoutsensitive;

import org.spoofax.jsglr2.parseforest.basic.IBasicParseForest;
import org.spoofax.jsglr2.parser.Position;

public interface ILayoutSensitiveParseForest extends IBasicParseForest {

    Position getStartPosition();

    Position getEndPosition();

    Position getLeftPosition();

    Position getRightPosition();

}
