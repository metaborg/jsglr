package org.spoofax.jsglr2.stack.basic;

import java.util.ArrayList;
import java.util.Collections;

import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.states.IState;
import org.spoofax.jsglr2.util.iterators.SingleElementWithListIterable;

public class HybridStackNode<ParseForest> extends AbstractBasicStackNode<ParseForest> {

    private StackLink<ParseForest, AbstractBasicStackNode<ParseForest>> firstLinkOut;
    private ArrayList<StackLink<ParseForest, AbstractBasicStackNode<ParseForest>>> otherLinksOut;

    public HybridStackNode(int stackNumber, IState state, Position position) {
        super(stackNumber, state, position);
    }

    @Override
    public Iterable<StackLink<ParseForest, AbstractBasicStackNode<ParseForest>>> getLinksOut() {
        if(otherLinksOut == null) {
            return Collections.singleton(firstLinkOut);
        } else {
            return SingleElementWithListIterable.of(firstLinkOut, otherLinksOut);
        }
    }

    @Override
    public StackLink<ParseForest, AbstractBasicStackNode<ParseForest>>
        addOutLink(StackLink<ParseForest, AbstractBasicStackNode<ParseForest>> link) {
        if(firstLinkOut == null)
            firstLinkOut = link;
        else {
            if(otherLinksOut == null)
                otherLinksOut = new ArrayList<>();

            otherLinksOut.add(link);
        }

        return link;
    }

    @Override
    public boolean allOutLinksRejected() {
        if(firstLinkOut == null || !firstLinkOut.isRejected())
            return false;

        if(otherLinksOut == null)
            return true;

        for(StackLink<ParseForest, AbstractBasicStackNode<ParseForest>> link : otherLinksOut) {
            if(!link.isRejected())
                return false;
        }

        return true;
    }

}
