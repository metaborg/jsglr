package org.spoofax.jsglr2.stack.basic;

import java.util.ArrayList;
import java.util.List;

import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.states.IState;

public class BasicStackNode<ParseForest> extends AbstractBasicStackNode<ParseForest> {

    // Directed to the initial stack node
    private final ArrayList<StackLink<ParseForest, AbstractBasicStackNode<ParseForest>>> linksOut =
        new ArrayList<StackLink<ParseForest, AbstractBasicStackNode<ParseForest>>>();

    public BasicStackNode(int stackNumber, IState state, Position position) {
        super(stackNumber, state, position);
    }

    @Override
    public List<StackLink<ParseForest, AbstractBasicStackNode<ParseForest>>> getLinksOut() {
        return linksOut;
    }

    @Override
    public StackLink<ParseForest, AbstractBasicStackNode<ParseForest>>
        addOutLink(StackLink<ParseForest, AbstractBasicStackNode<ParseForest>> link) {
        linksOut.add(link);

        return link;
    }

    @Override
    public boolean allOutLinksRejected() {
        if(linksOut.isEmpty())
            return false;

        for(StackLink<ParseForest, AbstractBasicStackNode<ParseForest>> link : linksOut) {
            if(!link.isRejected())
                return false;
        }

        return true;
    }

}
