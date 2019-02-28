package org.spoofax.jsglr2.stack.hybrid;

import java.util.ArrayList;
import java.util.Collections;

import org.metaborg.parsetable.IState;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.StackNode;
import org.spoofax.jsglr2.util.iterators.SingleElementWithListIterable;

public class HybridStackNode<ParseForest> extends StackNode<ParseForest> {

    private StackLink<ParseForest, StackNode<ParseForest>> firstLink;
    private ArrayList<StackLink<ParseForest, StackNode<ParseForest>>> otherLinks;

    public HybridStackNode(IState state, Position position) {
        super(state, position);
    }

    @Override public Iterable<StackLink<ParseForest, StackNode<ParseForest>>> getLinks() {
        if(otherLinks == null) {
            return Collections.singleton(firstLink);
        } else {
            return SingleElementWithListIterable.of(firstLink, otherLinks);
        }
    }

    @Override public StackLink<ParseForest, StackNode<ParseForest>>
        addLink(StackLink<ParseForest, StackNode<ParseForest>> link) {
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

        for(StackLink<ParseForest, StackNode<ParseForest>> link : otherLinks) {
            if(!link.isRejected())
                return false;
        }

        return true;
    }

}
