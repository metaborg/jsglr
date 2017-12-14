package org.spoofax.jsglr2.elkhound;

import java.util.ArrayList;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.states.IState;

public class BasicElkhoundStackNode<ParseForest extends AbstractParseForest>
    extends AbstractElkhoundStackNode<ParseForest> {

    // Directed to the initial stack node
    private ArrayList<StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>>> links =
        new ArrayList<StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>>>();

    public BasicElkhoundStackNode(int stackNumber, IState state, Position position, boolean isRoot) {
        super(stackNumber, state, position, isRoot);
    }

    @Override
    public Iterable<StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>>> getLinks() {
        return links;
    }

    @Override
    public StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> getOnlyLink() {
        return links.get(0);
    }

    @Override
    public StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> addLink(
        StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> link,
        Parse<ParseForest, AbstractElkhoundStackNode<ParseForest>> parse) {
        links.add(link);

        link.to.referenceCount++;

        if(links.size() == 1) { // This means the first link is just added.
            deterministicDepth = link.to.deterministicDepth + 1;
        } else if(links.size() == 2) { // The second link is added; this means non-determinism
            deterministicDepth = 0;

            if(referenceCount > 0) {
                parse.observing.notify(observer -> observer.resetDeterministicDepth(this));

                for(AbstractElkhoundStackNode<ParseForest> stack : parse.activeStacks)
                    if(stack != this)
                        stack.resetDeterministicDepth();
            }
        } // We do not handle the case > 2, since the case == 2 already adjusted deterministic depths

        return link;
    }

    @Override
    public boolean allLinksRejected() {
        if(links.isEmpty())
            return false;

        for(StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> link : links) {
            if(!link.isRejected())
                return false;
        }

        return true;
    }

}
