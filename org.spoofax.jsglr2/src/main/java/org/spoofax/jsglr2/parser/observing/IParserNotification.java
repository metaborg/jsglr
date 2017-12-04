package org.spoofax.jsglr2.parser.observing;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public interface IParserNotification<ParseForest extends AbstractParseForest, StackNode extends AbstractStackNode<ParseForest>> {

    public void notify(IParserObserver<ParseForest, StackNode> observer);

}
