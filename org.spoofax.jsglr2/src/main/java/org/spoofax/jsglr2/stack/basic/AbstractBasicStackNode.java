package org.spoofax.jsglr2.stack.basic;

import org.metaborg.parsetable.IState;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;

public abstract class AbstractBasicStackNode<ParseForest> extends AbstractStackNode<ParseForest> {

    public AbstractBasicStackNode(int stackNumber, IState state, Position position) {
        super(stackNumber, state, position);
    }

    public abstract Iterable<StackLink<ParseForest, AbstractBasicStackNode<ParseForest>>> getLinks();

    public abstract StackLink<ParseForest, AbstractBasicStackNode<ParseForest>>
        addLink(StackLink<ParseForest, AbstractBasicStackNode<ParseForest>> link);

    public StackLink<ParseForest, AbstractBasicStackNode<ParseForest>> addLink(int linkNumber,
        AbstractBasicStackNode<ParseForest> parent, ParseForest parseNode) {
        StackLink<ParseForest, AbstractBasicStackNode<ParseForest>> link =
            new StackLink<ParseForest, AbstractBasicStackNode<ParseForest>>(linkNumber, this, parent, parseNode);

        return addLink(link);
    }

}
