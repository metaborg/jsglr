package org.spoofax.jsglr2.parser.observing;

import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.stack.IStackNode;

import java.util.ArrayList;
import java.util.List;

public class ParserObserving
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<ParseForest, StackNode>>
//@formatter:on
{

    private final List<IParserObserver<ParseForest, Derivation, ParseNode, StackNode, ParseState>> observers;

    public ParserObserving() {
        this.observers = new ArrayList<>();
    }

    public void notify(IParserNotification<ParseForest, Derivation, ParseNode, StackNode, ParseState> notification) {
        if(observers.isEmpty())
            return;

        for(IParserObserver<ParseForest, Derivation, ParseNode, StackNode, ParseState> observer : observers)
            notification.notify(observer);
    }

    public void attachObserver(IParserObserver<ParseForest, Derivation, ParseNode, StackNode, ParseState> observer) {
        observers.add(observer);
    }

}
