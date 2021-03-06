package org.spoofax.jsglr2.elkhound;

import java.util.ArrayList;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.collections.IActiveStacks;

public class BasicElkhoundStackNode
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>>
//@formatter:on
    extends AbstractElkhoundStackNode<ParseForest> {

    // Directed to the initial stack node
    private ArrayList<StackLink<ParseForest, BasicElkhoundStackNode<ParseForest, Derivation, ParseNode>>> links =
        new ArrayList<>();

    public BasicElkhoundStackNode(IState state, boolean isRoot) {
        super(state, isRoot);
    }

    @Override @SuppressWarnings("unchecked") public
        Iterable<StackLink<ParseForest, BasicElkhoundStackNode<ParseForest, Derivation, ParseNode>>> getLinks() {
        return links;
    }

    public StackLink<ParseForest, BasicElkhoundStackNode<ParseForest, Derivation, ParseNode>> getOnlyLink() {
        return links.get(0);
    }

    @Override public BasicElkhoundStackNode<ParseForest, Derivation, ParseNode> getOnlyLinkTo() {
        return links.get(0).to;
    }

    public StackLink<ParseForest, BasicElkhoundStackNode<ParseForest, Derivation, ParseNode>> addLink(
        ParserObserving<ParseForest, Derivation, ParseNode, BasicElkhoundStackNode<ParseForest, Derivation, ParseNode>, ?> observing,
        BasicElkhoundStackNode<ParseForest, Derivation, ParseNode> parent, ParseForest parseNode,
        IActiveStacks<BasicElkhoundStackNode<ParseForest, Derivation, ParseNode>> activeStacks) {
        StackLink<ParseForest, BasicElkhoundStackNode<ParseForest, Derivation, ParseNode>> link =
            new StackLink<>(this, parent, parseNode);

        links.add(link);

        link.to.referenceCount++;

        if(links.size() == 1) { // This means the first link is just added.
            deterministicDepth = link.to.deterministicDepth + 1;
        } else if(links.size() == 2) { // The second link is added; this means non-determinism
            deterministicDepth = 0;

            if(referenceCount > 0) {
                observing.notify(observer -> observer.resetDeterministicDepth(this));

                for(BasicElkhoundStackNode<ParseForest, Derivation, ParseNode> stack : activeStacks)
                    if(stack != this)
                        stack.resetDeterministicDepth();
            }
        } // We do not handle the case > 2, since the case == 2 already adjusted deterministic depths

        return link;
    }

    @Override public boolean allLinksRejected() {
        if(links.isEmpty())
            return false;

        for(StackLink<ParseForest, BasicElkhoundStackNode<ParseForest, Derivation, ParseNode>> link : links) {
            if(!link.isRejected())
                return false;
        }

        return true;
    }

}
