package org.spoofax.jsglr2.parser.observing;

import java.util.ArrayList;
import java.util.List;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public class ParserObserving<ParseForest extends AbstractParseForest, StackNode extends AbstractStackNode<ParseForest>> {

    private final List<IParserObserver<ParseForest, StackNode>> observers;

    public ParserObserving() {
        this.observers = new ArrayList<IParserObserver<ParseForest, StackNode>>();
    }

    public void notify(IParserNotification<ParseForest, StackNode> notification) {
        if(observers.isEmpty())
            return;

        for(IParserObserver<ParseForest, StackNode> observer : observers)
            notification.notify(observer);
    }

    public void attachObserver(IParserObserver<ParseForest, StackNode> observer) {
        observers.add(observer);
    }

}
