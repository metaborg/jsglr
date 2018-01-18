package org.spoofax.jsglr2.elkhound;

import org.metaborg.parsetable.IState;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;

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
