package org.spoofax.jsglr2.stack.basic;

import java.util.ArrayList;
import java.util.List;

import org.metaborg.parsetable.IState;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.StackNode;

public class BasicStackNode<ParseForest> extends StackNode<ParseForest> {

    // Directed to the initial stack node
    private final ArrayList<StackLink<ParseForest, StackNode<ParseForest>>> links = new ArrayList<>();

    public BasicStackNode(IState state) {
        super(state);
    }

    @Override public List<StackLink<ParseForest, StackNode<ParseForest>>> getLinks() {
        return links;
    }

    @Override public StackLink<ParseForest, StackNode<ParseForest>>
        addLink(StackLink<ParseForest, StackNode<ParseForest>> link) {
        links.add(link);

        return link;
    }

    @Override public boolean allLinksRejected() {
        if(links.isEmpty())
            return false;

        for(StackLink<ParseForest, StackNode<ParseForest>> link : links) {
            if(!link.isRejected())
                return false;
        }

        return true;
    }

}
