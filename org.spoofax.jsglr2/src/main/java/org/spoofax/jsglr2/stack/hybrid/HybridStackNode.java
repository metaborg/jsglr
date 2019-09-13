package org.spoofax.jsglr2.stack.hybrid;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.util.iterators.SingleElementWithListIterable;

import java.util.ArrayList;
import java.util.Collections;

public class HybridStackNode<ParseForest> extends AbstractStackNode<ParseForest, HybridStackNode<ParseForest>> {

    private StackLink<ParseForest, HybridStackNode<ParseForest>> firstLink;
    private ArrayList<StackLink<ParseForest, HybridStackNode<ParseForest>>> otherLinks;

    public HybridStackNode(IState state) {
        super(state);
    }

    @Override public Iterable<StackLink<ParseForest, HybridStackNode<ParseForest>>> getLinks() {
        if(otherLinks == null) {
            return Collections.singleton(firstLink);
        } else {
            return SingleElementWithListIterable.of(firstLink, otherLinks);
        }
    }

    @Override public StackLink<ParseForest, HybridStackNode<ParseForest>>
        addLink(StackLink<ParseForest, HybridStackNode<ParseForest>> link) {
        if(firstLink == null)
            firstLink = link;
        else {
            if(otherLinks == null)
                otherLinks = new ArrayList<>();

            otherLinks.add(link);
        }

        return link;
    }

    @Override public boolean allLinksRejected() {
        if(firstLink == null || !firstLink.isRejected())
            return false;

        if(otherLinks == null)
            return true;

        for(StackLink<ParseForest, HybridStackNode<ParseForest>> link : otherLinks) {
            if(!link.isRejected())
                return false;
        }

        return true;
    }

}
