package org.spoofax.jsglr2.elkhound;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.StackLink;

public abstract class AbstractElkhoundStackNode<ParseForest extends IParseForest> implements IStackNode {

    public final IState state;
    public final Position position;

    public boolean isRoot;
    public int deterministicDepth;
    public int referenceCount;

    protected AbstractElkhoundStackNode(IState state, Position position, boolean isRoot) {
        this.state = state;
        this.position = position;

        this.isRoot = isRoot;
        this.deterministicDepth = isRoot ? 1 : 0;
        this.referenceCount = 0;
    }

    public IState state() {
        return state;
    }

    public Position position() {
        return position;
    }

    public abstract AbstractElkhoundStackNode<ParseForest> getOnlyLinkTo();

    public int resetDeterministicDepth() {
        if(isRoot)
            return 1;
        else if(deterministicDepth == 0)
            return 0;
        else
            return this.deterministicDepth = 1 + getOnlyLinkTo().resetDeterministicDepth();
    }

    public abstract <ElkhoundStackNode extends AbstractElkhoundStackNode<ParseForest>>
        Iterable<StackLink<ParseForest, ElkhoundStackNode>> getLinks();

}
