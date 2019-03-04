package org.spoofax.jsglr2.elkhound;

import java.util.ArrayList;

import org.metaborg.parsetable.IState;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.stack.StackLink;

public class BasicElkhoundStackNode<ParseForest extends IParseForest> extends ElkhoundStackNode<ParseForest> {

    // Directed to the initial stack node
    private ArrayList<StackLink<ParseForest, BasicElkhoundStackNode<ParseForest>>> links = new ArrayList<>();

    public BasicElkhoundStackNode(IState state, boolean isRoot) {
        super(state, isRoot);
    }

    @Override @SuppressWarnings("unchecked") public
        Iterable<StackLink<ParseForest, BasicElkhoundStackNode<ParseForest>>> getLinks() {
        return links;
    }

    public StackLink<ParseForest, BasicElkhoundStackNode<ParseForest>> getOnlyLink() {
        return links.get(0);
    }

    @Override public BasicElkhoundStackNode<ParseForest> getOnlyLinkTo() {
        return links.get(0).to;
    }

    public StackLink<ParseForest, BasicElkhoundStackNode<ParseForest>> addLink(
        BasicElkhoundStackNode<ParseForest> parent, ParseForest parseNode,
        AbstractParse<ParseForest, BasicElkhoundStackNode<ParseForest>> parse) {
        StackLink<ParseForest, BasicElkhoundStackNode<ParseForest>> link = new StackLink<>(this, parent, parseNode);

        links.add(link);

        link.to.referenceCount++;

        if(links.size() == 1) { // This means the first link is just added.
            deterministicDepth = link.to.deterministicDepth + 1;
        } else if(links.size() == 2) { // The second link is added; this means non-determinism
            deterministicDepth = 0;

            if(referenceCount > 0) {
                parse.observing.notify(observer -> observer.resetDeterministicDepth(this));

                for(ElkhoundStackNode<ParseForest> stack : parse.activeStacks)
                    if(stack != this)
                        stack.resetDeterministicDepth();
            }
        } // We do not handle the case > 2, since the case == 2 already adjusted deterministic depths

        return link;
    }

    @Override public boolean allLinksRejected() {
        if(links.isEmpty())
            return false;

        for(StackLink<ParseForest, BasicElkhoundStackNode<ParseForest>> link : links) {
            if(!link.isRejected())
                return false;
        }

        return true;
    }

}
