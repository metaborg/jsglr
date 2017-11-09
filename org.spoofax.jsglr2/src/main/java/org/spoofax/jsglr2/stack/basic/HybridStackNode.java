package org.spoofax.jsglr2.stack.basic;

import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.parsetable.IState;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.util.iterators.SingleElementWithListIterable;

import java.util.ArrayList;
import java.util.Collections;

public class HybridStackNode<ParseForest> extends AbstractBasicStackNode<ParseForest> {

    private StackLink<AbstractBasicStackNode<ParseForest>, ParseForest> firstLinkOut;
    private ArrayList<StackLink<AbstractBasicStackNode<ParseForest>, ParseForest>> otherLinksOut;

    public HybridStackNode(int stackNumber, IState state, Position position) {
        super(stackNumber, state, position);
    }

    @Override
    public Iterable<StackLink<AbstractBasicStackNode<ParseForest>, ParseForest>> getLinksOut() {
        if(otherLinksOut == null) {
            return Collections.singleton(firstLinkOut);
        } else {
            return SingleElementWithListIterable.of(firstLinkOut, otherLinksOut);
        }
    }

    @Override
    public StackLink<AbstractBasicStackNode<ParseForest>, ParseForest>
        addOutLink(StackLink<AbstractBasicStackNode<ParseForest>, ParseForest> link) {
        if(firstLinkOut == null)
            firstLinkOut = link;
        else {
            if(otherLinksOut == null)
                otherLinksOut = new ArrayList<>();

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

        for(StackLink<AbstractBasicStackNode<ParseForest>, ParseForest> link : otherLinksOut) {
            if(!link.isRejected())
                return false;
        }

        return true;
    }

}
