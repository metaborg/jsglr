package org.spoofax.jsglr2.stack.basic;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;

import java.util.ArrayList;
import java.util.List;

public class BasicStackNode<ParseForest> extends AbstractStackNode<ParseForest, BasicStackNode<ParseForest>> {

    // Directed to the initial stack node
    private final ArrayList<StackLink<ParseForest, BasicStackNode<ParseForest>>> links = new ArrayList<>();

    public BasicStackNode(IState state) {
        super(state);
    }

    @Override public List<StackLink<ParseForest, BasicStackNode<ParseForest>>> getLinks() {
        return links;
    }

    @Override public StackLink<ParseForest, BasicStackNode<ParseForest>>
        addLink(StackLink<ParseForest, BasicStackNode<ParseForest>> link) {
        links.add(link);

        return link;
    }

    @Override public boolean allLinksRejected() {
        if(links.isEmpty())
            return false;

        for(StackLink<ParseForest, BasicStackNode<ParseForest>> link : links) {
            if(!link.isRejected())
                return false;
        }

        return true;
    }

}
