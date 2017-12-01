package org.spoofax.jsglr2.stack.elkhound;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.states.IState;

public abstract class AbstractElkhoundStackNode<ParseForest extends AbstractParseForest>
    extends AbstractStackNode<ParseForest> {

    public int deterministicDepth;

    public AbstractElkhoundStackNode(int stackNumber, IState state, Position position, int deterministicDepth) {
        super(stackNumber, state, position);
        this.deterministicDepth = deterministicDepth;
    }

    public abstract Iterable<StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>>> getLinksOut();

    public abstract StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> getOnlyLinkOut();

    public abstract Iterable<StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>>> getLinksIn();

    public abstract StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> addOutLink(
        StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> link,
        Parse<ParseForest, AbstractElkhoundStackNode<ParseForest>> parse);

    public StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> addOutLink(int linkNumber,
        AbstractElkhoundStackNode<ParseForest> parent, ParseForest parseNode,
        Parse<ParseForest, AbstractElkhoundStackNode<ParseForest>> parse) {
        StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> link =
            new StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>>(linkNumber, this, parent, parseNode);

        return addOutLink(link, parse);
    }

    protected abstract void addInLink(StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> link);

    public void resetDeterministicDepth(int deterministicDepth) {
        if(deterministicDepth != 0) {
            this.deterministicDepth = deterministicDepth;

            for(StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> linkIn : getLinksIn())
                linkIn.from.resetDeterministicDepth(deterministicDepth + 1);
        }
    }

}
