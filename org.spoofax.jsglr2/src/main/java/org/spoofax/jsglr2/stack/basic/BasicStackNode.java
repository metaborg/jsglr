package org.spoofax.jsglr2.stack.basic;

import java.util.ArrayList;
import java.util.List;

import org.metaborg.parsetable.IState;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.stack.StackLink;

public class BasicStackNode<ParseForest> extends AbstractBasicStackNode<ParseForest> {

    // Directed to the initial stack node
    private final ArrayList<StackLink<ParseForest, AbstractBasicStackNode<ParseForest>>> links =
        new ArrayList<StackLink<ParseForest, AbstractBasicStackNode<ParseForest>>>();

    public BasicStackNode(int stackNumber, IState state, Position position) {
        super(stackNumber, state, position);
    }

    @Override
    public List<StackLink<ParseForest, AbstractBasicStackNode<ParseForest>>> getLinks() {
        return links;
    }

    @Override
    public StackLink<ParseForest, AbstractBasicStackNode<ParseForest>>
        addLink(StackLink<ParseForest, AbstractBasicStackNode<ParseForest>> link) {
        links.add(link);

        return link;
    }

    @Override
    public boolean allLinksRejected() {
        if(links.isEmpty())
            return false;

        for(StackLink<ParseForest, AbstractBasicStackNode<ParseForest>> link : links) {
            if(!link.isRejected())
                return false;
        }

        return true;
    }

}
