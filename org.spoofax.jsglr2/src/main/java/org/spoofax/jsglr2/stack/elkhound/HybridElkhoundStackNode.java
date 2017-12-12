package org.spoofax.jsglr2.stack.elkhound;

import java.util.ArrayList;
import java.util.Collections;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.states.IState;
import org.spoofax.jsglr2.util.iterators.SingleElementWithListIterable;

public class HybridElkhoundStackNode<ParseForest extends AbstractParseForest>
    extends AbstractElkhoundStackNode<ParseForest> {

    // Directed to the initial stack node
    private StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> firstLink;
    private ArrayList<StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>>> otherLinks;

    public HybridElkhoundStackNode(int stackNumber, IState state, Position position, boolean isRoot) {
        super(stackNumber, state, position, isRoot);
    }

    @Override
    public Iterable<StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>>> getLinks() {
        if(otherLinks == null)
            return Collections.singleton(firstLink);
        else
            return SingleElementWithListIterable.of(firstLink, otherLinks);
    }

    @Override
    public StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> getOnlyLink() {
        return firstLink;
    }

    @Override
    public StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> addLink(
        StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> link,
        Parse<ParseForest, AbstractElkhoundStackNode<ParseForest>> parse) {
        link.to.referenceCount++;

        if(firstLink == null) { // This means where adding the first link now
            firstLink = link;

            deterministicDepth = link.to.deterministicDepth + 1;
        } else if(otherLinks == null) { // The second link is added; at this point we detect non-determinism
            otherLinks = new ArrayList<StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>>>();

            otherLinks.add(link);

            deterministicDepth = 0;

            if(referenceCount > 0) {
                parse.observing.notify(observer -> observer.resetDeterministicDepth(this));

                for(AbstractElkhoundStackNode<ParseForest> stack : parse.activeStacks)
                    if(stack != this)
                        stack.resetDeterministicDepth();
            }
        } else { // We do not handle the case > 2, since the case == 2 already adjusted deterministic depths
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

        for(StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> link : otherLinks) {
            if(!link.isRejected())
                return false;
        }

        return true;
    }

}
