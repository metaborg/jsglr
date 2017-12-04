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

    // Directed from the initial stack node (to the future)
    private StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> firstLinkIn;
    private ArrayList<StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>>> otherLinksIn;

    public HybridElkhoundStackNode(int stackNumber, IState state, Position position, int deterministicDepth) {
        super(stackNumber, state, position, deterministicDepth);
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
    public Iterable<StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>>> getLinksIn() {
        if(firstLinkIn == null) {
            return Collections.emptyList();
        } else if(otherLinksIn == null) {
            return Collections.singleton(firstLinkIn);
        } else {
            return SingleElementWithListIterable.of(firstLinkIn, otherLinksIn);
        }
    }

    @Override
    public StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> addOutLink(
        StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> link,
        Parse<ParseForest, AbstractElkhoundStackNode<ParseForest>> parse) {
        link.to.addInLink(link);

        if(firstLinkOut == null) { // This means where adding the first link now
            firstLinkOut = link;

            deterministicDepth = link.to.deterministicDepth + 1;
        } else if(otherLinksOut == null) { // The second link is added; at this point we detect non-determinism
            otherLinksOut = new ArrayList<StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>>>();

            otherLinksOut.add(link);

            deterministicDepth = 0;

            parse.observing.notify(observer -> observer.resetDeterministicDepth(this));

            for(StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> linkIn : getLinksIn())
                linkIn.from.resetDeterministicDepth(1);
        } else { // We do not handle the case > 2, since the case == 2 already adjusted deterministic depths
            otherLinksOut.add(link);
        }

        return link;
    }

    @Override
    protected void addInLink(StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> link) {
        if(firstLinkIn == null)
            firstLinkIn = link;
        else {
            if(otherLinksIn == null)
                otherLinksIn = new ArrayList<StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>>>();

            otherLinksIn.add(link);
        }
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
