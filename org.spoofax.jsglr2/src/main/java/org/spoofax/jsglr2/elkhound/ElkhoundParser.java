package org.spoofax.jsglr2.elkhound;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.Parser;
import org.spoofax.jsglr2.parsetable.IParseTable;
import org.spoofax.jsglr2.reducing.ReduceManager;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackManager;
import org.spoofax.jsglr2.stack.collections.IActiveStacksFactory;
import org.spoofax.jsglr2.stack.collections.IForActorStacksFactory;

public class ElkhoundParser<ParseForest extends AbstractParseForest, ParseNode extends ParseForest, Derivation, StackNode extends AbstractStackNode<ParseForest>>
    extends Parser<ParseForest, ParseNode, Derivation, StackNode> {

    private final ElkhoundReduceManager<ParseForest, ParseNode, Derivation> elkhoundReduceManager;

    @SuppressWarnings("unchecked")
    public ElkhoundParser(IParseTable parseTable, IActiveStacksFactory activeStacksFactory,
        IForActorStacksFactory forActorStacksFactory, StackManager<ParseForest, StackNode> stackManager,
        ParseForestManager<ParseForest, ParseNode, Derivation> parseForestManager,
        ElkhoundReduceManager<ParseForest, ParseNode, Derivation> elkhoundReduceManager) {
        super(parseTable, activeStacksFactory, forActorStacksFactory, stackManager, parseForestManager,
            (ReduceManager<ParseForest, ParseNode, Derivation, StackNode>) elkhoundReduceManager);

        this.elkhoundReduceManager = elkhoundReduceManager;
    }

}
