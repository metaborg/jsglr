package org.spoofax.jsglr2.elkhound;

import java.util.ArrayList;
import java.util.Collections;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.collections.IActiveStacks;
import org.spoofax.jsglr2.util.iterators.SingleElementWithListIterable;

public class HybridElkhoundStackNode
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>>
//@formatter:on
    extends AbstractElkhoundStackNode<ParseForest> {

    // Directed to the initial stack node
    private StackLink<ParseForest, HybridElkhoundStackNode<ParseForest, Derivation, ParseNode>> firstLink;
    private ArrayList<StackLink<ParseForest, HybridElkhoundStackNode<ParseForest, Derivation, ParseNode>>> otherLinks;

    public HybridElkhoundStackNode(IState state, boolean isRoot) {
        super(state, isRoot);
    }

    @Override @SuppressWarnings("unchecked") public
        Iterable<StackLink<ParseForest, HybridElkhoundStackNode<ParseForest, Derivation, ParseNode>>> getLinks() {
        if(otherLinks == null)
            return Collections.singleton(firstLink);
        else
            return SingleElementWithListIterable.of(firstLink, otherLinks);
    }

    public StackLink<ParseForest, HybridElkhoundStackNode<ParseForest, Derivation, ParseNode>> getOnlyLink() {
        return firstLink;
    }

    @Override public HybridElkhoundStackNode<ParseForest, Derivation, ParseNode> getOnlyLinkTo() {
        return firstLink.to;
    }

    public StackLink<ParseForest, HybridElkhoundStackNode<ParseForest, Derivation, ParseNode>> addLink(
        ParserObserving<ParseForest, Derivation, ParseNode, HybridElkhoundStackNode<ParseForest, Derivation, ParseNode>, ?> observing,
        HybridElkhoundStackNode<ParseForest, Derivation, ParseNode> parent, ParseForest parseNode,
        IActiveStacks<HybridElkhoundStackNode<ParseForest, Derivation, ParseNode>> activeStacks) {
        StackLink<ParseForest, HybridElkhoundStackNode<ParseForest, Derivation, ParseNode>> link =
            new StackLink<>(this, parent, parseNode);

        link.to.referenceCount++;

        if(firstLink == null) { // This means where adding the first link now
            firstLink = link;

            deterministicDepth = link.to.deterministicDepth + 1;
        } else if(otherLinks == null) { // The second link is added; at this point we detect non-determinism
            otherLinks = new ArrayList<>();

            otherLinks.add(link);

            deterministicDepth = 0;

            if(referenceCount > 0) {
                observing.notify(observer -> observer.resetDeterministicDepth(this));

                for(HybridElkhoundStackNode<ParseForest, Derivation, ParseNode> stack : activeStacks)
                    if(stack != this)
                        stack.resetDeterministicDepth();
            }
        } else { // We do not handle the case > 2, since the case == 2 already adjusted deterministic depths
            otherLinks.add(link);
        }

        return link;
    }

    @Override public boolean allLinksRejected() {
        if(firstLink == null || !firstLink.isRejected())
            return false;

        if(otherLinks == null)
            return true;

        for(StackLink<ParseForest, HybridElkhoundStackNode<ParseForest, Derivation, ParseNode>> link : otherLinks) {
            if(!link.isRejected())
                return false;
        }

        return true;
    }

}
