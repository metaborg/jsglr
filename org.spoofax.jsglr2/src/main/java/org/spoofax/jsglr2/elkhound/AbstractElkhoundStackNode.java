package org.spoofax.jsglr2.elkhound;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.states.IState;

public abstract class AbstractElkhoundStackNode<ParseForest extends AbstractParseForest>
    extends AbstractStackNode<ParseForest> {

    public boolean isRoot;
    public int deterministicDepth;
    public int referenceCount;

    protected AbstractElkhoundStackNode(int stackNumber, IState state, Position position, boolean isRoot) {
        super(stackNumber, state, position);
        this.isRoot = isRoot;
        this.deterministicDepth = isRoot ? 1 : 0;
        this.referenceCount = 0;
    }

    public abstract Iterable<StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>>> getLinks();

    public abstract StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> getOnlyLink();

    public abstract StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> addLink(
        StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> link,
        Parse<ParseForest, AbstractElkhoundStackNode<ParseForest>> parse);

    public StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> addLink(int linkNumber,
        AbstractElkhoundStackNode<ParseForest> parent, ParseForest parseNode,
        Parse<ParseForest, AbstractElkhoundStackNode<ParseForest>> parse) {
        StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> link =
            new StackLink<>(linkNumber, this, parent, parseNode);

        return addLink(link, parse);
    }

    public int resetDeterministicDepth() {
        if(isRoot)
            return 1;
        else if(deterministicDepth == 0)
            return 0;
        else
            return this.deterministicDepth = 1 + getOnlyLink().to.resetDeterministicDepth();
    }

}
