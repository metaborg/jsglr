package org.spoofax.jsglr2.stack.elkhound;

import java.util.ArrayList;
import java.util.Collections;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.parsetable.IState;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.util.iterators.SingleElementWithListIterable;

public class HybridElkhoundStackNode<ParseForest extends AbstractParseForest>
    extends AbstractElkhoundStackNode<ParseForest> {

    // Directed to the initial stack node (to the origin)
    private StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> firstLinkOut;
    private ArrayList<StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest>> otherLinksOut;

    // Directed from the initial stack node (to the future)
    private StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> firstLinkIn;
    private ArrayList<StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest>> otherLinksIn;

    public HybridElkhoundStackNode(int stackNumber, IState state, Position position, int deterministicDepth) {
        super(stackNumber, state, position, deterministicDepth);
    }

    public Iterable<StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest>> getLinksOut() {
        if(otherLinksOut == null)
            return Collections.singleton(firstLinkOut);
        else
            return SingleElementWithListIterable.of(firstLinkOut, otherLinksOut);
    }

    public StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> getOnlyLinkOut() {
        return firstLinkOut;
    }

    public Iterable<StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest>> getLinksIn() {
        if(firstLinkIn == null) {
            return Collections.emptyList();
        } else if(otherLinksIn == null) {
            return Collections.singleton(firstLinkIn);
        } else {
            return SingleElementWithListIterable.of(firstLinkIn, otherLinksIn);
        }
    }

    public StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> addOutLink(
        StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> link,
        Parse<AbstractElkhoundStackNode<ParseForest>, ParseForest> parse) {
        link.to.addInLink(link);

        if(firstLinkOut == null) { // This means where adding the first link now
            firstLinkOut = link;

            deterministicDepth = link.to.deterministicDepth + 1;
        } else if(otherLinksOut == null) { // The second link is added; at this point we detect non-determinism
            otherLinksOut = new ArrayList<StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest>>();

            otherLinksOut.add(link);

            deterministicDepth = 0;

            parse.notify(observer -> observer.resetDeterministicDepth(this));

            for(StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> linkIn : getLinksIn())
                linkIn.from.resetDeterministicDepth(1);
        } else { // We do not handle the case > 2, since the case == 2 already adjusted deterministic depths
            otherLinksOut.add(link);
        }

        return link;
    }

    protected void addInLink(StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> link) {
        if(firstLinkIn == null)
            firstLinkIn = link;
        else {
            if(otherLinksIn == null)
                otherLinksIn = new ArrayList<StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest>>();

            otherLinksIn.add(link);
        }
    }

    public boolean allOutLinksRejected() {
        if(firstLinkOut == null || !firstLinkOut.isRejected())
            return false;

        if(otherLinksOut == null)
            return true;

        for(StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> link : otherLinksOut) {
            if(!link.isRejected())
                return false;
        }

        return true;
    }

}
