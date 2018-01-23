package org.spoofax.jsglr2.stack.basic;

import java.util.ArrayList;
import java.util.Collections;

import org.metaborg.parsetable.IState;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.util.iterators.SingleElementWithListIterable;

public class HybridStackNode<ParseForest> extends AbstractBasicStackNode<ParseForest> {

    private StackLink<ParseForest, AbstractBasicStackNode<ParseForest>> firstLink;
    private ArrayList<StackLink<ParseForest, AbstractBasicStackNode<ParseForest>>> otherLinks;

    public HybridStackNode(int stackNumber, IState state, Position position) {
        super(stackNumber, state, position);
    }

    @Override
    public Iterable<StackLink<ParseForest, AbstractBasicStackNode<ParseForest>>> getLinks() {
        if(otherLinks == null) {
            return Collections.singleton(firstLink);
        } else {
            return SingleElementWithListIterable.of(firstLink, otherLinks);
        }
    }

    @Override
    public StackLink<ParseForest, AbstractBasicStackNode<ParseForest>>
        addLink(StackLink<ParseForest, AbstractBasicStackNode<ParseForest>> link) {
        if(firstLink == null)
            firstLink = link;
        else {
            if(otherLinks == null)
                otherLinks = new ArrayList<>();

            otherLinks.add(link);
        }

        return link;
    }

    @Override
    public boolean allLinksRejected() {
        if(firstLink == null || !firstLink.isRejected())
            return false;

        if(otherLinks == null)
            return true;

        for(StackLink<ParseForest, AbstractBasicStackNode<ParseForest>> link : otherLinks) {
            if(!link.isRejected())
                return false;
        }

        return true;
    }

}
