package org.spoofax.jsglr2.stack;

import org.spoofax.jsglr2.elkhound.BasicElkhoundStackManager;
import org.spoofax.jsglr2.elkhound.BasicElkhoundStackNode;
import org.spoofax.jsglr2.elkhound.HybridElkhoundStackManager;
import org.spoofax.jsglr2.elkhound.HybridElkhoundStackNode;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.stack.basic.BasicStackManager;
import org.spoofax.jsglr2.stack.basic.BasicStackNode;
import org.spoofax.jsglr2.stack.hybrid.HybridStackManager;
import org.spoofax.jsglr2.stack.hybrid.HybridStackNode;

public interface StackManagerFactory
//@formatter:off
    <ParseForest  extends IParseForest,
     StackNode    extends IStackNode,
     Parse        extends AbstractParse<ParseForest, StackNode>,
     StackManager extends AbstractStackManager<ParseForest, StackNode, Parse>>
//@formatter:on
{
    StackManager get();

    static <ParseForest extends IParseForest, Parse extends AbstractParse<ParseForest, BasicStackNode<ParseForest>>>
        StackManagerFactory<ParseForest, BasicStackNode<ParseForest>, Parse, BasicStackManager<ParseForest, Parse>>
        basicStackManagerFactory() {
        return BasicStackManager<ParseForest, Parse>::new;
    }

    static <ParseForest extends IParseForest, Parse extends AbstractParse<ParseForest, HybridStackNode<ParseForest>>>
        StackManagerFactory<ParseForest, HybridStackNode<ParseForest>, Parse, HybridStackManager<ParseForest, Parse>>
        hybridStackManagerFactory() {
        return HybridStackManager<ParseForest, Parse>::new;
    }

    static <ParseForest extends IParseForest, Parse extends AbstractParse<ParseForest, BasicElkhoundStackNode<ParseForest>>>
        StackManagerFactory<ParseForest, BasicElkhoundStackNode<ParseForest>, Parse, BasicElkhoundStackManager<ParseForest, Parse>>
        basicElkhoundStackManagerFactory() {
        return BasicElkhoundStackManager<ParseForest, Parse>::new;
    }

    static <ParseForest extends IParseForest, Parse extends AbstractParse<ParseForest, HybridElkhoundStackNode<ParseForest>>>
        StackManagerFactory<ParseForest, HybridElkhoundStackNode<ParseForest>, Parse, HybridElkhoundStackManager<ParseForest, Parse>>
        hybridElkhoundStackManagerFactory() {
        return HybridElkhoundStackManager<ParseForest, Parse>::new;
    }
}
