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

    // Directed to the initial stack node (to the origin)
    private StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> firstLinkOut;
    private ArrayList<StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>>> otherLinksOut;

    public HybridElkhoundStackNode(int stackNumber, IState state, Position position, boolean isRoot) {
        super(stackNumber, state, position, isRoot);
    }

    @Override
    public Iterable<StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>>> getLinksOut() {
        if(otherLinksOut == null)
            return Collections.singleton(firstLinkOut);
        else
            return SingleElementWithListIterable.of(firstLinkOut, otherLinksOut);
    }

    @Override
    public StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> getOnlyLinkOut() {
        return firstLinkOut;
    }

    @Override
    public StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> addOutLink(
        StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> link,
        Parse<ParseForest, AbstractElkhoundStackNode<ParseForest>> parse) {
        link.to.referenceCount++;

        if(firstLinkOut == null) { // This means where adding the first link now
            firstLinkOut = link;

            deterministicDepth = link.to.deterministicDepth + 1;
        } else if(otherLinksOut == null) { // The second link is added; at this point we detect non-determinism
            otherLinksOut = new ArrayList<StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>>>();

            otherLinksOut.add(link);

            deterministicDepth = 0;

            if(referenceCount > 0) {
                parse.observing.notify(observer -> observer.resetDeterministicDepth(this));

                for(AbstractElkhoundStackNode<ParseForest> stack : parse.activeStacks)
                    if(stack != this)
                        stack.resetDeterministicDepth();
            }
        } else { // We do not handle the case > 2, since the case == 2 already adjusted deterministic depths
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

        for(StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> link : otherLinksOut) {
            if(!link.isRejected())
                return false;
        }

        return true;
    }

}
