package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public interface IParserNotification<StackNode extends AbstractStackNode<ParseForest>, ParseForest extends AbstractParseForest> {

    public void notify(IParserObserver<StackNode, ParseForest> observer);

}
