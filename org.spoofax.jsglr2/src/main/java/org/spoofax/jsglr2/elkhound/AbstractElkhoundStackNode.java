package org.spoofax.jsglr2.elkhound;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.StackLink;

public abstract class AbstractElkhoundStackNode<ParseForest extends IParseForest> implements IStackNode {

    public final IState state;

    public boolean isRoot;
    public int deterministicDepth;
    public int referenceCount;

    protected AbstractElkhoundStackNode(IState state, boolean isRoot) {
        this.state = state;

        this.isRoot = isRoot;
        this.deterministicDepth = isRoot ? 1 : 0;
        this.referenceCount = 0;
    }

    public IState state() {
        return state;
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
