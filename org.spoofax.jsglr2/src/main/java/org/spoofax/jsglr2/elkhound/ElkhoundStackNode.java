package org.spoofax.jsglr2.elkhound;

import org.metaborg.parsetable.IState;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;

public abstract class ElkhoundStackNode<ParseForest extends IParseForest> extends AbstractStackNode<ParseForest> {

    public boolean isRoot;
    public int deterministicDepth;
    public int referenceCount;

    protected ElkhoundStackNode(IState state, boolean isRoot) {
        super(state);
        this.isRoot = isRoot;
        this.deterministicDepth = isRoot ? 1 : 0;
        this.referenceCount = 0;
    }

    public abstract ElkhoundStackNode<ParseForest> getOnlyLinkTo();

    public int resetDeterministicDepth() {
        if(isRoot)
            return 1;
        else if(deterministicDepth == 0)
            return 0;
        else
            return this.deterministicDepth = 1 + getOnlyLinkTo().resetDeterministicDepth();
    }

    public abstract <ElkhoundStackNode extends org.spoofax.jsglr2.elkhound.ElkhoundStackNode<ParseForest>>
        Iterable<StackLink<ParseForest, ElkhoundStackNode>> getLinks();

}
